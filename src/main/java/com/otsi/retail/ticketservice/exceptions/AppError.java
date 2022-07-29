package com.otsi.retail.ticketservice.exceptions;

import lombok.Data;

@Data
public class AppError {

	private String errorCode;
	private String errorMsg;
}