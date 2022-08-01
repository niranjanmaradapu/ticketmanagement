/**
 * 
 */
package com.otsi.retail.ticketservice.bindings;

import lombok.Data;

/**
 * @author Sudheer.Swamy
 *
 */
@Data
public class ReportsVo {

	private String name;
	private Long totalTickets;
	private Long openTickets;
	private Long closedTickets;

}
