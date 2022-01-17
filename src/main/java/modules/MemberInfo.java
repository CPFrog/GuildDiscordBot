package modules;

import java.util.HashMap;

import net.dv8tion.jda.api.entities.*;

public class MemberInfo {
    private final HashMap<String, Member> members = new HashMap<>();

    public void signIn(String name, Member member) {
        members.put(name, member);
    }

    public int forceDelete(String name){
        if(!members.isEmpty()){
            if(members.containsKey(name)) {
                members.remove(name);
                return 0;
            }
            else return 1; // 인증된 적이 없는 멤버임
        }
        else return 2; // 인증 리스트가 없음
    }
    public boolean isMember(String name) {
        return members.containsKey(name);
    }
}