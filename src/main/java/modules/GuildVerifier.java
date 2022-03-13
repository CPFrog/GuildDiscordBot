/* GuildVerifier : 길드원인지 판단해주는 역할을 수행하는 모듈.
   - verify : 로스트아크 캐릭터명을 입력받고 해당 캐릭터가 길드원인지 여부를 로아 공홈 웹 크롤링을 통해 판단함.
 */

package modules;

public class GuildVerifier {
    private final String curGuildName;

    GuildVerifier(String guildName) {
        this.curGuildName = guildName;
    }

    // 로스트아크 캐릭터명을 입력받고 해당 캐릭터가 길드원인지 여부를 로아 공홈 웹 크롤링을 통해 판단함.
    public boolean verify(String name) {
        WebCrawler wc = new WebCrawler(name);
        String guild = wc.getGuild();

        return this.curGuildName.equals(guild);
    }
}
