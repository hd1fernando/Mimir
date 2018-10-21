package com.maquinadebusca.app.sementes;

import java.util.LinkedList;
import java.util.List;

    
public class Sementes {

    private List<String> urls = new LinkedList();

    public Sementes() {
    }
    

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }   
        
}
