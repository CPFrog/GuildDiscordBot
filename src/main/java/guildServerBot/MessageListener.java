package guildServerBot;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
    public static void main(String[] args) throws LoginException{
        String bot_token="OTI2MzExMTIwODU4NDA2OTEy.Yc50dQ.q__URDJJRI01JhBmJqb1p6hY13M";
        JDA jda=JDABuilder.createDefault(bot_token).build();
        jda.addEventListener(new MessageListener());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.isFromType(ChannelType.TEXT)){
            System.out.printf("[%s][%s] %#s: %s%n",event.getGuild());
        }
    }
}
