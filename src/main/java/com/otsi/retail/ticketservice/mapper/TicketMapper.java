/**
 * 
 */
package com.otsi.retail.ticketservice.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.ticketservice.bindings.CommentVo;
import com.otsi.retail.ticketservice.bindings.FeedBackVo;
import com.otsi.retail.ticketservice.bindings.Ticket;
import com.otsi.retail.ticketservice.entities.FeedBackEntity;
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

	public List<Ticket> convertEntityToVo(List<TicketEntity> tickets) {

		List<Ticket> ticketList = new ArrayList<>();
		tickets.stream().forEach(t -> {

			Ticket ticket = new Ticket();
			ticket.setId(t.getId());
			ticket.setTicketId(t.getTicketId());
			ticket.setIssueType(t.getIssueType());
			ticket.setModuleType(t.getModuleType());
			ticket.setContactNumber(t.getContactNumber());
			ticket.setEmailId(t.getEmailId());
			ticket.setPriority(t.getPriority());
			ticket.setClosedBy(t.getClosedBy());
			ticket.setStatus(t.getStatus());
			ticket.setTitle(t.getTitle());
			ticket.setDescription(t.getDescription());
			ticket.setAssignee(t.getAssignee());
			ticket.setClientId(t.getClientId());
			ticket.setStoreId(t.getStoreId());

			// getting comments
			List<CommentVo> commentList = new ArrayList<>();
			t.getComments().stream().forEach(c -> {
				CommentVo commentVo = new CommentVo();
				commentVo.setId(c.getId());
				commentVo.setComment(c.getComment());
				commentList.add(commentVo);
			});
			ticket.setCommentsVo(commentList);

			ticketList.add(ticket);
		});
		return ticketList;

	}

}
