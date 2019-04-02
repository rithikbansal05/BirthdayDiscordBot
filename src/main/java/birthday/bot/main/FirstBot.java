package birthday.bot.main;


import birthday.bot.events.AddBirthday;
import birthday.bot.events.HelloThere;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class FirstBot {

    private static JDA jda;

    public static void main(String[] args) throws Exception{
        jda = new JDABuilder(AccountType.BOT)
                .setToken("NTI1OTQ1OTQ3OTg1ODA1MzQz.Dv-CQw.vsXl4XhCrIvZ2trJ-DWZppq1w78")
                .buildBlocking();

        jda.addEventListener(new HelloThere());
        jda.addEventListener(new AddBirthday());
    }
}
