package com.CPFrog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url="https://lostark.game.onstove.com/Profile/Character/";

        Scanner scanner=new Scanner(System.in);

        String name=scanner.next();

        url+=name;

        Document doc= null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        doc.html();

        Elements elem=doc.select("div[class=\"game-info__guild\"]");

        String guild;
        int cnt=0;
        for(Element e:elem.select("span")){
            if(e.text().equals("길드"))
                continue;
            guild=e.text();
            System.out.println(guild);
        }
    }
}