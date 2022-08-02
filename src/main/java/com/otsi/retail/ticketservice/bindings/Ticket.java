/**
 * 
 */
package com.otsi.retail.ticketservice.bindings;

import java.util.List;

import com.otsi.retail.ticketservice.common.IssueType;
import com.otsi.retail.ticketservice.common.Priority;
import com.otsi.retail.ticketservice.common.TicketStatus;
import com.otsi.retail.ticketservice.entities.CommentEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Sudheer.Swamy
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Ticket {

	private Long id;
	private String ticketId;
	private IssueType issueType;
	private String moduleType;
	private String contactNumber;
	private String emailId;
	private Priority priority;
	private String title;
	private String description;
	private TicketStatus status;
	private String assignee;
	private String closedBy;
	private Long clientId;
	private Long storeId;

	private FeedBackVo feedBackVo;
	private List<CommentVo> commentsVo;

}
