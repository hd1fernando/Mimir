package com.maquinadebusca.app.sementes;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

    
public class Sementes {

    private List<String> urls = new LinkedList();
    private List<LocalDate> ultimaColeta = new LinkedList();

    public List<LocalDate> getUltimaColeta() {
        return ultimaColeta;
    }

    public void setUltimaColeta(List<LocalDate> ultimaColeta) {
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
