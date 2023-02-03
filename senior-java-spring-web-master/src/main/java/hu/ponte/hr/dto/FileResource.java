package hu.ponte.hr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileResource {
    private byte[] data;
    private String mediaType;
    private String originalFileName;
}
