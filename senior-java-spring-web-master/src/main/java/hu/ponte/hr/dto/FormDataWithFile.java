package hu.ponte.hr.dto;

import lombok.Data;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Data
public class FormDataWithFile {

    private CommonsMultipartFile file;
    private String title;
    private String category;


    @Override
    public String toString() {
        return "FormDataWithFile{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", fileSize='" + file.getSize() + '\'' +
                '}';
    }
}
