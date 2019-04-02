package birthday.bot.events;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class HelloThere extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split(" ");

        if(args[0].equalsIgnoreCase("Hello")){
            if(!event.getMember().getUser().isBot())
                event.getChannel().sendMessage("Hey " + event.getMember().getUser().getName()).queue();
        }

    }
}
