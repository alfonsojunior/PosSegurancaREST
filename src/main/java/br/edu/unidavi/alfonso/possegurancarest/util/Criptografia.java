package br.edu.unidavi.alfonso.possegurancarest.util;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class Criptografia {

    private Cipher cipherRSA;
    private Cipher cipherAES;
    private static final int IV_LENGTH=16;

    private ChavesRSA chavesRSA = null;

    public Criptografia() {
        try {
            this.cipherRSA = Cipher.getInstance("RSA");
            this.cipherAES = Cipher.getInstance("AES/CFB8/NoPadding");
            this.chavesRSA = new ChavesRSA();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public String decryptRSA(String base64Encrypted) {

        String retorno = "";
        try {
            cipherRSA.init(Cipher.DECRYPT_MODE, this.chavesRSA.getPrivate());
            byte[] bytesEnc  = Base64.getDecoder().decode(base64Encrypted);
            byte[] bytes = cipherRSA.doFinal(bytesEnc);
            retorno = Base64.getEncoder().encodeToString(bytes) ;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return retorno;
    }

    /*
    private void encryptAES(InputStream in, OutputStream out, String password) throws Exception{

        SecureRandom r = new SecureRandom();
        byte[] iv = new byte[IV_LENGTH];
        r.nextBytes(iv);
        out.write(iv);
        out.flush();

        SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipherAES.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        out = new CipherOutputStream(out, cipherAES);
        byte[] buf = new byte[1024];
        int numRead = 0;
        while ((numRead = in.read(buf)) >= 0) {
            out.write(buf, 0, numRead);
        }
        out.close();
    }
    */
}
