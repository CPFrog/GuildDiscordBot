package modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.entities.*;

public class MemberInfo {
    private final HashMap<String, Member> members = new HashMap<>();

    public void signIn(String nickname, Member member) {
        members.put(nickname, member);
    }

    public int forceDelete(String name) {
        if (isMember(name)) {
            members.remove(name);
            return 0;
        } else return 1; // 인증된 적이 없는 멤버임
    }

    public boolean isMember(String name) {
        return members.containsKey(name);
    }

    public int changeName(String prevName, String newName, Member member) {
        if (isMember(prevName)) {
            if (!isMember(newName)) {
                members.remove(prevName);
                members.put(newName, member);
                return 0; //정상적으로 닉변 완료
            } else return 1; // 변경하려는 닉네임으로 이미 인증이 되어있는 경우
        } else {
            return 2; // 길드 디코에 인증이 되어있지 않은 경우
        }
    }

    public void refresh(String guildName) {
        GuildVerifier gv = new GuildVerifier(guildName);
        // 서버에서 강퇴시키기 위한 닉네임 리스트 만들어서 반환 후, 이 리스트를 이용해 서버에서 해당 디코 유저 강퇴시키는 코드 필요함.
        for (Map.Entry<String, Member> m : members.entrySet()) {
            String target = m.getKey();
            if (!gv.verify(target)) {
                members.remove(target);
            }
        }
    }
}