/**
 * @Package com.ec.log.service.test
 * @Description: TODO
 * @author Administrator
 * @date 2016年4月6日 下午4:10:15
 */
package com.ec.log.service.test;

import org.junit.After;
import org.junit.BeforeClass;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @ClassName: MonitoringServiceImplTest
 * @Description: TODO
 * @author Administrator
 * @date 2016年4月6日 下午4:10:15
 */
public class MonitoringServiceImplTest {
    private static GenericXmlApplicationContext ctx = null;

    @BeforeClass
    public static void before() {
        ctx = new GenericXmlApplicationContext();
        ctx.load("classpath*:/spring/applicationContext.xml");
        ctx.refresh();
    }

    @After
    public void after() {
        ctx.close();
    }
}
