package hu.ponte.hr.controller.upload;

import hu.ponte.hr.services.FileUploader;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
@Component
@RequestMapping("api/file")
public class UploadController
{

    private FileUploader fileUploader;

    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ResponseBody
    public String handleFormUpload(@RequestParam("file") MultipartFile file) {
        return "ok";
    }
}
