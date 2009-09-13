package javabot.commands;

import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ApiDao;
import javabot.javadoc.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Created Jan 9, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class InfoApi extends BaseCommand {
    @Autowired
    private ApiDao dao;
    @Autowired
    private ApplicationContext context;
    @Param
    String name;

    @Override
    @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
    public void execute(final Javabot bot, final BotEvent event) {
        final String destination = event.getChannel();
        final Api api = dao.find(name);
        if (api != null) {
            bot.postMessage(new Message(destination, event, String.format(
                "The %s API can be found at %s.  I know about these packages from that API:  %s",
                api.getName(), api.getBaseUrl(), api.getPackages() == null ? "*" : api.getPackages())));
        } else {
            bot.postMessage(new Message(destination, event, String.format(
                "I don't have javadoc for %s, %s", name, event.getSender())));
        }
    }

    private void drop(final Javabot bot, final BotEvent event, final String destination, final Api api,
        final ApiDao apiDao) {
        bot.postMessage(new Message(destination, event, String.format(
            "removing old %s javadoc", api.getName())));
        apiDao.delete(api);
        bot.postMessage(new Message(destination, event, String.format(
            "done removing javadoc for %s", api.getName())));
    }
}