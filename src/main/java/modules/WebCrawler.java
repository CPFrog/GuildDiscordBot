package modules;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class WebCrawler {
    private String name;
    private String infoURL = "https://lostark.game.onstove.com/Profile/Character/";
    private String troubleURL_p1 = "https://www.inven.co.kr/board/lostark/5355";
    private String troubleURL_p2 = "?name=subjcont&keyword=";
    private String troubleURL_p3 = "&sterm=";
    private String guild = null;

    public WebCrawler(){}

    public WebCrawler(String name) {
        this.name = name;
    }

    private Document doc = null;

    private void Crawl() throws IOException {
        this.infoURL += name;

        try {
            doc = Jsoup.connect(infoURL).get();
        } catch (IOException e) {
            System.out.println("IO 예외 발생!");
            e.printStackTrace();
        }

        doc.html();

        Elements elem = doc.select("div[class=\"game-info__guild\"]");

        int cnt = 0;
        for (Element e : elem.select("span")) {
            if (e.text().equals("길드"))
                continue;
            guild = e.text();
        }
    }

    public String getGuild() {
        if (guild == null) {
            try {
                Crawl();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return guild;
    }

    private int recent_no() {
        int startNum = 10000000;
        try {
            doc = Jsoup.connect(troubleURL_p1).get();
        } catch (IOException e) {
            System.out.println("IO 예외 발생!");
            e.printStackTrace();
        }
        doc.html();
        Elements numbers = doc.select("td[class=\"num\"]"); // 게시글 번호 가져오는 코드
        return startNum - Integer.parseInt(numbers.get(1).text());
    }

    public ArrayList<String> getTroubles(String keyword) {
        int startNum = recent_no();
        String invenURL = troubleURL_p1 + troubleURL_p2 + keyword + troubleURL_p3;
        ArrayList<String> urls = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int stermNo = startNum + 10000 * i;
            invenURL.concat(Integer.toString(stermNo));
            try {
                doc = Jsoup.connect(invenURL).get();
            } catch (IOException e) {
                System.out.println("IO 예외 발생!");
                e.printStackTrace();
            }
            doc.html();
            Elements links = doc.select("a[class=\"subject-link\"]");
            if (links.size() > 0) {
                for (Element e : links) {
                    String text = e.text();
                    if (text.contains("셀프") || text.contains("셀박"))
                        continue;
                    else {
                        String link = e.attr("href");
                        urls.add(link);
                    }
                }
            }
        }
        return urls;
    }
}
