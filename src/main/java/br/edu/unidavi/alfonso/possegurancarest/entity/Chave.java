package br.edu.unidavi.alfonso.possegurancarest.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
@XmlRootElement
public class Chave implements Serializable {

    private String id;
    private String key;

    public Chave() {
        super();
    }

}
