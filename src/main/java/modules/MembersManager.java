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
                eraser(0, event, 2);
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
        tc.sendMessage("이 봇에서 지원되는 명령어입니다.\n!인증 (캐릭터명) : 길드원 인증 명령어입니다.\n!변경 (캐릭터명): 길드에 가입된 본인 캐릭터에 한해 디코 닉네임을 변경할 때 사용하는 명령어입니다.").queue();
        eraser(3, event, 3);
    }

    public void nameChange(MessageReceivedEvent event, MemberInfo mi){
        TextChannel tc=event.getTextChannel();
        GuildVerifier vf=new GuildVerifier(gName);
        String message= event.getMessage().getContentRaw();
        String name = message.split(" ")[1].trim();
        String user=event.getMember().getNickname();
        Member target= event.getMember();
        Guild guild=event.getGuild();

        if(vf.verify(name)){
            int resultCode=mi.changeName(user,name,target);
            if(resultCode==0){
                guild.modifyNickname(target,name).queue();
                eraser(0,event,3);
            }
            else if(resultCode==1){
                tc.sendMessage(name + "님의 닉네임으로 이전에 인증된 내역이 있습니다.\n닉네임 변경을 취소합니다.").queue();
                eraser(3, event, 3);
            }
            else{
                tc.sendMessage("변경 전 닉네임인 "+user+"으로 인증된 내역을 찾을 수 없어 닉네임 변경을 할 수 없습니다.\n닉네임 변경을 취소합니다.").queue();
                eraser(4,event,3);
            }
        }
        else {
            tc.sendMessage(name + "님은 " + gName + " 길드에 가입되어있지 않습니다.").queue();
            eraser(2, event, 3);
        }
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
