package modules;

public class GuildVerifier {
    private final String curGuildName;

    GuildVerifier(String guildName) {
        this.curGuildName = guildName;
    }

    public boolean verify(String name) {
        WebCrawler wc = new WebCrawler(name);
        String guild = wc.getGuild();

        return this.curGuildName.equals(guild);
    }

    // 매일 디코 멤버의 로아 길드 가입 여부 확인 함수
    public void dailyrenewal(MemberInfo mi) {
        WebCrawler wc = new WebCrawler();
    }
}
