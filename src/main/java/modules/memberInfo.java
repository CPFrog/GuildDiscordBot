package modules;

import java.util.HashMap;

public class memberInfo {
    private final HashMap<String, Integer> members = new HashMap<>();

    public String signIn(String name){
        int role=search(name);
        if(role==4) {
            members.put(name, 3);
            return name+"님에게 길드원 권한이 부여되었습니다.";
        }
        else return name+"님은 이미 인증이 완료된 길드 구성원입니다.";
    }

    private int search(String name){
        if(this.members.containsKey(name))
            return members.get(name);
        else return 4;
    }
}