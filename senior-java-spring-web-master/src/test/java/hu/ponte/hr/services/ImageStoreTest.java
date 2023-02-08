package hu.ponte.hr.services;

import hu.ponte.hr.domain.FileRegistry;
import hu.ponte.hr.repository.UploadRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ImageStoreTest {

    @Autowired
    private UploadRepository uploadRepository;
    private FileRegistry fileRegistry = new FileRegistry();
    @Autowired
    private ImageStore imageStore;
    @Test
    void test_findPictureById() {

        String controlPath = "https://res.cloudinary.com/dt7earmc9/image/authenticated/s--pglVi7U7--/v1675779319/hard-work-always-pays-off_nypzq0.webp";

        String path = this.imageStore.getImagePathById(1L);
        assertEquals(controlPath, path);
    }

    @Test
    void test_imageMetaConversion() {
        fileRegistry.setId(1L);
        fileRegistry.setMediaType("image/webp");
        fileRegistry.setDigitalSign("U0gtCsTfiSxnnjt1MiYNFf9gvb9rws/uLkpvPnASuFCg/1xV6gTC9dxTj7sIox+cnBG2UVQhUnGCVAWyLlNIDxVEZxtm8VRe6hZ0Q603hzFwb6iEyXOvdfBYZ1RLOtCmqouhmvhbtRrHbi+8lMkvgsbCbLb1UmIDEnl5pGpEjQ8GHecWqUGrtrsuU+1E/UO9mK4LJ3i0iQ6p0nBVmn6lc9N1Vzp6iGnKjFYMbo+069BzufBIKn3mYyP/qzbeaTiyNj2oGh4lZMeuaN7+N3JVvkWgpdNJN1guSXCy8PDX+y/0f4CCpcAuOaZeSB4VspZO8S1cSJGSgmrr/t11jnHOuA==");
        fileRegistry.setOriginalFileName("TIGRISEK.jpg");
        fileRegistry.setFileSize(33646L);

        String pictureDetails = imageStore.mapToImageMeta(fileRegistry).toString();
        System.out.println(pictureDetails);
        assertEquals(pictureDetails, "ImageMeta(id=1, name=TIGRISEK.jpg, mimeType=image/webp, size=33646, digitalSign=U0gtCsTfiSxnnjt1MiYNFf9gvb9rws/uLkpvPnASuFCg/1xV6gTC9dxTj7sIox+cnBG2UVQhUnGCVAWyLlNIDxVEZxtm8VRe6hZ0Q603hzFwb6iEyXOvdfBYZ1RLOtCmqouhmvhbtRrHbi+8lMkvgsbCbLb1UmIDEnl5pGpEjQ8GHecWqUGrtrsuU+1E/UO9mK4LJ3i0iQ6p0nBVmn6lc9N1Vzp6iGnKjFYMbo+069BzufBIKn3mYyP/qzbeaTiyNj2oGh4lZMeuaN7+N3JVvkWgpdNJN1guSXCy8PDX+y/0f4CCpcAuOaZeSB4VspZO8S1cSJGSgmrr/t11jnHOuA==)");
    }

    @Test
    void test_listAllImages() {
//        List<FileRegistry>
    }

}