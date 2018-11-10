package com.maquinadebusca.app.sementes;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Sementes {

    private List<String> urls = new LinkedList();
    private List<LocalDateTime> ultimaColeta = new LinkedList();

    public List<LocalDateTime> getUltimaColeta() {
        return ultimaColeta;
    }

    public void setUltimaColeta(List<LocalDateTime> ultimaColeta) {
        this.ultimaColeta = ultimaColeta;
    }

    public Sementes() {
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

}
