package modules;

import java.util.HashMap;

import net.dv8tion.jda.api.entities.*;

public class MemberInfo {
    private final HashMap<String, Member> members = new HashMap<>();

    public void signIn(String name, Member member) {
        members.put(name, member);
    }

    public boolean isMember(String name) {
        return members.containsKey(name);
    }
}