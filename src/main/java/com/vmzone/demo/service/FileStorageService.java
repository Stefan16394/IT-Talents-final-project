package com.vmzone.demo.service;

import org.springframework.stereotype.Service;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.vmzone.demo.exceptions.FileStorageException;
import com.vmzone.demo.exceptions.MyFileNotFoundException;
import com.vmzone.demo.models.FileStorageProperties;
import com.vmzone.demo.models.Photo;
import com.vmzone.demo.repository.PhotoRepository;
import com.vmzone.demo.repository.ProductRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Service layer communicating with product repository and photo repository for managing uploading and downloading a file
 * 
 * @author Stefan Rangelov and Sabiha Djurina
 *
 */

@Service
public class FileStorageService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private PhotoRepository photoRepository;
	
	private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    /**
     * store  file in uploads folder and file name in db
     * 
     * @param file - added MultipartFile 
     * @param id - id of product object stored in db
     * @return String - path of the product
     */
    
    public String storeFile(MultipartFile file, Long id) {
    	UUID uuid = UUID.randomUUID();
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println("filename: " + fileName);
        String fileNameWithoutExtension = FilenameUtils.getBaseName(fileName);
        String fileExtension = FilenameUtils.getExtension(fileName);
        String path = fileNameWithoutExtension + uuid.toString() + "." +fileExtension;
        
        
        System.out.println("path" + path);
        

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(path);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            Photo photo = new Photo(this.productRepository.findById(id).get(), path);
            
            this.photoRepository.save(photo);

            return path;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) throws MalformedURLException {
       
    	try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
        
    }

}
