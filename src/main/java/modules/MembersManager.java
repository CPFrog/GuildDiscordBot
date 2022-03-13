/* MembersManager : 일반 길드원들이 사용하는 명령어의 기능을 제공하는 모듈
   - setgName : 길드의 이름을 설정하는 메소드.
   - verify : 로아 공홈에서 해당 캐릭터가 길드원이 맞는지 검색하는 메소드.
   - guide : 일반 길드원들이 사용할 수 있는 명령어를 보여주는 메소드
   - nameChange : 캐릭터명이 변경되거나 기존과 다른 캐릭터로 길드원 인증을 하려는 경우.
   - commandError : 일반 길드원용 커멘드에서 해당 명령어가 없는 경우 오류 문구를 채팅 채널에 출력하는 메소드.
   - eraser : 명령어 실행 결과 또는 오류 문구를 삭제하기 위해 작성한 메소드.
 */

package modules;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class MembersManager {
    private String gName = "";

    public MembersManager(String gName) {
        setgName(gName);
    }

    // 길드의 이름을 설정하는 함수.
    public void setgName(String name) {
        this.gName = name;
    }

    // 로아 공홈에서 해당 캐릭터가 길드원이 맞는지 검색하는 메소드.
    public void verify(MessageReceivedEvent event, MemberInfo mi) {
        TextChannel tc = event.getTextChannel();
        String message = event.getMessage().getContentRaw();
        Guild guild = event.getGuild();
        GuildVerifier vf = new GuildVerifier(gName);
        String name = message.split(" ")[1].trim();

        // 만약 길드원이 맞을 경우
        if (vf.verify(name)) {
            if (!mi.isMember(name)) {
                // 최초 인증하는 길드원일 경우 인증된 길드원 리스트에 등록하고 역할 부여
                Member target = event.getMember();
                mi.signIn(name, target);
                Role role = guild.getRolesByName("길드원", true).get(0);
                guild.modifyNickname(target, name).queue();
                guild.addRoleToMember(target.getId(), role).queue();
                eraser(0, event, 2);
            } else {
                // 이미 인증된 길드원일 경우 오류 메시지 출력
                tc.sendMessage(name + "님은 이미 인증된 길드원입니다.").queue();
                eraser(2, event, 3);
                // 나중에 필요시 해당 인증을 시도한 디스코드 계정 밴 하는 코드도...
            }
        } else {
            // 길드원이 아닐 경우 오류 메시지 출력
            tc.sendMessage(name + "님은 " + gName + " 길드에 가입되어있지 않습니다.").queue();
            eraser(2, event, 3);
        }
    }

    // 명령어 안내
    public void guide(MessageReceivedEvent event) {
        TextChannel tc = event.getTextChannel();
        tc.sendMessage("이 봇에서 지원되는 명령어입니다.\n!인증 (캐릭터명) : 길드원 인증 명령어입니다.\n!변경 (캐릭터명): 길드에 가입된 본인 캐릭터에 한해 디코 닉네임을 변경할 때 사용하는 명령어입니다.").queue();
        eraser(3, event, 3);
    }

    // 캐릭터명이 변경되거나 기존과 다른 캐릭터로 길드원 인증을 하려는 경우.
    public void nameChange(MessageReceivedEvent event, MemberInfo mi) {
        TextChannel tc = event.getTextChannel();
        GuildVerifier vf = new GuildVerifier(gName);
        String message = event.getMessage().getContentRaw();
        String name = message.split(" ")[1].trim();
        String user = event.getMember().getNickname();
        Member target = event.getMember();
        Guild guild = event.getGuild();

        if (vf.verify(name)) {
            int resultCode = mi.changeName(user, name, target);
            if (resultCode == 0) {
                guild.modifyNickname(target, name).queue();
                eraser(0, event, 3);
            } else if (resultCode == 1) {
                tc.sendMessage(name + "님의 닉네임으로 이전에 인증된 내역이 있습니다.\n닉네임 변경을 취소합니다.").queue();
                eraser(3, event, 3);
            } else {
                tc.sendMessage("변경 전 닉네임인 " + user + "으로 인증된 내역을 찾을 수 없어 닉네임 변경을 할 수 없습니다.\n닉네임 변경을 취소합니다.").queue();
                eraser(4, event, 3);
            }
        } else {
            tc.sendMessage(name + "님은 " + gName + " 길드에 가입되어있지 않습니다.").queue();
            eraser(2, event, 3);
        }
    }

    // 일반 길드원용 커멘드에서 해당 명령어가 없는 경우 오류 문구를 채팅 채널에 출력하는 메소드.
    public void commandError(MessageReceivedEvent event) {
        TextChannel tc = event.getTextChannel();
        tc.sendMessage("잘못된 명령어입니다.\n'!명령어'를 사용해 지원되는 명령어 양식을 다시 확인해주세요.").queue();
        eraser(3, event, 3);
    }

    // 명령어 실행 결과 또는 오류 문구를 삭제하기 위해 작성한 메소드.
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
