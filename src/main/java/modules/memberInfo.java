package modules;

import java.util.HashSet;

public class memberInfo {
    private final HashSet<String> members = new HashSet<>();

    public boolean signIn(String name){
        if(!isMember(name)) {
            members.add(name);
            return true;
        }
        else return false;
    }

    public boolean isMember(String name){
        return members.contains(name);
    }
}