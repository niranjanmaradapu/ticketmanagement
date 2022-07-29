/**
 * 
 */
package com.otsi.retail.ticketservice.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.otsi.retail.ticketservice.common.IssueType;
import com.otsi.retail.ticketservice.common.Priority;
import com.otsi.retail.ticketservice.common.TicketStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Sudheer.Swamy
 *
 */
@Entity
@Table(name = "ticket")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String ticketId;
	@Enumerated(EnumType.STRING)
	private IssueType issueType;
	private String moduleType;
	private String contactNumber;
	private String emailId;
	@Enumerated(EnumType.STRING)
	private Priority priority;
	private String title;
	private String description;
	@Enumerated(EnumType.STRING)
	private TicketStatus status;
	private String assignee;
	private String closedBy;
	private Long clientId;
	private Long storeId;

}
