package com.otsi.retail.ticketservice.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.otsi.retail.ticketservice.constants.AppConstants;
import com.otsi.retail.ticketservice.props.AppProperties;

@Component
public class FileUploadUtils {

	@Autowired
	private AppProperties appProps;

	public boolean uploadFile(MultipartFile multipartFile) {
		boolean f = false;

		try {

			Map<String, String> messages = appProps.getMessages();
			String UPLOAD_DIR = messages.get(AppConstants.FILES_UPLOAD_DIRECTORY);

			Files.copy(multipartFile.getInputStream(),
					Paths.get(UPLOAD_DIR + File.separator + multipartFile.getOriginalFilename()),
					StandardCopyOption.REPLACE_EXISTING);

			f = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return f;
	}

}
