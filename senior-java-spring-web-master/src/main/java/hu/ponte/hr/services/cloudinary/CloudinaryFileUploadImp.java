package hu.ponte.hr.services.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.ponte.hr.domain.FileRegistry;
import hu.ponte.hr.repository.UploadRepository;
import hu.ponte.hr.services.FileUploader;
import hu.ponte.hr.services.SignService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@Transactional
public class CloudinaryFileUploadImp extends FileUploader {

    private final Cloudinary cloudinary;

    public CloudinaryFileUploadImp(UploadRepository uploadRepository, SignService signService, Cloudinary cloudinary) {
        super(uploadRepository, signService);
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
}
