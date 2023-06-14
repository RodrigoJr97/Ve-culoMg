package com.veiculosmg.model.repository;

import com.veiculosmg.model.entity.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarroRepository extends JpaRepository<Carro, Long> {

    Carro findByPlaca(String placa);
    boolean existsByPlaca(String placa);

}
