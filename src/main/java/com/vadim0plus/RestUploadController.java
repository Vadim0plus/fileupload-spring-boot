package com.vadim0plus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RestUploadController {

	private static String FILE_STORAGE_PATH = "";
	
	private final Logger logger = LoggerFactory.getLogger(RestUploadController.class);
	
	@PostMapping("/api/upload")
	public ResponseEntity<?> fileUpload(@RequestParam("file") MultipartFile file) {
	
		logger.info("single file upload!");
		
		if(file.isEmpty()) {
			return new ResponseEntity("Please select a file!", HttpStatus.OK);
		}
		
		try {
		
				saveUploadedFiles(Arrays.asList(file));
				return new ResponseEntity("Successfully uploaded - " +
					file.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
					
			} catch (IOException e) { 
				e.printStackTrace();
				return new ResponseEntity("Error on Server Side", HttpStatus.OK); 
			}

	}
	
	/* Multiple upload with extra fields */
	@PostMapping("/api/upload/multi")
	public ResponseEntity<?> filesUpload(
			@RequestParam("extraField") String extraField,
			@RequestParam("files") MultipartFile[] files) {
		
		String uploadedFileNames = Arrays.stream(files).map(x -> x.getOriginalFilename())
				.filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
				
		logger.info("Multiple file upload:\n"+
			"extraField:"+extraField + "\n" +
			"number of files:" + files.length + "\n" +
			"Files:" + uploadedFileNames);
				
		if (StringUtils.isEmpty(uploadedFileNames)) {
			return new ResponseEntity("Please select a file!", HttpStatus.OK);
		}
		
		try {
		
			saveUploadedFiles(Arrays.asList(files));
			return new ResponseEntity("Successfully uploaded - " +
					uploadedFileNames, new HttpHeaders(), HttpStatus.OK);
					
			} catch (IOException e) { 
				e.printStackTrace();
				return new ResponseEntity("Error on Server Side", HttpStatus.OK); 
			}

	}
	
	private void saveUploadedFiles(List<MultipartFile> files) throws IOException {
	
		for(MultipartFile file : files) {
		
			if(file.isEmpty()) {
				continue;
			}
			
			byte[] bytes = file.getBytes();
			Path path = Paths.get(FILE_STORAGE_PATH + file.getOriginalFilename());
			Files.write(path, bytes);
			
		}
	}
} 
