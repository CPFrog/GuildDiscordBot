package guildServerBot;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class ReadyListener implements EventListener{
    public static void main(String[] args) throws LoginException{
        String bot_token="OTI2MzExMTIwODU4NDA2OTEy.Yc50dQ.q__URDJJRI01JhBmJqb1p6hY13M";
        JDA jda=JDABuilder.createDefault(bot_token).addEventListeners(new ReadyListener()).build();
    }

    @Override
    public void onEvent(GenericEvent event){
        if(event instanceof ReadyEvent)
            System.out.println("API가 준비되었습니다.");
    }
}
