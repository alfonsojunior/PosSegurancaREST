package br.edu.unidavi.alfonso.possegurancarest.entity;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso({Chave.class})
public class ChaveResource extends Resource<Chave> {

    public ChaveResource() {
        this(new Chave());
    }

    public ChaveResource(Chave chave, Link... links) {
        super(chave, links);
    }

}
