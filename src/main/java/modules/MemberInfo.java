/* MemberInfo : 인증된 길드원들에 대한 리스트를 관리 및 기능을 제공하는 모듈
   - signIn : 새로운 길드원이 가입하게 된 경우 인증된 길드원 리스트에 추가하는 메소드.
   - forceDelete : 인증된 길드원 리스트에서 argument(name)의 이름을 가지는 길드원을 강제로 삭제하는 메소드.
   - isMember : 인증된 길드원 목록에 해당 길드원 이름으로 된 인증이 있는지 검사하는 메소드.
   - changeName : 이미 인증된 길드원이 다른 캐릭터로의 재인증 또는 캐릭터명을 변경한 경우 리스트에도 반영할 수 있도록 지원하는 메소드.
   - refresh : 길드원 리스트를 뤱 크롤링을 통해 최신화 시키는 함수.
 */

package modules;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.entities.*;

public class MemberInfo {
    private final HashMap<String, Member> members = new HashMap<>();

    // 새로운 길드원이 가입하게 된 경우 인증된 길드원 리스트에 추가하는 메소드.
    public void signIn(String nickname, Member member) {
        members.put(nickname, member);
    }

    // 인증된 길드원 리스트에서 argument(name)의 이름을 가지는 길드원을 강제로 삭제하는 메소드.
    public int forceDelete(String name) {
        if (isMember(name)) {
            members.remove(name);
            return 0;
        } else return 1; // 인증된 적이 없는 멤버임
    }

    // 인증된 길드원 목록에 해당 길드원 이름으로 된 인증이 있는지 검사하는 메소드.
    public boolean isMember(String name) {
        return members.containsKey(name);
    }

    // 이미 인증된 길드원이 다른 캐릭터로의 재인증 또는 캐릭터명을 변경한 경우 리스트에도 반영할 수 있도록 지원하는 메소드.
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

    // 길드원 리스트를 뤱 크롤링을 통해 최신화 시키는 함수.
    // 길드원 리스트를 순회하면서 공식 홈페이지의 전투정보실에 캐릭터를 검색하고, 만약 더 이상 길드원이 아닌 경우 해당 디코 유저 강퇴 및 리스트에서 제거
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