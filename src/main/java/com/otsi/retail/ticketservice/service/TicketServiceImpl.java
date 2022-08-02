/**
 * 
 */
package com.otsi.retail.ticketservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.otsi.retail.ticketservice.bindings.ReportsVo;
import com.otsi.retail.ticketservice.bindings.Ticket;
import com.otsi.retail.ticketservice.common.TicketStatus;
import com.otsi.retail.ticketservice.constants.AppConstants;
import com.otsi.retail.ticketservice.entities.TicketEntity;
import com.otsi.retail.ticketservice.exceptions.InvalidDataException;
import com.otsi.retail.ticketservice.exceptions.RecordNotFoundException;
import com.otsi.retail.ticketservice.exceptions.RegAppException;
import com.otsi.retail.ticketservice.mapper.TicketMapper;
import com.otsi.retail.ticketservice.props.AppProperties;
import com.otsi.retail.ticketservice.repository.TicketRepository;
import com.otsi.retail.ticketservice.utils.EmailUtils;
import com.otsi.retail.ticketservice.utils.FileUploadUtils;

/**
 * @author Sudheer.Swamy
 *
 */
@Service
public class TicketServiceImpl implements TicketService {

	private Logger log = LogManager.getLogger(TicketServiceImpl.class);

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

	/**
	 * @SavTicket ticket creation API
	 */
	@Override
	public boolean saveTicket(Ticket ticket) {

		log.debug("debuggging saveTicket():" + ticket);
		ticket.setTicketId(
				"TK" + LocalDate.now().getYear() + LocalDate.now().getDayOfMonth() + LocalDate.now() + getSaltString());
		ticket.setStatus(TicketStatus.OPEN);

		TicketEntity ticketEntity = ticketMapper.convertVoToEntity(ticket);

		TicketEntity save = ticketRepository.save(ticketEntity);
		log.warn("we are checking if ticket is saved..");
		log.info("Ticket Saved Successfully!!");

		if (null != save.getTicketId()) {
			return sendEmail(ticket);
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

		log.debug("debuggging sendEmail():" + ticket);
		boolean isMailSent = false;
		try {
			Map<String, String> messages = appProps.getMessages();
			String subject = messages.get(AppConstants.TICKET_MAIL_SUBJECT);
			String bodyFileName = messages.get(AppConstants.TICKET_MAIL_BODY_TEMPLATE_FILE);
			String body = readMailBody(bodyFileName, ticket);
			emaiUtils.sendEmail(subject, body, ticket.getEmailId());
			isMailSent = true;

		} catch (Exception e) {
			log.error("REGAPP101 Error");
			throw new RegAppException(e.getMessage());
		}
		log.warn("we are checking if mail is send..");
		log.info("Mail Sent Successfully!!");
		return isMailSent;

	}

	/**
	 * This is a private method
	 * @ReadMailBody to read mail body file API
	 */
	private String readMailBody(String fileName, Ticket ticket){

		log.debug("debuggging readMailBody():" + fileName + " " + ticket);
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
		log.warn("we are checking if mail body is reading..");
		log.info("Mail Body Readed Successfully!!");
		return mailBody;
	}

	/**
	 * @getTicketsByStatusAPI get the tickets by status api
	 *
	 */
	@Override
	public List<Ticket> getTicketsByStatus(TicketStatus status) {

		log.debug("debuggging getTicketsByStatus():" + status);
		List<TicketEntity> ticketEntity = new ArrayList<>();

		if (status.equals(TicketStatus.ALL)) {
			ticketEntity = ticketRepository.findAll();
		} else {

			ticketEntity = ticketRepository.findAllByStatus(status);
			if (CollectionUtils.isEmpty(ticketEntity)) {
				log.error("records not found");
				throw new RecordNotFoundException("Records not found");
			}
		}
		List<Ticket> ticketsList = ticketMapper.convertTicketEntityToVo(ticketEntity);
		log.warn("we are checking tickets are fetching..");
		log.info("fetching list of tickets");
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
		log.debug("debuggging uploadFile():" + file);
		boolean uploadedFile = false;
		try {
			if (file.isEmpty()) {
				log.error("Request must be a file");
				throw new InvalidDataException("Request must be a file");
			}

			if (!((file.getContentType().equals("image/jpeg")) || (file.getContentType().equals("image/png")))) {
				log.error("Only JPEG and PNG content are allowed");
				throw new InvalidDataException("Only JPEG and PNG content are allowed");
			}

			uploadedFile = fileUploadUtils.uploadFile(file);
			log.warn("we are checking files are uploading..");
			log.info("Files Uploaded Successfully!!");
			if (!uploadedFile) {
				throw new InvalidDataException("File uploading failed");
			}
		} catch (Exception e) {
			log.error("Invalid Request");
			throw new InvalidDataException("Invalid Request");

		}

		return uploadedFile;

	}

	@Override
	public String updateTicketStatus(Ticket ticket) {
		log.debug("debuggging updateTicketStatus():" + ticket);
		Optional<TicketEntity> ticketById = ticketRepository.findById(ticket.getId());
		TicketEntity update = null;

		if (ticketById.isPresent()) {

			TicketEntity ticketEntity = ticketById.get();
			ticketEntity.setStatus(ticket.getStatus());
			update = ticketRepository.save(ticketEntity);
			log.warn("we are checking ticket status updated..");
			log.info("Ticket Status Successfully!!");

		} else {

			log.error("Records not found");
			throw new RecordNotFoundException("Records not found");
		}

		return AppConstants.UPDATE_TICKET_STATUS + update.getStatus();
	}

	@Override
	public List<ReportsVo> getTicketsCount() {
		log.debug("debuggging getTicketsCount():");
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

		log.warn("we are checking ticket are getting..");
		log.info("Tickets Getting Successfully!!");
		return ticketsList;
	}

	@Override
	public List<Ticket> ticketsSearching(Ticket ticket) {
		log.debug("debuggging ticketsSearching():");
		List<TicketEntity> ticketsList = new ArrayList<>();
		if (ticket.getTicketId() != null && ticket.getStatus() != null) {
			ticketsList = ticketRepository.findByStatusAndTicketId(ticket.getStatus(), ticket.getTicketId());
		}
		if (CollectionUtils.isEmpty(ticketsList)) {
			throw new RecordNotFoundException("Records not found");
		}
		List<Ticket> result = ticketMapper.convertTicketEntityToVo(ticketsList);
		log.warn("we are checking ticket are getting..");
		log.info("Tickets Getting Successfully!!");
		return result;
	}

}
