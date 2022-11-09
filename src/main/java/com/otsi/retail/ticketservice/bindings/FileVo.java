package com.otsi.retail.ticketservice.bindings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileVo {
	
	private Long id;

	private String fileName;

	private String fileType;

	private String filePath;
	
	private Ticket tickets;

}
