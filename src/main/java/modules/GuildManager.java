package modules;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class GuildManager {
    private String gName = "";

    public GuildManager(String name) {
        this.gName = name;
    }

    public String editGuildName(MessageReceivedEvent event)
    // 임원 이상급 멤버인 경우 검색 길드명 설정 가능하도록 하는 권한 부여. 추후 구현.
    {
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
        eraser(2, event, 3);
        return targetName;
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
