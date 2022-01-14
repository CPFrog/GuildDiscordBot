package modules;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class MembersManager {
    private String gName = "";

    public MembersManager(String gName) {
        setgName(gName);
    }

    public void setgName(String name) {
        this.gName = name;
    }

    public void verify(MessageReceivedEvent event, MemberInfo mi) {
        TextChannel tc = event.getTextChannel();
        String message = event.getMessage().getContentRaw();
        Guild guild = event.getGuild();
        GuildVerifier vf = new GuildVerifier(gName);
        String name = message.split(" ")[1].trim();

        if (vf.verify(name)) {
            if (!mi.isMember(name)) {
                Member target = event.getMember();
                mi.signIn(name, target);
                Role role = guild.getRolesByName("길드원", true).get(0);
                guild.modifyNickname(target, name).queue();
                guild.addRoleToMember(target.getId(), role).queue();
                eraser(0, event, 3);
            } else {
                tc.sendMessage(name + "님은 이미 인증된 길드원입니다.").queue();
                eraser(2, event, 3);
            }
        } else {
            tc.sendMessage(name + "님은 " + gName + " 길드에 가입되어있지 않습니다.").queue();
            eraser(2, event, 3);
        }

    }

    public void guide(MessageReceivedEvent event) {
        TextChannel tc = event.getTextChannel();
        tc.sendMessage("이 봇에서 지원되는 명령어입니다.\n!인증 (캐릭터명) : 길드원 인증 명령어입니다.").queue();
        eraser(3, event, 3);
    }

    public void commandError(MessageReceivedEvent event) {
        TextChannel tc = event.getTextChannel();
        tc.sendMessage("잘못된 명령어입니다.\n'!명령어'를 사용해 지원되는 명령어 양식을 다시 확인해주세요.").queue();
        eraser(3, event, 3);
    }

    private void eraser(float delaySec, MessageReceivedEvent event, int count) {
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
}
