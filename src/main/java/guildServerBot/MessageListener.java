package guildServerBot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.*;

import modules.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.ArrayList;
import java.util.List;

//ref: https://blog.naver.com/PostView.naver?blogId=duckhyun4433&logNo=221982334696&parentCategoryNo=&categoryNo=52&viewDate=&isShowPopularPosts=true&from=search

public class MessageListener extends ListenerAdapter {
    private MemberInfo mi = new MemberInfo();
    private String gName = "밤잠";
    private MembersManager manager = new MembersManager(this.gName);
    private GuildManager gm=new GuildManager(this.gName);

    public static void main(String[] args) throws LoginException {
        DiscordToken dt = new DiscordToken();
        String bot_token = dt.getToken();
        JDA jda = JDABuilder.createDefault(bot_token).setMemberCachePolicy(MemberCachePolicy.ALL).enableIntents(GatewayIntent.GUILD_MEMBERS).build();
        // 멤버 리스트 안나오는 현상 수정 참고 : https://stackoverflow.com/questions/61226721/discord-jda-invalid-member-list
        jda.addEventListener(new MessageListener());
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();
        List<TextChannel> tcList = guild.getTextChannelsByName("인증", true);
        TextChannel tc = tcList.get(0);
        for (TextChannel tmp : tcList) {
            if (tmp.getName().equals("인증")) {
                tc = tmp;
                break;
            }
        }
        tc.sendMessage("역할을 부여받으려면 '!인증 캐릭터명' 형태로 입력해주세요.").queue();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String message = event.getMessage().getContentRaw();
        if (event.getChannel().getName().equals("인증")) {
            if (message.startsWith("!인증"))
                this.manager.verify(event, mi);
            else if (message.startsWith("!명령어"))
                this.manager.guide(event);
            else
                this.manager.commandError(event);
        }
        else if(event.getChannel().getName().equals("임원")){
            if (message.startsWith("@길드변경")){
                gName=this.gm.editGuildName(event);
                this.manager.setgName(gName);
            }
            if (message.startsWith("@강제인증")){
                // 강제 길드원 인증과 관련된 작업 수행
            }
        } else {
            System.out.printf("[%s] %#s: %s%n", event.getChannel().getName(), event.getAuthor(), event.getMessage().getContentDisplay());
        }
    }
}
