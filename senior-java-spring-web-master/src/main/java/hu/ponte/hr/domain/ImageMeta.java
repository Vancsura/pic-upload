package hu.ponte.hr.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author zoltan
 */
@Data
@NoArgsConstructor
public class ImageMeta
{
	private Long id;
	private String name;
	private String mimeType;
	private long size;
	private String digitalSign;

}
