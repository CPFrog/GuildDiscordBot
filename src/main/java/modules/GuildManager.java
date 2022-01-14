package modules;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class GuildManager {
    private String gName = "";

    public GuildManager(String name) {
        this.gName = name;
    }

    public String editGuildName(MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        String message = event.getMessage().getContentRaw();
        TextChannel tc = event.getTextChannel();
        List<Role> roleList = guild.getRolesByName("길드원", false);
        roleList.add(guild.getRolesByName("@everyone", false).get(0));
        String targetName = message.split(" ")[1].trim();
        List<Member> members = new ArrayList<>();
        for (Role r : roleList) {
            List<Member> temp = guild.getMembersWithRoles(r);
            members.addAll(temp);
        }
        Member sender = event.getMember();
        if (!members.contains(sender)) {
            this.gName = targetName;
            tc.sendMessage("길드 이름을 <" + this.gName + ">으로 설정했습니다.").queue();
        } else {
            tc.sendMessage("길드 설정을 변경하려면 임원 이상의 권한이 필요합니다.").queue();
        }
        eraser(2, event, 2);
        return targetName;
    }

    public void deleteInfo(MessageReceivedEvent event){
        TextChannel tc= event.getTextChannel();
        tc.sendMessage("지원 예정입니다.").queue();
        eraser(2,event,2);
    }

    public void guide(MessageReceivedEvent event) {
        TextChannel tc = event.getTextChannel();
        tc.sendMessage("이 봇에서 지원되는 임원진 전용 명령어입니다.\n.길드변경/.길드설정 (길드이름) : 길드원 여부를 판단하는 기준 길드명을 변경합니다.\n.삭제 (캐릭터명) : 해당 캐릭터의 기존에 인증 정보를 삭제합니다.").queue();
        eraser(5, event, 2);
    }

    public void commandError(MessageReceivedEvent event) {
        TextChannel tc = event.getTextChannel();
        tc.sendMessage("잘못된 명령어입니다.\n'.명령어'를 사용해 지원하는 명령어 양식을 다시 확인해주세요.").queue();
        eraser(3, event, 2);
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
    }

}
