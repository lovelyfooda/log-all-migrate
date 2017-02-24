package com.ec.log.all.jetty;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.springframework.stereotype.Component;

import com.ec.commons.command.ServerShutdownOption;
import com.ec.commons.server.jetty.IHandler;
import com.ec.commons.server.jetty.JettyUrl;
import com.ec.log.all.entrance.ProgramEntrance;
import com.ec.log.all.service.BusinessProcess;
import com.ec.log.all.service.IStatistical;

/**
 *
 * @ClassName: CommandHandler
 * @author ChenJun
 * @date 2015年11月13日 上午9:16:16
 */
@Component
@JettyUrl(target = "/")
public class CommandHandler implements IHandler {
    protected Logger log = LogManager.getLogger(this.getClass());

    @Override
    public void doRequest(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Writer writer = response.getWriter();
        String command = request.getParameter("command");
        log.info("received command " + command);
        if ("Stop".equalsIgnoreCase(command)) {
            ProgramEntrance.appMain.shutdownServer(ServerShutdownOption.GRACEFUL);
            writer.write("server stoping");
        } else if ("startFullSync".equalsIgnoreCase(command)) {
            writer.write("filetered startFullSync:" + request.getRequestURI());
            IStatistical service = ProgramEntrance.getApplicationContext().getBean(IStatistical.class);
            service.startFullSync();
        } else if ("getMysqlDataCount".equalsIgnoreCase(command)) {
            writer.write("filetered getMysqlDataCount:" + request.getRequestURI());
            IStatistical service = ProgramEntrance.getApplicationContext().getBean(IStatistical.class);
            service.getMysqlDataCount();
        } else if ( "finishSync".equalsIgnoreCase(command) ) {
        	writer.write("finish Sync" + request.getRequestURI());
        	BusinessProcess businessProcess = ProgramEntrance.getApplicationContext().getBean(BusinessProcess.class);
        	businessProcess.finishSignal();
        } else {
            writer.write("unknown command");
        }
        writer.flush();
    }
}
