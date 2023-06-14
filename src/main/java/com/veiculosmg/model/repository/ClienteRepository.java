package com.veiculosmg.model.repository;

import com.veiculosmg.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByCpf(String cpf);

    boolean existsByNome(String nome);
    boolean existsByCpf(String cpf);

    boolean existsByNumeroTelefone(String numeroTelefone);

    boolean existsByEmail(String email);

}
