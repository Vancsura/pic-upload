package hu.ponte.hr.services;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@Transactional
public class SignService
{
    private final Path privateFileDetails = Path.of("C:\\Users\\PC\\IdeaProjects\\pic-upload\\" +
                "senior-java-spring-web-master\\src\\main\\resources\\config\\keys\\key.private");
    private final Path publicFileDetails = Path.of("C:\\Users\\PC\\IdeaProjects\\pic-upload\\" +
                "senior-java-spring-web-master\\src\\main\\resources\\config\\keys\\key.pub");

    public PrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] privateKeyBytes = Files.readAllBytes(privateFileDetails);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(keySpec);
    }

    public PublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] publicKeyBytes = Files.readAllBytes(publicFileDetails);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(spec);
    }

    public String makeSignature(String fileName) throws Exception {

        byte[] messageBytes = fileName.getBytes();

        //Signature generating
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(getPrivateKey());
        signature.update(messageBytes);
        byte[] digitalSignature = signature.sign();

        Files.write(Paths.get("digital_signature"), digitalSignature);

        //Verifying the Signature
        signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(getPublicKey());
        signature.update(messageBytes);
        byte[] receivedSignature = Files.readAllBytes(Paths.get("digital_signature"));
        boolean isCorrect = signature.verify(receivedSignature);

        if (isCorrect) {
            return Base64.getEncoder().encodeToString(digitalSignature);
        } else {
            return null;
        }

    }
}
