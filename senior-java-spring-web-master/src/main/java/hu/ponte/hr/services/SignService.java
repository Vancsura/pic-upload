package hu.ponte.hr.services;

import hu.ponte.hr.exception.GlobalExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final Path privateFileDetails = Path.of("C:\\Users\\PC\\IdeaProjects\\pic-upload\\" +
                "senior-java-spring-web-master\\src\\main\\resources\\config\\keys\\key.private");
    private final Path publicFileDetails = Path.of("C:\\Users\\PC\\IdeaProjects\\pic-upload\\" +
                "senior-java-spring-web-master\\src\\main\\resources\\config\\keys\\key.pub");

    private PrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] privateKeyBytes = Files.readAllBytes(privateFileDetails);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        logger.info("Private key read and generated successfully.");

        return privateKey;
    }

    private PublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] publicKeyBytes = Files.readAllBytes(publicFileDetails);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);
        logger.info("Public key read and generated successfully.");

        return publicKey;
    }

    public String makeSignature(String fileName) throws Exception {

        byte[] messageBytes = fileName.getBytes();
        Path path = Paths.get("digital_signature");

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(getPrivateKey());
        signature.update(messageBytes);
        byte[] digitalSignature = signature.sign();

        generateFile(digitalSignature, path);

        if (verifySignature(messageBytes, path)) {
            return Base64.getEncoder().encodeToString(digitalSignature);
        } else {
            return null;
        }
    }

    private boolean verifySignature(byte[] bytes, Path path) throws SignatureException, NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidKeySpecException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(getPublicKey());
        signature.update(bytes);
        byte[] receivedSignature = Files.readAllBytes(path);
        return signature.verify(receivedSignature);
    }

    private void generateFile(byte[] digitalSignature, Path path) throws IOException {
        Files.write(path, digitalSignature);
    }
}
