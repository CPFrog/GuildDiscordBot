/* Common : 일반 길드원용, 길드 관리자용 모듈에서 공통적으로 존재하는 기능을 모은 모듈
   TODO: 1. eraser, 2. 사사게 검색, 3. 명령어 오류 안내, 4. 공통변수(a.k.a. 길드 이름) 추후 여기로 옮겨오기.
 */
package modules;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class Common {
    public String gName;

    // 명령어 실행 결과 또는 오류 문구를 삭제하기 위해 작성한 메소드.
    public void eraser(float delaySec, MessageReceivedEvent event, int count) {
        try {
            Thread.sleep((int) (delaySec * 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextChannel tc = event.getTextChannel();
        MessageHistory mh = new MessageHistory(tc);
        List<Message> msg = mh.retrievePast(count).complete();
        tc.deleteMessages(msg).complete();
        tc.sendMessage("역할을 부여받으려면 '!인증 캐릭터명' 형태로 입력해주세요.").queue();
    }

    // 로스트아크 인벤에서 사사게를 검색하는 함수.
    public void searchTroubles(MessageReceivedEvent event) {
        TextChannel tc = event.getTextChannel();
        WebCrawler wc = new WebCrawler();
        String message = event.getMessage().getContentRaw();
        String targetName = message.split(" ")[1].trim();
        String[] result = wc.getTroubles(targetName).split(" ");
        tc.sendMessage("검색어: **" + targetName + "**, 게시글 수: **" + result[1] + "**\n로스트아크 인벤 사건/사고게시판에서 제목+내용으로 최근 10만개 게시글을 검색한 결과입니다.\n제목에 '셀프' 또는 '셀박'이 포함된 경우 합산하지 않았습니다.").queue();
        tc.sendMessage("사사게 검색 바로가기: " + result[0]).queue();
    }
}
