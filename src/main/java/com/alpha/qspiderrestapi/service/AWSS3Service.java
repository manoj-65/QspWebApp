package com.alpha.qspiderrestapi.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface AWSS3Service {
	String uploadFile(final MultipartFile multipartFile, String folder);

	String uploadFileToS3Bucket(final String bucketName, final File file, String floder);
}
