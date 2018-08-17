package br.edu.unidavi.alfonso.possegurancarest.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ChavesRSA {

    private String fileBase = "serverKey";
    private File keyFile = new File(fileBase + ".key");
    private File pubFile = new File(fileBase + ".pub");

    public void geraChaves() {
        if (!keyFile.exists()) {
            KeyPairGenerator kpg = null;
            KeyPair kp = null;
            try {
                kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(2048);
                kp = kpg.generateKeyPair();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            if (kp != null) {
                try (FileOutputStream out = new FileOutputStream(fileBase + ".key")) {
                    out.write(kp.getPrivate().getEncoded());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try (FileOutputStream out = new FileOutputStream(fileBase + ".pub")) {
                    out.write(kp.getPublic().getEncoded());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public PublicKey getPublic() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = Files.readAllBytes(Paths.get(pubFile.getAbsolutePath()));
        X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pub = kf.generatePublic(ks);
        return pub;
    }

    public PrivateKey getPrivate() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = Files.readAllBytes(Paths.get(keyFile.getAbsolutePath()));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey pub = kf.generatePrivate(spec);
        return pub;
    }

}
