/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maquinadebusca.app.repository;

import com.maquinadebusca.app.model.Termo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author fernando
 */
public interface ITermoRepository extends JpaRepository<Termo, Long> {

    @Override
    List<Termo> findAll();

    @Override
    Termo save(Termo termo);

    Termo findById(long id);
}
