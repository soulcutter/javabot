package javabot.operations;

import javabot.BotEvent;
import javabot.Javabot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ricky_clarkson
 */
public class SpecialCasesOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(SpecialCasesOperation.class);

    public SpecialCasesOperation(final Javabot javabot) {
        super(javabot);
    }

    @Override
    public boolean handleMessage(final BotEvent event) {
        String message = event.getMessage();
        boolean handled = false;
        if(log.isDebugEnabled()) {
            log.debug("SpecialCasesOperation: " + message);
        }

        final String lowerMessage = message.toLowerCase();
        if (lowerMessage.startsWith("no")) {
            message = message.substring("no".length());

            if (message.startsWith(",")) {
                message = message.substring(",".length());
            }

            message = message.replaceAll("^\\s+", "");
            if(log.isDebugEnabled()) {
                log.debug("SpecialCasesOperation: " + message);
            }

            String key = message.substring(0, message.indexOf(" is "));
            key = key.replaceAll("^\\s+", "");
            if(log.isDebugEnabled()) {
                log.debug("SpecialCasesOperation: Key " + key);
            }
            handled = getBot().getResponses(event.getChannel(), event.getSender(),
                event.getLogin(), event.getHostname(), "forget " + key);
        }
        return handled;
    }
}
