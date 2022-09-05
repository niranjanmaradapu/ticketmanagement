/**
 * 
 */
package com.otsi.retail.ticketservice.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.otsi.retail.ticketservice.bindings.ClientDetailsVO;
import com.otsi.retail.ticketservice.bindings.ReportsVo;
import com.otsi.retail.ticketservice.bindings.Ticket;
import com.otsi.retail.ticketservice.common.TicketStatus;
import com.otsi.retail.ticketservice.config.Config;
import com.otsi.retail.ticketservice.constants.AppConstants;
import com.otsi.retail.ticketservice.entities.CommentEntity;
import com.otsi.retail.ticketservice.entities.FeedBackEntity;
import com.otsi.retail.ticketservice.entities.TicketEntity;
import com.otsi.retail.ticketservice.exceptions.InvalidDataException;
import com.otsi.retail.ticketservice.exceptions.RecordNotFoundException;
import com.otsi.retail.ticketservice.exceptions.RegAppException;
import com.otsi.retail.ticketservice.gatewayresponse.GateWayResponse;
import com.otsi.retail.ticketservice.mapper.TicketMapper;
import com.otsi.retail.ticketservice.props.AppProperties;
import com.otsi.retail.ticketservice.repository.CommentRepository;
import com.otsi.retail.ticketservice.repository.FeedBackRepository;
import com.otsi.retail.ticketservice.repository.TicketRepository;
import com.otsi.retail.ticketservice.utils.EmailUtils;
import com.otsi.retail.ticketservice.utils.FileUploadUtils;

/**
 * @author Sudheer.Swamy
 *
 */
