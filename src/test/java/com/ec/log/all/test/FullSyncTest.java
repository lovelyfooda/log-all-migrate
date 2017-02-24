/**
 * @Package com.ec.log.all.test
 * @Description: TODO
 * @author Administrator
 * @date 2016年3月31日 下午7:33:29
 */
package com.ec.log.all.test;

import java.lang.reflect.InvocationTargetException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.ec.log.all.service.IStatistical;

/**
 * @ClassName: FullSyncTest
 * @Description: TODO
 * @author Administrator
 * @date 2016年3月31日 下午7:33:29
 */
public class FullSyncTest {
    private static GenericXmlApplicationContext ctx;
    private IStatistical iStatistical;

    @Before
    public void before() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ctx = new GenericXmlApplicationContext();
        ctx.load("classpath*:/spring/applicationContext.xml");
        ctx.refresh();
    }

    @After
    public void after() {
        ctx.close();
    }

    @Test
    public void testStartFullSync() {
        iStatistical = ctx.getBean(IStatistical.class);
        iStatistical.startFullSync();
    }
}
