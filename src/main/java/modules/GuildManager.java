package modules;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class GuildManager {
    private String gName;

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

    public void deleteInfo(MessageReceivedEvent event, MemberInfo mi) {
        TextChannel tc = event.getTextChannel();
        String message = event.getMessage().getContentRaw();
        String targetName = message.split(" ")[1].trim();
        int returnCode = mi.forceDelete(targetName);
        switch (returnCode) {
            case 0 -> tc.sendMessage(targetName + "님에 대한 인증정보가 삭제되었습니다.").queue();
            case 1 -> tc.sendMessage(targetName + "님에 대한 인증정보가 존재하지 않습니다.").queue();
            case 2 -> tc.sendMessage("인증 테이블이 존재하지 않습니다. 봇 관리자에게 문의해주세요.").queue();
        }
        eraser(2, event, 2);
    }

    public void guide(MessageReceivedEvent event) {
        TextChannel tc = event.getTextChannel();
        tc.sendMessage("이 봇에서 지원되는 임원진 전용 명령어입니다.\n.길드변경/.길드설정 (길드이름) : 길드원 여부를 판단하는 기준 길드명을 변경합니다.\n.삭제 (캐릭터명) : 해당 캐릭터의 기존에 인증 정보를 삭제합니다.\n.갱신 : 길드원 인증 정보를 즉시 갱신합니다.").queue();
        eraser(7, event, 2);
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

    public void searchTroubles(MessageReceivedEvent event) {
        TextChannel tc = event.getTextChannel();
        WebCrawler wc = new WebCrawler();
        String message = event.getMessage().getContentRaw();
        String targetName = message.split(" ")[1].trim();
        String[] result = wc.getTroubles(targetName).split(" ");
        tc.sendMessage("["+targetName + "] 검색어로 최근 10만개 게시글의 제목+내용 검색결과, " + result[1] + "건 검색되었습니다.\n제목에 '셀프' 또는 '셀박'이 포함된 경우 합산하지 않았습니다.").queue();
        tc.sendMessage("사사게 검색 바로가기: " + result[0]).queue();
    }
}
