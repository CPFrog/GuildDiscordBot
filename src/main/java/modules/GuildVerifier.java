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
}
