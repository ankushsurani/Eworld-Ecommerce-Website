package com.eworld.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class FileImageUpload {
	
	public static String uploadImage(String path, MultipartFile file) {
		
		//file name
		String name = file.getOriginalFilename();
		
		String randomId = UUID.randomUUID().toString();
		String fileName1 = randomId.concat(name.substring(name.lastIndexOf(".")));
		
		//full path
		String filePath= path + File.separator + fileName1;
		
		
		//create folder if not created
		File f = new File(path);
		if(!f.exists()) {
			f.mkdir();
		}
		
		
		//file copy
		try {
			Files.copy(file.getInputStream(), Paths.get(filePath));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return fileName1;
	}
	
	
	
	public static InputStream getResource(String path, String fileName) throws FileNotFoundException {
		
		String fullPath = path + File.separator + fileName;
		
		InputStream is = new FileInputStream(fullPath);
		
		//db logic to return inputstream
		
		return is;
	}

}
