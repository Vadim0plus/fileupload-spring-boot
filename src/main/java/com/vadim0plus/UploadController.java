package com.vadim0plus;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadController {

	private static String FILE_STORAGE_PATH = "";
	
	@GetMapping("/")
	public String getIndex() {
		return "upload";
	}
	
	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}
	
	@PostMapping("upload")
	public String fileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		
		if(file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}
		
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(FILE_STORAGE_PATH + file.getOriginalFilename());
			Files.write(path, bytes);
			
			redirectAttributes.addFlashAttribute("message", "You succesfully uploaded '"+
					file.getOriginalFilename() + "'");
			
			} catch (IOException e) { e.printStackTrace(); }
		
		return "redirect:/uploadStatus";
	}
	
} 
