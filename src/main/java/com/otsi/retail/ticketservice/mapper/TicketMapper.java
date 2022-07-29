/**
 * 
 */
package com.otsi.retail.ticketservice.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.otsi.retail.ticketservice.bindings.Ticket;
import com.otsi.retail.ticketservice.entities.TicketEntity;

/**
 * @author Sudheer.Swamy
 *
 */
@Component
public class TicketMapper {

	public TicketEntity convertVoToEntity(Ticket ticket) {

		TicketEntity ticketEntity = new TicketEntity();
		ticketEntity.setId(ticket.getId());
		ticketEntity.setTicketId(ticket.getTicketId());
		ticketEntity.setIssueType(ticket.getIssueType());
		ticketEntity.setModuleType(ticket.getModuleType());
		ticketEntity.setContactNumber(ticket.getContactNumber());
		ticketEntity.setEmailId(ticket.getEmailId());
		ticketEntity.setPriority(ticket.getPriority());
		ticketEntity.setClosedBy(ticket.getClosedBy());
		ticketEntity.setStatus(ticket.getStatus());
		ticketEntity.setTitle(ticket.getTitle());
		ticketEntity.setDescription(ticket.getDescription());
		ticketEntity.setAssignee(ticket.getAssignee());
		ticketEntity.setClientId(ticket.getClientId());
		ticketEntity.setStoreId(ticket.getStoreId());

		return ticketEntity;

	}

	public Ticket convertEntityToVo(TicketEntity ticketEntity) {

		Ticket ticket = new Ticket();
		ticket.setId(ticketEntity.getId());
		ticket.setTicketId(ticketEntity.getTicketId());
		ticket.setIssueType(ticketEntity.getIssueType());
		ticket.setModuleType(ticketEntity.getModuleType());
		ticket.setContactNumber(ticketEntity.getContactNumber());
		ticket.setEmailId(ticketEntity.getEmailId());
		ticket.setPriority(ticketEntity.getPriority());
		ticket.setClosedBy(ticketEntity.getClosedBy());
		ticket.setStatus(ticketEntity.getStatus());
		ticket.setTitle(ticketEntity.getTitle());
		ticket.setDescription(ticketEntity.getDescription());
		ticket.setAssignee(ticketEntity.getAssignee());
		ticket.setClientId(ticketEntity.getClientId());
		ticket.setStoreId(ticketEntity.getStoreId());

		return ticket;

	}

	public List<Ticket> convertTicketEntityToVo(List<TicketEntity> tickets) {
		return tickets.stream().map(dto -> convertEntityToVo(dto)).collect(Collectors.toList());

	}

}
