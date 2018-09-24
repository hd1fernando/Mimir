package com.maquinadebusca.app.service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Host;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.repository.IDocumentoRepository;
import com.maquinadebusca.app.repository.IHostRepository;
import com.maquinadebusca.app.repository.ILinkRepository;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ColetorService {

    @Autowired
    IDocumentoRepository dr;

    @Autowired
    ILinkRepository lr;

    @Autowired
    IHostRepository hr;

    public Set<String> exclusaoRobo = new HashSet<>();
    public Set<String> urlVisited = null;
    private static String lastVisitedHost = "";
    String urlSemente = "http://jovemnerd.com.br/";

    public List<Documento> executar() {
        List<Documento> documentos = new LinkedList();

        try {

            int cont = 0;
            int condicaoParada = 5;
            String url = urlSemente;

            while (cont < condicaoParada) {
                documentos.add(this.coletar(url.trim()));
                url = this.getLinkByPosition(cont);
                cont++;
            }
        } catch (Exception e) {
            System.out.println("Erro ao executar o serviço de coleta!");
            e.printStackTrace();
        }
        return documentos;
    }

    public Documento coletar(String urlDocumento) {
        Documento documento = new Documento();
        Link link;
        Host host;
        URL _url;
        try {
            link = new Link();
            host = new Host();
            Document d = Jsoup.connect(urlDocumento).get();
            Elements urls = d.select("a[href]");
            Elements lang = d.select("html");

            String idioma = lang.attr("lang");

            this.ProtocoloExclusao(urlDocumento);

            documento.setUrl(urlDocumento);
            documento.setTexto(d.html());
            documento.setVisao(FiltrarStopWords(d.text(), idioma));

            link.setUrl(urlDocumento);
            link.setUltimaColeta(LocalDateTime.now());
            documento.addLink(link);
            int i = 0;
            for (Element url : urls) {
                i++;
                String u = url.attr("abs:href");

                if ((!u.equals("")) && (u != null)) {
                    if (this.isLinkLoaded(u) || this.isDocumentoLoaded(u) || this.exclusaoRobo.contains(u)) {
                        continue;
                    }
                    link = new Link();
                    link.setUrl(u);
                    link.setUltimaColeta(LocalDateTime.now());
                    documento.addLink(link);
                }
            }
            _url = new URL(urlDocumento);
            if (!this.isHostLoaded(_url.getHost())) {
                host.setHost(_url.getHost());
                host.setContador(i);
                documento.addHost(host);
            }

//            System.out.println("Número de links coletados: " + host.getContador());
//            System.out.println("Tamanho da lista links: " + documento.getLinks().size());
            documento = dr.save(documento);

        } catch (Exception e) {
            System.out.println("Erro ao coletar a página.");
            e.printStackTrace();
        }
        this.exclusaoRobo.clear();
        documento = dr.save(documento);
        return documento;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters">
    public List<Host> getHost() {
        Iterable<Host> hosts = hr.findAll();
        List<Host> resposta = new LinkedList();
        for (Host documento : hosts) {
            resposta.add(documento);
        }
        return resposta;
    }

    public Host getHost(long id) {
        Host host = hr.findById(id);
        return host;
    }

    public List<Documento> getDocumento() {
        Iterable<Documento> documentos = dr.findAll();
        List<Documento> resposta = new LinkedList();
        for (Documento documento : documentos) {
            resposta.add(documento);
        }
        return resposta;
    }

    public Documento getDocumento(long id) {
        Documento documento = dr.findById(id);
        return documento;
    }

    public List<Link> getLink() {
        Iterable<Link> links = lr.findAll();
        List<Link> resposta = new LinkedList();
        for (Link link : links) {
            resposta.add(link);
        }
        return resposta;
    }

    public Link getLink(long id) {
        Link link = lr.findById(id);
        return link;
    }

    public String getLinkByPosition(int posicao) {
        try {
            String resultado = this.getLink().get(posicao).getUrl();
//            lr.deleteById(this.getLink().get(posicao).getId());
            System.out.println("PRIMEIRA URL COLETADA: " + resultado);
            return resultado;
        } catch (IndexOutOfBoundsException ex) {

        }
        return this.urlSemente;
    }

    private boolean isLinkLoaded(String nome) {
        List<String> resultado = lr.findByUrl(nome.trim());
        if (resultado.size() > 0) {
            return true;
        }
        return false;
    }

    private boolean isHostLoaded(String nome) {
        List<String> resultado = hr.findByHost(nome.trim());
        if (resultado.size() > 0) {
            return true;
        }
        return false;
    }

    private boolean isDocumentoLoaded(String nome) {
        List<String> resultado = dr.findByUrl(nome.trim());
        if (resultado.size() > 0) {
            return true;
        }
        return false;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Protocolo de Exclusão">
    private void ProtocoloExclusao(String url) {
        try {
            if (this.isEqualsLastVisitedHost(url)) {
                Thread.sleep(10000);
            }
            this.ProtocoloExclusaoRobos(url);
        } catch (InterruptedException ex) {
            Logger.getLogger(ColetorService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void ProtocoloExclusaoRobos(String url) {
        String newUrl = url.endsWith("/") ? url + "robots.txt" : url + "/robots.txt";
        if (StringUtils.countOccurrencesOf(newUrl, "/") > 3) {
            return;
        }
        ArrayList<String> robots = this.LerArquivoRobots(newUrl);
        for (String linha : robots) {
            this.ProtocoloExclusaoRobosDissalow(linha, url);
        }

    }

    private void ProtocoloExclusaoRobosDissalow(String linha, String _url) {
        String[] newLine = null;
        String url = _url.endsWith("/") ? _url.replace(_url.substring(_url.length() - 1), "") : _url + "";

        if (!linha.contains("disallow:")) {
            return;
        }

        newLine = linha.split(":");
        if (newLine[1].trim().contains("*")) {
            //realizar tratamento para expressão regular
            return;
        }

        if (this.exclusaoRobo.contains(url + (newLine[1].trim()))) {
            this.exclusaoRobo.remove(url + (newLine[1].trim().matches(".*")));
        }

    }

    private Boolean isEqualsLastVisitedHost(String url) {
        try {
            URL newUrl = new URL(url);
            if (newUrl.getHost().equals(lastVisitedHost)) {
                return true;
            }
            lastVisitedHost = url;
        } catch (MalformedURLException ex) {
            Logger.getLogger(ColetorService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="StopWords">
    private String FiltrarStopWords(String texto, String idioma) {
        //verificar témino da url para saber idioma do site, se não for de nehum conhecido, remover apenas stopwords porgues e ingles
        String caminho = "/home/fernando/MEGA/Sistemas de informação/7° período/Técnologias Emergentes/app/app/src/main/java/com/maquinadebusca/app/stopwords/";
        ArrayList<String> stopWords = this.LerArquivo(caminho.trim() + IdentificaIdioma(idioma.trim()));
        String novoTexto[] = texto.toLowerCase().split(" ");
        String textoSemStopWords = "";
        String aux = "";
        for (int i = 0; i < novoTexto.length; i++) {
            aux = RemoverPontuacao(novoTexto[i].trim());
            if (!stopWords.contains(aux.trim())) {
                textoSemStopWords += aux;
            }
        }

        return textoSemStopWords;
    }

    private String IdentificaIdioma(String idioma) {
        String arquivo = "";
        switch (idioma.trim()) {
            case "en-US":
                arquivo = "en.sw";
                break;
            case "en-us":
                arquivo = "en.sw";
                break;
            case "en":
                arquivo = "en.sw";
                break;
            default:
                arquivo = "pt.sw";
        }
        return arquivo;
    }

    private String RemoverPontuacao(String palavra) {
//        String newWord = palavra.replaceAll("[^a-zA-Z0-9 \\u0080-\\u9fff]", "");
        String newWord = palavra.replaceAll("[;?!\'\":,“ ”-]", "");
        newWord = (newWord.endsWith(".")) ? newWord.substring(0, newWord.length() - 1) : newWord;
        return newWord + " ";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Arquivos">
    private void SalvarArquivo(String texto, String caminho) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(caminho), "utf-8"))) {
            writer.write(texto);
        } catch (Exception ex) {
            Logger.getLogger(ColetorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ArrayList<String> LerArquivoRobots(String url) {
        ArrayList<String> linhas = new ArrayList<String>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            String linha = null;
            while ((linha = in.readLine()) != null) {
                linhas.add(linha.toLowerCase().trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linhas;
    }

    private ArrayList<String> LerArquivo(String caminho) {
        ArrayList<String> linhas = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(caminho));
            while (br.ready()) {
                String linha = br.readLine();
                linhas.add(linha.toLowerCase().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linhas;
    }
    //</editor-fold>
}
