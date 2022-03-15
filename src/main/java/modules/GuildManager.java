/* GuildManager : 스태프 권한을 가진 멤버가 사용하는 명령어의 기능을 제공하는 모듈
   - editGuildName : 길드 이름 변경
   - deleteInfo : 멤버의 길드 인증 정보 강제 삭제,
   - guide : 스태프가 사용할 수 있는 명령어 안내
   - commandError : 임원 채팅 채널에서 .로 시작하는 채팅이면서 해당 명령어가 없는 경우 오류 문구 출력.
   - eraser : 명령어 실행 결과 또는 오류 문구를 삭제하기 위해 작성한 함수.
   - searchTroubles : 로스트아크 인벤에서 사사게를 검색하는 함수.
 */

package modules;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class GuildManager extends Common {
    private String gName;

    public GuildManager(String name) {
        this.gName = name;
    }

    // 로스트아크 길드원인지 판단하는 기준이 되는 길드 이름 변경 함수
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

    // 해당 캐릭터 이름을 가진 길드원이 인증된 정보를 강제로 삭제함. 간헐적으로 발생하는 길드원 인증 오류 발생시 사용.
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

    // 스태프가 사용할 수 있는 명령어 안내.
    public void guide(MessageReceivedEvent event) {
        TextChannel tc = event.getTextChannel();
        tc.sendMessage("이 봇에서 지원되는 임원진 전용 명령어입니다.\n.길드변경/.길드설정 (길드이름) : 길드원 여부를 판단하는 기준 길드명을 변경합니다.\n.삭제 (캐릭터명) : 해당 캐릭터의 기존에 인증 정보를 삭제합니다.\n.갱신 : 길드원 인증 정보를 즉시 갱신합니다.\n.사사게/!사사게 (검색어) : 해당 검색어를 포함하는 사사게 게시물을 검색하고 결과를 보여줍니다.").queue();
        eraser(7, event, 2);
    }

    // 임원 채팅 채널에서 .로 시작하는 채팅이면서 해당 명령어가 없는 경우 오류 문구 출력.
    public void commandError(MessageReceivedEvent event) {
        TextChannel tc = event.getTextChannel();
        tc.sendMessage("잘못된 명령어입니다.\n'.명령어'를 사용해 지원하는 명령어 양식을 다시 확인해주세요.").queue();
        eraser(3, event, 2);
    }


}
