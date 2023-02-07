package hu.ponte.hr.services.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.ponte.hr.domain.FileRegistry;
import hu.ponte.hr.dto.FileResource;
import hu.ponte.hr.repository.UploadRepository;
import hu.ponte.hr.services.FileUploader;
import hu.ponte.hr.services.ImageStore;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

@Service
@Transactional
public class CloudinaryFileUploadImp extends FileUploader {

    private Cloudinary cloudinary;

    public CloudinaryFileUploadImp(UploadRepository uploadRepository, ImageStore imageStore, Cloudinary cloudinary) {
        super(uploadRepository, imageStore);
        this.cloudinary = cloudinary;
    }

    @Override
    protected FileRegistry storeFile(CommonsMultipartFile commonsMultipartFile, String category) {
        Map params = ObjectUtils.asMap(
                "folder", category,
                "access_mode", "authenticated",
                "overwrite", false,
                "type", "authenticated",
                "resource_type", "auto",
                "use_filename", true
        );
        UploadResponse uploadResponse;
        File fileToUpload = new File(System.getProperty("java.io.tmpdir") + '/' + commonsMultipartFile.getOriginalFilename());
        try {
            commonsMultipartFile.transferTo(fileToUpload);
            uploadResponse = new ObjectMapper()
                    .convertValue(cloudinary.uploader().upload(fileToUpload, params), UploadResponse.class);
        } catch (IOException e) {
            System.out.println("The file size is too large!");
            throw new CloudinaryUploadException();
        }

        return new FileRegistry(uploadResponse, commonsMultipartFile);
    }

    @Override
    public FileResource getFile(Long id) {
        FileRegistry upload = findById(id);
        String url = upload.getFilePath();
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream())) {
            byte[] bytes = IOUtils.toByteArray(in);
            return new FileResource(bytes, upload.getMediaType(), upload.getOriginalFileName());
        } catch (IOException e) {
            throw new CloudinaryDownloadException();
        }
    }
}
