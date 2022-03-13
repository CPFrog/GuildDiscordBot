/* WebCrawler : 길드원 인증 또는 사사게 검색 시 실제 웹 크롤링을 수행하는 모듈. Jsoup을 기반으로 동작.
   - Crawl : 전투정보실에서 해당 캐릭터가 가입된 길드 이름을 가져오는 메소드.
   - getGuild : 캐릭터의 정보를 크롤링 할 때 가입된 길드명 가져오는 메소드.
   - recent_no : 로스트아크 인벤 사사게 검색 시 가장 최근 게시글의 글 번호를 가져오는 메소드.
   - getTroubles : 사사게에서 해당 키워드를 제목/내용에 포함하는 게시글이 몇 개나 있는지 알려주는 메소드.
   TODO: Crawl과 getGuild가 굳이 나눠져있어야 할 필요가 있는지 재검토하고 불필요한 모듈화인 경우 모듈 병합.
 */

package modules;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebCrawler {
    private String name;
    private String infoURL = "https://lostark.game.onstove.com/Profile/Character/";
    private final String troubleURL_p1 = "https://www.inven.co.kr/board/lostark/5355";
    private final String troubleURL_p2 = "?name=subjcont&keyword="; // p1과 일부러 나눠놓은거임. (최초 검색에서는 이부분 필요 X)
    private final String troubleURL_p3 = "&sterm=";
    private String guild = null;

    public WebCrawler() {
    }

    public WebCrawler(String name) {
        this.name = name;
    }

    private Document doc = null;

    // 전투정보실에서 해당 캐릭터가 가입된 길드 이름을 가져오는 메소드.
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

        // int cnt = 0;
        for (Element e : elem.select("span")) {
            if (e.text().equals("길드"))
                continue;
            guild = e.text();
        }
    }

    // 캐릭터의 정보를 크롤링 할 때 가입된 길드명 가져오는 함수
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

    // 로스트아크 인벤 사사게 검색 시 가장 최근 게시글의 글 번호를 가져오는 메소드.
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

    // 사사게에서 해당 키워드를 제목/내용에 포함하는 게시글이 몇 개나 있는지 알려주는 메소드.
    public String getTroubles(String keyword) {
        int startNum = recent_no();
        String invenURL = troubleURL_p1 + troubleURL_p2 + keyword + troubleURL_p3;
        int count = 0;
        for (int i = 0; i < 10; i++) {
            int stermNo = startNum + 10000 * i;
            try {
                doc = Jsoup.connect(invenURL + stermNo).get();
            } catch (IOException e) {
                System.out.println("IO 예외 발생!");
                e.printStackTrace();
            }
            doc.html();
            Elements links = doc.select("a[class=\"subject-link\"]");
            int arrLen = links.size();

            for (int j = 1; j < arrLen; j++) {
                String text = links.get(j).text();
                if (text.contains("셀프") || text.contains("셀박"))
                    continue;
                else {
                    // 상위 게시글 5개 가져오기 용 코드
//                        String link = links.get(j).attr("href");
                    count++;
                }
            }
        }
        return troubleURL_p1 + troubleURL_p2 + keyword + " " + count;
    }
}
