package hu.ponte.hr.services;

import hu.ponte.hr.domain.FileRegistry;
import hu.ponte.hr.domain.ImageMeta;
import hu.ponte.hr.repository.UploadRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ImageStore {
    private final UploadRepository uploadRepository;
    private final SignService signService;

    public List<ImageMeta> listAllImages() {
        return uploadRepository.findAll().stream().map(this::mapToImageMeta).collect(Collectors.toList());
    }

    public ImageMeta mapToImageMeta(FileRegistry fileRegistry) {
        ImageMeta imageMeta = new ImageMeta();
        imageMeta.setId(fileRegistry.getId().toString());
        imageMeta.setSize(fileRegistry.getFileSize());
        imageMeta.setName(fileRegistry.getOriginalFileName());
        imageMeta.setMimeType(fileRegistry.getMediaType());
        imageMeta.setDigitalSign(fileRegistry.getDigitalSign());

        return imageMeta;
    }

    public String getImagePathById(Long id) {
        return uploadRepository.findById(id).orElseThrow(EntityNotFoundException::new).getFilePath();
    }
}
