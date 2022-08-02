/**
 * 
 */
package com.otsi.retail.ticketservice.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.otsi.retail.ticketservice.common.FeedBack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Sudheer.Swamy
 *
 */
@Entity
@Table(name = "feedback")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FeedBackEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private FeedBack workQuality;
	@Enumerated(EnumType.STRING)
	private FeedBack responseTime;
	@Enumerated(EnumType.STRING)
	private FeedBack issueResolutionTime;
	@Enumerated(EnumType.STRING)
	private FeedBack overallRating;
	
	@OneToOne(mappedBy = "feedBackEntity", cascade = CascadeType.ALL)
	private TicketEntity ticketEntity;

}
