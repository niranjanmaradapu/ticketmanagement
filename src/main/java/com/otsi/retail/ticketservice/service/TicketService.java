/**
 * 
 */
package com.otsi.retail.ticketservice.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.otsi.retail.ticketservice.bindings.ReportsVo;
import com.otsi.retail.ticketservice.bindings.Ticket;
import com.otsi.retail.ticketservice.common.TicketStatus;

/**
 * @author Sudheer.Swamy
 *
 */
@Component
public interface TicketService {

	boolean saveTicket(Ticket ticket);

	List<Ticket> getTicketsByStatus(TicketStatus status);

	boolean uploadFile(MultipartFile file);
	
	String updateTicketStatus(Ticket ticket);
	
	List<ReportsVo> getTicketsCount();
	
	List<Ticket> ticketsSearching(Ticket ticket);

}