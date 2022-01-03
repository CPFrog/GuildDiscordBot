package guildServerBot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import modules.WebCrawler;

//ref: https://blog.naver.com/PostView.naver?blogId=duckhyun4433&logNo=221982334696&parentCategoryNo=&categoryNo=52&viewDate=&isShowPopularPosts=true&from=search

public class MessageListener extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        DiscordToken dt = new DiscordToken();
        String bot_token = dt.getToken();
        JDA jda = JDABuilder.createDefault(bot_token).build();
        jda.addEventListener(new MessageListener());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        String message=event.getMessage().getContentRaw();
        if (message.startsWith("!인증")) {
//            System.out.println("event :"+event.getMessage().getContentRaw());
            Verifier vf = new Verifier("밤잠");
            if(vf.verify(message))
                System.out.println("해당 길드원이 맞습니다.");
            else System.out.println("해당 길드에 가입되어있지 않습니다.");
        }
        else{
            System.out.printf("[PM] %#s: %s%n", event.getAuthor(), event.getMessage().getContentDisplay());
        }
    }
}

class Verifier{
    private String curGuildName="밤잠";
    Verifier(String guildName){
        this.curGuildName=guildName;
    }
    public boolean verify(String rawMessage){
        String[] temp=rawMessage.split(" ");
        WebCrawler wc=new WebCrawler(temp[1]);
        String guild=wc.getGuild();

        return this.curGuildName.equals(guild);
    }
}
