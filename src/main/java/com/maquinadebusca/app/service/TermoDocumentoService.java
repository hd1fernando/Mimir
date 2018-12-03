package com.maquinadebusca.app.service;

import com.maquinadebusca.app.model.TermoDocumento;
import com.maquinadebusca.app.repository.ITermoDocumentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TermoDocumentoService {

  @Autowired
  ITermoDocumentoRepository tdr;

  public TermoDocumentoService () {
  }

  public TermoDocumento save (TermoDocumento termoDocumento) {
    return tdr.save (termoDocumento);
  }

  public double getIdf (String termoDocumento) {
    return tdr.getIdf (termoDocumento);
  }
  
}
