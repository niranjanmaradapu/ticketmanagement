package com.otsi.retail.ticketservice.bindings;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.otsi.retail.ticketservice.common.PlanDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDetailsVO {

	private Long id;

	private String name;

	private String organizationName;

	private String address;

	private LocalDate createdDate;

	private LocalDate lastModifiedDate;

	private String mobile;

	private String email;

	private boolean isActive;

	private Long createdBy;

	private Boolean isEsSlipEnabled;

	private Boolean isTaxIncluded;

	private String planName;

	private Long planId;

	private String description;

	private String planTenure;

	private String rayzorPayPaymentId;

	private Long amount;

	@JsonIgnore
	private PlanDetails plandetails;

	private LocalDateTime planExpiryDate;

	private LocalDateTime planActivatedDate;
	
	private Boolean planExpired;

	private LocalDateTime systemTime;

}
