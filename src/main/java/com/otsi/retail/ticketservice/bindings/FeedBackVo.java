/**
 * 
 */
package com.otsi.retail.ticketservice.bindings;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.otsi.retail.ticketservice.common.WorkQuality;

import lombok.Data;

/**
 * @author Sudheer.Swamy
 *
 */
@Data
public class FeedBackVo {

	private Long id;
	private WorkQuality workQuality;
	private WorkQuality responseTime;
	private WorkQuality issueResolutionTime;
	private WorkQuality overallRating;

}
