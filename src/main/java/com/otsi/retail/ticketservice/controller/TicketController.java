
package com.otsi.retail.ticketservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public GateWayResponse<?> saveNewTicket(@RequestBody Ticket ticket) {
		boolean saveTicket = ticketService.saveTicket(ticket);
		return new GateWayResponse<>(AppConstants.SAVE_TICKET, saveTicket);

	}

	@GetMapping(CommonRequestMappings.GET_TICKETS_BY_STATUS)
	public GateWayResponse<?> getTickets(@RequestParam("status") TicketStatus status) {

		List<Ticket> tickets = ticketService.getTicketsByStatus(status);

		return new GateWayResponse<>(AppConstants.GET_TICKETS, tickets);

	}

	@PostMapping(CommonRequestMappings.UPLOAD_FILE)
	public GateWayResponse<?> uploadFile(@RequestParam("file") MultipartFile file) {

		boolean uploadFile = ticketService.uploadFile(file);

		return new GateWayResponse<>(AppConstants.UPLOAD_FILE, uploadFile);
	}

	@PutMapping(CommonRequestMappings.UPDATE_TICKET_STATUS)
	public GateWayResponse<?> updateTicketStatus(@RequestBody Ticket ticket) {
		String updateTicketStatus = ticketService.updateTicketStatus(ticket);
		return new GateWayResponse<>(AppConstants.UPDATE_TICKET_STATUS, updateTicketStatus);

	}

	@PostMapping(CommonRequestMappings.TICKETS_SEARCHING)
	public GateWayResponse<?> searchTickets(@RequestBody Ticket ticket) {
		List<Ticket> tickets = ticketService.ticketSearching(ticket);
		return new GateWayResponse<>(AppConstants.GET_TICKETS, tickets);

	}

	@GetMapping(CommonRequestMappings.TICKETS_COUNT)
	public GateWayResponse<?> getTicketsCount() {
		List<ReportsVo> ticketsCount = ticketService.getTicketsCount();
		return new GateWayResponse<>(AppConstants.GET_TICKETS, ticketsCount);

	}

}
