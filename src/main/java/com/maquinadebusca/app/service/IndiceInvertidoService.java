package com.maquinadebusca.app.service;

import com.maquinadebusca.app.model.IndiceInvertido;
import com.maquinadebusca.app.repository.IIndiceInvertidoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class IndiceInvertidoService {

  @Autowired
  IIndiceInvertidoRepository iir;

  public IndiceInvertidoService () {
  }

  public List<IndiceInvertido> getEntradasIndiceInvertido (String termo) {
    return iir.getEntradasIndiceInvertido (termo);
  }

}
