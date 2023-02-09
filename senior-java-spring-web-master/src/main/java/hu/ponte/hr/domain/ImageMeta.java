package hu.ponte.hr.domain;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author zoltan
 */
@Data
@NoArgsConstructor
public class ImageMeta {
    private String id;
    private String name;
    private String mimeType;
    private long size;
    private String digitalSign;

}
