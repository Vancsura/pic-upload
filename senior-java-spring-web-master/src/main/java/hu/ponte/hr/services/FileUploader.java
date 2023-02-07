package hu.ponte.hr.services;

import hu.ponte.hr.domain.FileRegistry;
import hu.ponte.hr.dto.FileResource;
import hu.ponte.hr.repository.UploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;

public abstract class FileUploader {

    protected UploadRepository uploadRepository;
    protected ImageStore imageStore;

    @Autowired
    public FileUploader(UploadRepository uploadRepository, ImageStore imageStore) {
        this.uploadRepository = uploadRepository;
        this.imageStore = imageStore;
    }

    public Long processFile(CommonsMultipartFile commonsMultipartFile, String category) throws Exception {
        FileRegistry fileRegistry = storeFile(commonsMultipartFile, category);

        Long id = uploadRepository.save(fileRegistry).getId();

        String fileName = fileRegistry.getOriginalFileName();
        String sign = imageStore.makeSignature(fileName);
        fileRegistry.setDigitalSign(sign);
        uploadRepository.save(fileRegistry);

        return id;
    }

    protected abstract FileRegistry storeFile(CommonsMultipartFile commonsMultipartFile, String category) throws IOException;

    public abstract FileResource getFile(Long id);

    public FileRegistry findById(Long id) {
        return uploadRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No Upload found with given Id: " + id));
    }

    public List<FileRegistry> getFileRegistryList() {
        return uploadRepository.findAll();
    }
}
