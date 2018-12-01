package com.maquinadebusca.app.service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Termo;
import com.maquinadebusca.app.repository.IDocumentoRepository;
import com.maquinadebusca.app.repository.ITermoRepository;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IndexadorService {

    private Hashtable hashTermos;

    @Autowired
    IDocumentoRepository dr;

    @Autowired
    ITermoRepository tr;

    public IndexadorService() {
        this.hashTermos = new Hashtable();
    }

    @Transactional
    public boolean criarIndice() {
        List<Documento> documentos = this.getDocumentos();
        for (Documento documento : documentos) {
            documento.setFrequenciaMaxima(0L);
            documento.setSomaQuadradosPesos(0L);
            documento = dr.save(documento);
            this.indexar(documento);
        }
        return true;
    }

    private void indexar(Documento documento) {

        String visaoDocumento = documento.getVisao();
        String[] termos = visaoDocumento.split(" ");
        for (int i = 0; i < termos.length; i++) {
            if (!termos[i].equals("")) {
                Termo termo = this.getTermo(termos[i]);
                int f = this.frequencia(termo, termos);
                if (f > documento.getFrequenciaMaxima()) {
                    documento.setFrequenciaMaxima(f);
                }
                termo.inserirEntradaIndiceInvertido(documento, f);
            }
        }
    }

    private Termo getTermo(String texto) {
        Termo termo;
        if (this.hashTermos.containsKey(texto)) {
            termo = (Termo) this.hashTermos.get(texto);
        } else {
            termo = new Termo();
            termo.setTexto(texto);
            termo.setN(0L);
            termo = tr.save(termo);
            this.hashTermos.put(texto, termo);
        }
        return termo;
    }

    private int frequencia(Termo termo, String[] termos) {
        int contador = 0;
        for (int i = 0; i < termos.length; i++) {
            if (termos[i] != "") {
                if (termos[i].equalsIgnoreCase(termo.getTexto())) {
                    contador++;
                    termos[i] = "";
                }
            }
        }
        return contador;
    }

    public List<Documento> getDocumentos() {
        Documento documento;
        List<Documento> documentos = new LinkedList<>();

        documento = new Documento();
        documento.setUrl("www.1.com.br");
        documento.setTexto("to do is to be to be is to do");
        documento.setVisao("to do is to be to be is to do");
        documentos.add(documento);

        documento = new Documento();
        documento.setUrl("www.2.com.br");
        documento.setTexto("to be or not to be i am what i am");
        documento.setVisao("to be or not to be i am what i am");
        documentos.add(documento);

        documento = new Documento();
        documento.setUrl("www.3.com.br");
        documento.setTexto("i think therefore i am do be do be do");
        documento.setVisao("i think therefore i am do be do be do");
        documentos.add(documento);
        documento = new Documento();
        documento.setUrl("www.4.com.br");
        documento.setTexto("do do do da da da let it be let it be");
        documento.setVisao("do do do da da da let it be let it be");
        documentos.add(documento);

        return documentos;
    }
}
