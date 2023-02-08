package hu.ponte.hr.services;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

@Service
@Transactional
public class SignService {

    private final Path privateFileDetails = Path.of("C:\\Users\\PC\\IdeaProjects\\pic-upload\\" +
                "senior-java-spring-web-master\\src\\main\\resources\\config\\keys\\key.private");

    private final Path publicFileDetails = Path.of("C:\\Users\\PC\\IdeaProjects\\pic-upload\\" +
                "senior-java-spring-web-master\\src\\main\\resources\\config\\keys\\key.pub");

    public String makeSignature(String fileName) throws Exception {

        byte[] keyPrivate = Files.readAllBytes(privateFileDetails);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyPrivate);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        byte[] keyPublic = Files.readAllBytes(publicFileDetails);
        KeyFactory kF = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyPublic);
        PublicKey publicKey = kF.generatePublic(spec);

        byte[] messageBytes = fileName.getBytes(StandardCharsets.UTF_8);

        //Signing

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(messageBytes);
        byte[] digitalSignature = signature.sign();

        Files.write(Paths.get("digital_signature_2"), digitalSignature);

        //Verifying the Signature
        signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(messageBytes);
        byte[] receivedSignature = Files.readAllBytes(Paths.get("digital_signature_2"));
        boolean isCorrect = signature.verify(receivedSignature);

        if (isCorrect) {
            System.out.println(Base64.getEncoder().encodeToString(digitalSignature));
            return Base64.getEncoder().encodeToString(digitalSignature);
        } else {
            return null;
        }
//        _________________________________________________________________________

//        Path fileDetails = Path.of("C:\\Users\\PC\\IdeaProjects\\pic-upload\\" +
//                "senior-java-spring-web-master\\src\\main\\resources\\config\\keys\\key.private");
//
//        byte[] strPk = Files.readAllBytes(fileDetails);
//
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(strPk);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//
//        Signature privateSignature = Signature.getInstance("SHA256withRSA");
//        privateSignature.initSign(kf.generatePrivate(spec));
//        privateSignature.update(fileName.getBytes(StandardCharsets.UTF_8));
//        byte[] s = privateSignature.sign();
//        return Base64.getEncoder().encodeToString(s);
    }
}
