package javabot.commands;

import java.util.List;
import java.util.ArrayList;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.model.Channel;
import javabot.dao.ChannelDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class AddChannel implements Command {
    @Autowired
    private ChannelDao dao;
    private static final String LOGGED = "logged";

    @Override
    public void execute(final Javabot bot, final BotEvent event, final List<String> args) {
        if (args.isEmpty()) {
            bot.postMessage(new Message(event.getChannel(), event,
                String.format("usage: addChannel <channel> (%s) (password)", LOGGED)));
            bot.postMessage(new Message(event.getChannel(), event,
                "usage: the password and 'logged' are optional and can appear in any order"));
        } else {
            final String channelName = args.remove(0);
            Boolean logged = null;
            String key = null;
            while (!args.isEmpty()) {
                final String next = args.remove(0);
                logged = logged == null && LOGGED.equals(next);
                if(!LOGGED.equals(next) && key == null) {
                    key = next;
                }
            }
            logged = logged == null ? Boolean.TRUE : logged;
            
            Channel channel = dao.get(channelName);
            if (channel == null) {
                channel = dao.create(channelName, logged, key);
            } else {
                channel.setLogged(logged);
                dao.save(channel);
            }
            bot.postMessage(new Message(event.getChannel(), event, "Now joining " + channelName +
                (logged ? " and logging it" : "")));
            channel.join(bot);
            bot.postMessage(new Message(channelName, event, "I was asked to join this channel by " + event.getSender()));
        }
    }
}
