package hu.ponte.hr.services;

import hu.ponte.hr.domain.FileRegistry;
import hu.ponte.hr.exception.GlobalExceptionHandler;
import hu.ponte.hr.repository.UploadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import java.io.IOException;

public abstract class FileUploader {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    protected UploadRepository uploadRepository;
    protected SignService signService;

    @Autowired
    public FileUploader(UploadRepository uploadRepository, SignService signService) {
        this.uploadRepository = uploadRepository;
        this.signService = signService;
    }

    public Long processFile(CommonsMultipartFile commonsMultipartFile, String category) throws Exception {
        FileRegistry fileRegistry = storeFile(commonsMultipartFile, category);

        Long id = uploadRepository.save(fileRegistry).getId();

        String fileName = fileRegistry.getOriginalFileName();
        String sign = signService.makeSignature(fileName);
        fileRegistry.setDigitalSign(sign);
        uploadRepository.save(fileRegistry);
        logger.info("Image save successful.");

        return id;
    }

    protected abstract FileRegistry storeFile(CommonsMultipartFile commonsMultipartFile, String category)
            throws IOException;
}
