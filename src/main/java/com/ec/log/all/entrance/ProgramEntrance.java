package com.ec.log.all.entrance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ec.commons.command.ServerShutdownOption;
import com.ec.commons.server.jetty.DispatchHandler;
import com.ec.commons.server.jetty.SimpleHTTPServer;

/**
 * @ClassName: EsApplicationContext
 * @Description: 程序启动类
 * @author longqingping
 * @date 2015年6月25日 上午10:53:40
 */
public class ProgramEntrance {

    private static ApplicationContext applicationContext;
    private static Logger log = LogManager.getLogger("dataConversionMainLog");

    public static ProgramEntrance appMain = null;
    private SimpleHTTPServer httpServer;
    private boolean waitingForGracefulShutdown = false;

    public static void main(String[] args) {
        try {
            appMain = new ProgramEntrance();
            appMain.init();
            appMain.start();
        } catch (Throwable t) {
            log.fatal("Fatal Error in main, System exit.", t);
            System.exit(8);
        }
    }

    private void init() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("classpath*:/spring/applicationContext.xml");
        log.info("=========>spring容器初始化完成...");

        httpServer = applicationContext.getBean(SimpleHTTPServer.class);
    }

    private void start() throws Exception {

        log.info(this + " start httpServer");
        httpServer.startServer(applicationContext.getBean(DispatchHandler.class));

        synchronized (this) {
            this.wait();
        }
        log.debug("Server Stoped Normaly!");
        System.exit(0);
    }

    public void shutdownServer(ServerShutdownOption shutdownOption) {
        log.info("shutdownServer(), option=" + shutdownOption);

        if (shutdownOption == ServerShutdownOption.FORCEIBLE) {
            stop();
        } else if (shutdownOption == ServerShutdownOption.GRACEFUL_WAIT) {
            waitForGracefulShutdown();
            stop();
        } else {// by default is ServerShutdownOption.GRACEFUL
            if (!waitingForGracefulShutdown) {
                final ProgramEntrance appMain = this;
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            appMain.waitForGracefulShutdown();
                            appMain.stop();

                        } catch (Exception e) {
                            log.error(e);
                        }
                    }
                };
                thread.setName("WaitForGracefulShutdownThread");
                thread.start();
            }
        }
    }

    public void waitForGracefulShutdown() {
        log.info("waitingForGracefulShutdown");
        waitingForGracefulShutdown = true;

        if (httpServer != null) {
            try {
                httpServer.stopServer();
                log.info("stoped httpServer");
            } catch (Exception e) {
                log.error("关闭Jetty server发生异常", e);
            }
        }
    }

    public synchronized void stop() {
        this.notify();
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
