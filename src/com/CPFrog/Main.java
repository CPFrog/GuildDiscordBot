package com.CPFrog;

public class Main {
    public static void main(String[] args) {
        WebCrawler wc=new WebCrawler();
        String guild=wc.getGuild();
        System.out.println(guild);

        WebCrawler wc2=new WebCrawler("바드받으세영");
        guild=wc2.getGuild();
        System.out.println(guild);
    }
}