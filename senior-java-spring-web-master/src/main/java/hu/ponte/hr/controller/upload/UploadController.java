package hu.ponte.hr.controller.upload;


import hu.ponte.hr.services.FileUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;

@RestController
@Component
@RequestMapping("api/file")
public class UploadController
{

    private FileUploader fileUploader;

    @Autowired
    public UploadController(FileUploader fileUploader) {
        this.fileUploader = fileUploader;
    }

    @PostMapping("/post")
    public ResponseEntity<Long> uploadFile(@RequestParam("file") @NotNull @NotBlank CommonsMultipartFile file) throws Exception {

        return new ResponseEntity<>(fileUploader.processFile(file, null), HttpStatus.OK);
    }
}
