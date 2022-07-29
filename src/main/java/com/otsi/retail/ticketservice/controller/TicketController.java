
package com.otsi.retail.ticketservice.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

	private Logger log = LogManager.getLogger(TicketController.class);

	@Autowired
	private TicketService ticketService;

	@PostMapping(CommonRequestMappings.SAVE_TICKET)
	public GateWayResponse<?> saveNewTicket(@RequestBody Ticket ticket) {
		log.info("Recieved request to saveNewTicket():" + ticket);
		boolean saveTicket = ticketService.saveTicket(ticket);
		return new GateWayResponse<>(AppConstants.SAVE_TICKET, saveTicket);

	}

	@GetMapping(CommonRequestMappings.GET_TICKETS_BY_STATUS)
	public GateWayResponse<?> getTickets(@RequestParam("status") TicketStatus status) {

		log.info("Recieved request to getTicketsByStatus():");
		List<Ticket> tickets = ticketService.getTicketsByStatus(status);

		return new GateWayResponse<>(AppConstants.GET_TICKETS, tickets);

	}

	@PostMapping(CommonRequestMappings.UPLOAD_FILE)
	public GateWayResponse<?> uploadFile(@RequestParam("file") MultipartFile file) {

		log.info("Recieved request to uploadFile():");
		boolean uploadFile = ticketService.uploadFile(file);

		return new GateWayResponse<>(AppConstants.UPLOAD_FILE, uploadFile);
	}

}
