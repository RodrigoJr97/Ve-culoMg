package com.veiculosmg.service;

import java.util.List;
import java.util.Optional;


public interface CrudService<T> {

    List<T> listaEntidades();

    Optional<T> entidadePorId(Long id);

    T salvaNovaEntidade(T t);

    void deletaEntidade(Long id);

    void updateEntidade(T t, Long id);

}
