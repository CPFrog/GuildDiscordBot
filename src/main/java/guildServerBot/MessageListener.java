package guildServerBot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.*;

import modules.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.ArrayList;
import java.util.List;

//ref: https://blog.naver.com/PostView.naver?blogId=duckhyun4433&logNo=221982334696&parentCategoryNo=&categoryNo=52&viewDate=&isShowPopularPosts=true&from=search

public class MessageListener extends ListenerAdapter {
    memberInfo mi = new memberInfo();
    private String gName = "밤잠";

    public static void main(String[] args) throws LoginException {
        DiscordToken dt = new DiscordToken();
        String bot_token = dt.getToken();
        JDA jda = JDABuilder.createDefault(bot_token).setMemberCachePolicy(MemberCachePolicy.ALL).enableIntents(GatewayIntent.GUILD_MEMBERS).build();
        // 멤버 리스트 안나오는 현상 수정 참고 : https://stackoverflow.com/questions/61226721/discord-jda-invalid-member-list
        jda.addEventListener(new MessageListener());
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();
        List<TextChannel> tcList = guild.getTextChannelsByName("인증", true);
        TextChannel tc = tcList.get(0);
        for (TextChannel tmp : tcList) {
            if (tmp.getName().equals("인증")) {
                tc = tmp;
                break;
            }
        }
        tc.sendMessage("역할을 부여받으려면 '!인증 캐릭터명' 형태로 입력해주세요.").queue();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        TextChannel tc = event.getTextChannel();
        String message = event.getMessage().getContentRaw();
        Guild guild = event.getGuild();
        if (event.getChannel().getName().equals("인증")) {
            if (message.startsWith("!인증")) {
//            System.out.println("event :"+event.getMessage().getContentRaw());
                Verifier vf = new Verifier(gName);
                String name = message.split(" ")[1].trim();
                if (vf.verify(name)) {
                    if (this.mi.signIn(name)) {
                        Member target = event.getMember();
                        Role role = guild.getRolesByName("길드원", true).get(0);
                        guild.modifyNickname(target, name).queue();
                        guild.addRoleToMember(target.getId(), role).queue(); //ID만 가져오라는데 이거 맞나?
                    } else {
                        tc.sendMessage(name + "님은 이미 인증된 길드원입니다.").queue();
                        try {
                            Thread.sleep(1500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    tc.sendMessage(name + "님은 " + gName + " 길드에 가입되어있지 않습니다.").queue();
                    try {
                        Thread.sleep(1500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                eraser(2, event, 3);
            } else if (message.startsWith("!명령어")) {
                tc.sendMessage("이 봇에서 지원되는 명령어입니다.\n!인증 (캐릭터명) : 길드원 인증 명령어입니다.\n!길드설정 (길드명) : 길드원 여부를 판단하는 길드명을 설정합니다.");
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                eraser(2, event, 3);
            }
            // 임원 이상급 멤버인 경우 검색 길드명 설정 가능하도록 하는 권한 부여. 추후 구현.
            else if (message.startsWith("!길드설정")) {
                List<Role> roleList = guild.getRolesByName("길드원", false);
                roleList.add(guild.getRolesByName("@everyone", false).get(0));
                String unvarifiedName = message.split(" ")[1].trim();
                List<Member> members = new ArrayList<>();
                for (Role r : roleList) {
                    List<Member> temp = guild.getMembersWithRoles(r);
                    members.addAll(temp);
                }
                Member sender = event.getMember();
                if (!members.contains(sender)) {
                    setgName(unvarifiedName);
                    tc.sendMessage("길드 이름을 <" + gName + ">으로 설정했습니다.").queue();
                    eraser(2, event, 3);
                } else {
                    tc.sendMessage("길드 설정을 변경하려면 임원 이상의 권한이 필요합니다.").queue();
                    eraser(2, event, 3);
                }
            } else {
                tc.sendMessage("명령어의 형태가 잘못되었습니다. !명령어 를 사용해서 전체 명령어를 확인하실 수 있습니다.").queue();

                eraser(2, event, 3);
            }
        } else {
            System.out.printf("[%s] %#s: %s%n", event.getChannel().getName(), event.getAuthor(), event.getMessage().getContentDisplay());
        }
    }

    public void eraser(float delaySec, MessageReceivedEvent event, int count) {
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

    public void setgName(String gName) {
        this.gName = gName;
    }
}

class Verifier {
    private final String curGuildName;

    Verifier(String guildName) {
        this.curGuildName = guildName;
    }

    public boolean verify(String name) {
        WebCrawler wc = new WebCrawler(name);
        String guild = wc.getGuild();

        return this.curGuildName.equals(guild);
    }
}
