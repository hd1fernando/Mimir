package com.maquinadebusca.app.service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.repository.IDocumentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DocumentoService {

  @Autowired
  IDocumentoRepository dr;

  public DocumentoService () {
  }

  public Documento save (Documento documento) {
    return dr.save (documento);
  }

}
