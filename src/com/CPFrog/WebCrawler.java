package com.CPFrog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebCrawler {
    private String name="CP개구링";
    private String url="https://lostark.game.onstove.com/Profile/Character/";
    private String guild=null;
    WebCrawler(){}
    WebCrawler(String name) {
        this.name=name;
    }

    private Document doc= null;

    private void Crawl() throws IOException{
        this.url+=name;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("IO Exception Occurred!");
        }

        doc.html();

        Elements elem=doc.select("div[class=\"game-info__guild\"]");

        int cnt=0;
        for(Element e:elem.select("span")){
            if(e.text().equals("길드"))
                continue;
            guild=e.text();
        }
    }

    public String getGuild(){
        if(guild==null){
            try {
                Crawl();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return guild;
    }
}
