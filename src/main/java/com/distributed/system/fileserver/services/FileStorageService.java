package com.distributed.system.fileserver.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.distributed.system.fileserver.configuration.CustomProperties;

import jakarta.annotation.PostConstruct;

@Service
public class FileStorageService {
	Logger logger = org.slf4j.LoggerFactory.getLogger(FileStorageService.class);

	private Path rootLocation;
	@Autowired
	private CustomProperties customProperties;

	@PostConstruct
	public void init() {
		rootLocation = Paths.get(customProperties.getBaseDirectoryPath());
		logger.info("base directory path: {}", rootLocation);
	}

	public void store(MultipartFile file) {
		try {
			Files.copy(file.getInputStream(), rootLocation.resolve(file.getOriginalFilename()));
		} catch (IOException e) {
			logger.error("Failed to store file {} - {}", file.getOriginalFilename(), e);
		}
	}

	public Resource loadFile(String filename) {
		try {
			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
		} catch (MalformedURLException e) {
			logger.error("Could not read file: {} - {}", filename, e);
		}
		return null;
	}

	public void deleteFile(String filename) {
		try {
			Files.deleteIfExists(rootLocation.resolve(filename));
		} catch (IOException e) {
			logger.error("Failed to delete file {} - {}", filename, e);
		}
	}

	public List<String> getAllFiles() {
		List<String> allFiles = new ArrayList<String>();
		try {
			File rootLocationFile = rootLocation.getFileName().toFile();
			File[] files = rootLocationFile.listFiles();
			if (files != null) {
				for (File file : files) {
					allFiles.add(file.getName());
				}
			}
		} catch (Exception e) {
			logger.error("Failed to list files {}", e);
		}
		return allFiles;
	}
}
