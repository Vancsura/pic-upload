package hu.ponte.hr.services.serverstorage;

import hu.ponte.hr.domain.FileRegistry;
import hu.ponte.hr.dto.FileResource;
import hu.ponte.hr.repository.UploadRepository;
import hu.ponte.hr.services.FileUploader;
import hu.ponte.hr.services.ImageStore;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ServerStorageFileUploaderImp extends FileUploader {

    private UploadRepository uploadRepository;
    private HttpServletRequest request;

    @Autowired
    public ServerStorageFileUploaderImp(UploadRepository uploadRepository, ImageStore imageStore, HttpServletRequest request) {
        super(uploadRepository, imageStore);
        this.request = request;
    }

    @Override
    protected FileRegistry storeFile(CommonsMultipartFile commonsMultipartFile, String category) throws IOException {
        String uploadsDir = "/uploads/";
        String realPathToUploads = request.getServletContext().getRealPath(uploadsDir);
        if (!new File(realPathToUploads).exists()) {
            new File(realPathToUploads).mkdir();
        }
        String originalFilename = commonsMultipartFile.getOriginalFilename();
        String fullFilePath = realPathToUploads + originalFilename;
        File destination = new File(fullFilePath);
        commonsMultipartFile.transferTo(destination);

        FileRegistry newUpload = new FileRegistry(fullFilePath,
                commonsMultipartFile.getSize(),
                commonsMultipartFile.getContentType(),
                originalFilename);
        return newUpload;
    }

    @Override
    public FileResource getFile(Long id) {
        FileRegistry uploadById = findById(id);
        FileSystemResource fileResource = new FileSystemResource(uploadById.getFilePath());
        File file = fileResource.getFile();
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
            byte[] bytes = IOUtils.toByteArray(in);
            return new FileResource(bytes, uploadById.getMediaType(), uploadById.getOriginalFileName());
        } catch (IOException e) {
            throw new ServerStorageDownloadException();
        }
    }
}
