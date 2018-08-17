package br.edu.unidavi.alfonso.possegurancarest.controller;

import br.edu.unidavi.alfonso.possegurancarest.entity.Chave;
import br.edu.unidavi.alfonso.possegurancarest.entity.ChaveResource;
import br.edu.unidavi.alfonso.possegurancarest.entity.ChaveResourceAssembler;
import br.edu.unidavi.alfonso.possegurancarest.util.ChavesRSA;

import br.edu.unidavi.alfonso.possegurancarest.util.Criptografia;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

@RestController
@RequestMapping(value = "/chaves")
@Api(value = "/chaves", description = "Web Service para Troca de Chaves")
public class SecurityRestController {

    private List<Chave> chaves = new ArrayList<>();

    //private Cipher cipher;
    private String fileBase = "serverKey";
    private File keyFile = new File(fileBase + ".key");
    private File pubFile = new File(fileBase + ".pub");
    private ChavesRSA chavesRSA = null;

    private Criptografia cripto = null;

    private ChaveResourceAssembler assembler = new ChaveResourceAssembler();

    public SecurityRestController() {
        chavesRSA = new ChavesRSA();
        cripto = new Criptografia();
    }

    @ApiOperation(httpMethod = "GET",
            value = "Retorna a chave com o ID informado",
            nickname = "/chave/{id}")
    @RequestMapping(value = "/chave/{id}", method = RequestMethod.GET)
    public ResponseEntity<ChaveResource> getChave(@ApiParam("ID da Chave") @PathVariable String id) {

        OptionalInt index = IntStream.range(0, chaves.size())
                .filter(i -> chaves.get(i).getId().equals(id)).findFirst();
        if (index.isPresent()) {
            Chave chave = chaves.get(index.getAsInt());
            return new ResponseEntity<>(assembler.toResource(chave), HttpStatus.OK);
        } else {
            //return new ResponseEntity<>(HttpStatus.valueOf("Chave nao encontrada!"));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @ApiOperation(httpMethod = "GET",
            value = "Retorna a chave pública do servidor",
            nickname = "/publickey")
    @RequestMapping(value = "/publickey", method = RequestMethod.GET)
    public ResponseEntity<ChaveResource> getPublicKey() {

        ResponseEntity<ChaveResource> retorno = null;
        Chave chave = new Chave();
        chavesRSA.geraChaves();
        PublicKey pub = null;
        if (keyFile.exists()) {
            try {
                pub = chavesRSA.getPublic();
                chave.setId("Publica");
                chave.setKey(Base64.getEncoder().encodeToString(pub.getEncoded()));

                OptionalInt index = IntStream.range(0, chaves.size())
                        .filter(i -> chaves.get(i).getId().equals(chave.getId())).findFirst();
                if (!index.isPresent()) {
                    chaves.add(chave);
                }
                retorno = new ResponseEntity<>(assembler.toResource(chave), HttpStatus.OK);
            } catch (IOException e) {
                retorno = new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                retorno = new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                retorno = new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
                e.printStackTrace();
            }
        }
        return retorno;

    }

    @PostMapping(value="/post")
    public ResponseEntity<ChaveResource> postAESKey(@RequestBody Chave chave) {
        System.out.println(chave);

        Chave chave1 = null;
        chave1 = chaves.stream().filter(x -> x.getId().equals(chave.getId())).findFirst().orElse(null);
        if (chave1 == null) {
            chaves.add(chave);
            return new ResponseEntity<>(assembler.toResource(chave), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(assembler.toResource(chave1), HttpStatus.OK);
        }
    }

    @GetMapping(value="/get/{id}")
    public ResponseEntity<ChaveResource> getAESKey(@PathVariable String id) {
        Chave chave1 = chaves.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
        if (chave1 != null) {
            chave1.setKey(cripto.decryptRSA(chave1.getKey()));
            return new ResponseEntity<>(assembler.toResource(chave1), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
