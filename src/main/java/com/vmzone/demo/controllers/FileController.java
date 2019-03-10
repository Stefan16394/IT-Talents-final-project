package com.vmzone.demo.controllers;



import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vmzone.demo.dto.UploadFileResponse;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.User;
import com.vmzone.demo.service.FileStorageService;
import com.vmzone.demo.utils.SessionManager;

/**
 * Rest Controller for managing file requests
 * 
 * @author Sabiha Djurina and Stefan Rangelov
 * 
 *
 */

@RestController
public class FileController {
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;
    
    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id, HttpSession session ) throws ResourceDoesntExistException, BadCredentialsException {
    	
    	if (!SessionManager.isUserLoggedIn(session)) {
			throw new ResourceDoesntExistException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		if(!SessionManager.isAdmin(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED, "You do not have access to this feature!");
		}
    	
        String fileName = fileStorageService.storeFile(file, id);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("id") Long id, HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
       
    	if (!SessionManager.isUserLoggedIn(session)) {
			throw new ResourceDoesntExistException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		if(!SessionManager.isAdmin(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED, "You do not have access to this feature!");
		}
    	
    	return Arrays.asList(files)
                .stream()
                .map(file -> {
					try {
						return uploadFile(file, id, session);
					} catch (ResourceDoesntExistException | BadCredentialsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				})
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request, HttpSession session) throws MalformedURLException, ResourceDoesntExistException, BadCredentialsException {
    	if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		if(!((User) session.getAttribute("user")).isAdmin()) {
			throw new BadCredentialsException("You do not have access to this feature!");
		}
    	
    	// Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(( resource).getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
