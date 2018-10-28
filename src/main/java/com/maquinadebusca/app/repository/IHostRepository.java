/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maquinadebusca.app.repository;

import com.maquinadebusca.app.model.Host;
import com.maquinadebusca.app.model.Link;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author fernando
 */
public interface IHostRepository extends JpaRepository<Host, Long> {

    @Override
    List<Host> findAll();

    List<String> findByHost(String name);

    Host findById(long id);

    List<Host> findByHostIgnoreCaseContaining(String host);

}