@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private TicketMapper ticketMapper;

	@Autowired
	private AppProperties appProps;

	@Autowired
	private EmailUtils emaiUtils;

	@Autowired
	private FileUploadUtils fileUploadUtils;

	@Autowired
	private FeedBackRepository feedBackRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private Config config;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * @SavTicket ticket creation API
	 */
	@Override
	public boolean saveTicket(Ticket ticket) {

		if (ticket.getTicketId() == null) {
			ticket.setTicketId("TK" + LocalDate.now().getYear() + LocalDate.now().getDayOfMonth() + LocalDate.now()
					+ getSaltString());
			ticket.setStatus(TicketStatus.OPEN);

			TicketEntity ticketEnt = ticketMapper.convertVoToEntity(ticket);

			TicketEntity save = ticketRepository.save(ticketEnt);

			if (null != save.getTicketId()) {
				return sendEmail(ticket);
			}
		} else if (ticket.getTicketId() != null && ticket.getFeedBackVo() != null) {

			TicketEntity ticketEntity = ticketRepository.findByTicketId(ticket.getTicketId());
			if (ticketEntity == null)
				throw new RecordNotFoundException("Record not found");
			FeedBackEntity feedBackEntity = new FeedBackEntity();
			feedBackEntity.setId(ticket.getFeedBackVo().getId());
			feedBackEntity.setWorkQuality(ticket.getFeedBackVo().getWorkQuality());
			feedBackEntity.setResponseTime(ticket.getFeedBackVo().getResponseTime());
			feedBackEntity.setIssueResolutionTime(ticket.getFeedBackVo().getIssueResolutionTime());
			feedBackEntity.setOverallRating(ticket.getFeedBackVo().getOverallRating());
			// set feedback into ticket
			ticketEntity.setFeedBackEntity(feedBackEntity);
			feedBackRepository.save(feedBackEntity);

		} else if (ticket.getTicketId() != null && !(CollectionUtils.isEmpty(ticket.getCommentsVo()))) {

			TicketEntity ticketEntity = ticketRepository.findByTicketId(ticket.getTicketId());
			if (ticketEntity == null)
				throw new RecordNotFoundException("Record not found");
			List<CommentEntity> commentList = new ArrayList<>();
			ticket.getCommentsVo().stream().forEach(c -> {

				CommentEntity commentEntity = new CommentEntity();
				commentEntity.setId(c.getId());
				commentEntity.setComment(c.getComment());
				// set ticket into comment
				commentEntity.setTicketEntity(ticketEntity);
				commentList.add(commentEntity);
				commentRepository.save(commentEntity);
			});
			// set comments into ticket
			ticketEntity.setComments(commentList);
		}

		return false;

	}

	/**
	 * This is a private method
	 * 
	 * @param ticket
	 * @SendTextEmail sending text email api
	 */
	private boolean sendEmail(Ticket ticket) {

		boolean isMailSent = false;
		try {
			Map<String, String> messages = appProps.getMessages();
			String subject = messages.get(AppConstants.TICKET_MAIL_SUBJECT);
			String bodyFileName = messages.get(AppConstants.TICKET_MAIL_BODY_TEMPLATE_FILE);
			String body = readMailBody(bodyFileName, ticket);
			emaiUtils.sendEmail(subject, body, ticket.getEmailId());
			isMailSent = true;

		} catch (Exception e) {
			throw new RegAppException(e.getMessage());
		}

		return isMailSent;

	}

	/**
	 * This is a private method
	 * 
	 * @ReadMailBody to read mail body file API
	 */
	private String readMailBody(String fileName, Ticket ticket) {

		String mailBody = null;
		StringBuffer buffer = new StringBuffer();

		Path path = Paths.get(fileName);
		try (Stream<String> stream = Files.lines(path)) {
			stream.forEach(line -> {
				buffer.append(line);
			});
			mailBody = buffer.toString();
			mailBody = mailBody.replace(AppConstants.EMAIL, ticket.getEmailId());
			mailBody = mailBody.replace(AppConstants.MODULE, ticket.getModuleType());
			mailBody = mailBody.replace(AppConstants.TICKET_ID, ticket.getTicketId());
			mailBody = mailBody.replace(AppConstants.PRIORITY, String.valueOf(ticket.getPriority()));
			mailBody = mailBody.replace(AppConstants.STATUS, String.valueOf(ticket.getStatus()));
			mailBody = mailBody.replace(AppConstants.SUBJECT, ticket.getTitle());

		} catch (IOException e) {
			e.printStackTrace();
		}
		return mailBody;
	}

	/**
	 * @throws URISyntaxException
	 * @getTicketsByStatusAPI get the tickets by status api
	 *
	 */
	@Override
	public List<Ticket> getTicketsByStatus(TicketStatus status, Long userId) throws URISyntaxException {

		List<TicketEntity> ticketEntity = new ArrayList<>();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> entity = new HttpEntity<Object>(headers);

		URI uri = null;

		uri = UriComponentsBuilder.fromUri(new URI(config.getGetClientsByUserIdFromUrm() + "?userId=" + userId)).build()
				.encode().toUri();
		ResponseEntity<?> clientsResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, GateWayResponse.class);

		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		GateWayResponse<?> gatewayResponse = mapper.convertValue(clientsResponse.getBody(), GateWayResponse.class);

		List<ClientDetailsVO> listOfClients = mapper.convertValue(gatewayResponse.getResult(),
				new TypeReference<List<ClientDetailsVO>>() {
				});

		List<Long> clientIds = listOfClients.stream().map(c -> c.getId()).collect(Collectors.toList());

		if (status.equals(TicketStatus.ALL)) {
			ticketEntity = ticketRepository.findByClientIdIn(clientIds);
		} else {

			ticketEntity = ticketRepository.findByStatusAndClientIdIn(status, clientIds);
			if (CollectionUtils.isEmpty(ticketEntity)) {
				throw new RecordNotFoundException("Records not found");
			}
		}
		List<Ticket> ticketsList = ticketMapper.convertEntityToVo(ticketEntity);
		return ticketsList;

	}

	/**
	 * @StringGenerationAPI string generation
	 */
	public static String getSaltString() {
		String SALTCHARS = "1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 3) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	@Override
	public boolean uploadFile(MultipartFile file) {
		boolean uploadedFile = false;
		try {
			if (file.isEmpty()) {
				throw new InvalidDataException("Request must be a file");
			}

			if (!((file.getContentType().equals("image/jpeg")) || (file.getContentType().equals("image/png")))) {
				throw new InvalidDataException("Only JPEG and PNG content are allowed");
			}

			uploadedFile = fileUploadUtils.uploadFile(file);
			if (!uploadedFile) {
				throw new InvalidDataException("File uploading failed");
			}
		} catch (Exception e) {
			throw new InvalidDataException("Invalid Request");

		}

		return uploadedFile;

	}

	@Override
	public String updateTicketStatus(Ticket ticket) {
		Optional<TicketEntity> ticketById = ticketRepository.findById(ticket.getId());
		TicketEntity update = null;

		if (ticketById.isPresent()) {

			TicketEntity ticketEntity = ticketById.get();
			ticketEntity.setStatus(ticket.getStatus());
			update = ticketRepository.save(ticketEntity);

		} else {
			throw new RecordNotFoundException("Records not found");
		}

		return AppConstants.UPDATE_TICKET_STATUS + update.getStatus();
	}

	@Override
	public List<ReportsVo> getTicketsCount() {
		List<ReportsVo> ticketsList = new ArrayList<>();

		List<TicketEntity> openTickets = ticketRepository.findByStatus(TicketStatus.OPEN);
		long count = openTickets.stream().mapToLong(t -> t.getId()).count();
		ReportsVo rvo1 = new ReportsVo();
		rvo1.setName("Open Tickets");
		rvo1.setOpenTickets(count);
		ticketsList.add(rvo1);

		List<TicketEntity> closedTickets = ticketRepository.findByStatus(TicketStatus.CLOSED);
		long count2 = closedTickets.stream().mapToLong(t -> t.getId()).count();
		ReportsVo rvo2 = new ReportsVo();
		rvo2.setName("Closed Tickets");
		rvo2.setClosedTickets(count2);
		ticketsList.add(rvo2);

		List<TicketEntity> totalTickets = ticketRepository.findAll();
		long count3 = totalTickets.stream().mapToLong(t -> t.getId()).count();
		ReportsVo rvo3 = new ReportsVo();
		rvo3.setName("Total Tickets");
		rvo3.setTotalTickets(count3);
		ticketsList.add(rvo3);

		return ticketsList;
	}

	@Override
	public List<Ticket> ticketSearching(Ticket ticket, Long clientId) {
		List<TicketEntity> ticketsList = new ArrayList<>();
		if (ticket.getTicketId() != null && ticket.getStatus() != null) {
			ticketsList = ticketRepository.findByStatusAndTicketIdAndClientId(ticket.getStatus(), ticket.getTicketId(),
					clientId);
		}
		if (CollectionUtils.isEmpty(ticketsList)) {
			throw new RecordNotFoundException("Records not found");
		}
		List<Ticket> result = ticketMapper.convertEntityToVo(ticketsList);
		return result;
	}

}
