package hu.ponte.hr.dto;

import lombok.Data;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Data
public class FormDataWithFile {

    private CommonsMultipartFile file;
    private String category;

    @Override
    public String toString() {
        return "FormDataWithFile{" +
                ", category='" + category + '\'' +
                ", fileSize='" + file.getSize() + '\'' +
                '}';
    }
}
