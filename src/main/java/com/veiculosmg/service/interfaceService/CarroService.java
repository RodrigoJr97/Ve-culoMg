package com.veiculosmg.service.interfaceService;

import com.veiculosmg.model.entity.Carro;

import java.util.List;

public interface CarroService extends CrudService<Carro> {

    List<Carro> disponivel();
    List<Carro> listCategoria(String categoria);

}
