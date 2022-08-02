/**
 * 
 */
package com.otsi.retail.ticketservice.bindings;

import com.otsi.retail.ticketservice.common.FeedBack;

import lombok.Data;

/**
 * @author Sudheer.Swamy
 *
 */
@Data
public class FeedBackVo {

	private Long id;
	private FeedBack workQuality;
	private FeedBack responseTime;
	private FeedBack issueResolutionTime;
	private FeedBack overallRating;

}
