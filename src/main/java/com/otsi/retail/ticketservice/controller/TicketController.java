
package com.otsi.retail.ticketservice.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.otsi.retail.ticketservice.bindings.ReportsVo;
import com.otsi.retail.ticketservice.bindings.Ticket;
import com.otsi.retail.ticketservice.common.TicketStatus;
import com.otsi.retail.ticketservice.constants.AppConstants;
import com.otsi.retail.ticketservice.constants.CommonRequestMappings;
import com.otsi.retail.ticketservice.gatewayresponse.GateWayResponse;
import com.otsi.retail.ticketservice.service.TicketService;

/**
 * @author Sudheer.Swamy
 *
 */
@RestController
@RequestMapping(CommonRequestMappings.TICKET)
public class TicketController {

	@Autowired
	private TicketService ticketService;

	@PostMapping(CommonRequestMappings.SAVE_TICKET)
	public GateWayResponse<?> saveNewTicket(@RequestBody Ticket ticket, @RequestHeader(value="clientId") Long clientId) {
		boolean saveTicket = ticketService.saveTicket(ticket,clientId);
		return new GateWayResponse<>(AppConstants.SAVE_TICKET, saveTicket);

	}

	@GetMapping(CommonRequestMappings.GET_TICKETS_BY_STATUS)
	public GateWayResponse<?> getTickets(@RequestParam("status") TicketStatus status, @RequestHeader(value = "userId") Long userId) throws URISyntaxException {

		List<Ticket> tickets = ticketService.getTicketsByStatus(status, userId);

		return new GateWayResponse<>(AppConstants.GET_TICKETS, tickets);

	}

	@PostMapping(CommonRequestMappings.UPLOAD_FILE)
	public GateWayResponse<?> uploadFile(@RequestParam("file") MultipartFile file) {

		boolean uploadFile = ticketService.uploadFile(file);

		return new GateWayResponse<>(AppConstants.UPLOAD_FILE, uploadFile);
	}
	
	@GetMapping("/getFile/{fileName}")
	public ResponseEntity<?> getTickets(@PathVariable String fileName) throws URISyntaxException, IOException {

		 byte[] downloadImageFromFileSystem = ticketService.downloadImageFromFileSystem(fileName);

		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.valueOf("image/png"))
				.body(downloadImageFromFileSystem);

	}

	@PutMapping(CommonRequestMappings.UPDATE_TICKET_STATUS)
	public GateWayResponse<?> updateTicketStatus(@RequestBody Ticket ticket) {
		String updateTicketStatus = ticketService.updateTicketStatus(ticket);
		return new GateWayResponse<>(AppConstants.UPDATE_TICKET_STATUS, updateTicketStatus);

	}

	@PostMapping(CommonRequestMappings.TICKETS_SEARCHING)
	public GateWayResponse<?> searchTickets(@RequestBody Ticket ticket, @RequestHeader(value = "userId") Long userId) throws URISyntaxException {
		List<Ticket> tickets = ticketService.ticketSearching(ticket,userId);
		return new GateWayResponse<>(AppConstants.GET_TICKETS, tickets);

	}

	@GetMapping(CommonRequestMappings.TICKETS_COUNT)
	public GateWayResponse<?> getTicketsCount() {
		List<ReportsVo> ticketsCount = ticketService.getTicketsCount();
		return new GateWayResponse<>(AppConstants.GET_TICKETS, ticketsCount);

	}

}
