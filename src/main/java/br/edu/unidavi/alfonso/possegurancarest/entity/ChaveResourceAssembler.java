package br.edu.unidavi.alfonso.possegurancarest.entity;

import br.edu.unidavi.alfonso.possegurancarest.controller.SecurityRestController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class ChaveResourceAssembler extends ResourceAssemblerSupport<Chave, ChaveResource> {

    public ChaveResourceAssembler() {
        super(Chave.class, ChaveResource.class);
    }

    @Override
    public ChaveResource toResource(Chave chave) {
        return new ChaveResource(chave,
                linkTo(methodOn(SecurityRestController.class).getChave(chave.getId())).withSelfRel());
    }

    @Override
    protected ChaveResource instantiateResource(Chave chave) {
        return new ChaveResource(chave);
    }

}
