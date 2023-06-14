package com.veiculosmg.service.interfaceService;

import java.util.List;
import java.util.Optional;


public interface CrudService<T> {

    T salvaNovaEntidade(T t);

    List<T> listaEntidades();

    Optional<T> entidadePorId(Long id);

    void updateEntidade(T t, Long id);

    void deletaEntidade(Long id);

}
