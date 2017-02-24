package com.ec.log.all.test;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.ec.log.all.service.StatisticalImpl;

/**
 * @ClassName: IDiscussionRepositoryTest
 * @Description: 测试
 * @author longqingping
 * @date 2015年8月10日 上午11:57:21
 */
public class IDiscussionRepositoryTest {

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

    @Test
    public void startFullSync() throws CloneNotSupportedException {
        long start = System.currentTimeMillis();
        StatisticalImpl add = ctx.getBean(StatisticalImpl.class);
        add.startFullSync();
        long end = System.currentTimeMillis();
        System.out.println("运行时间：" + (end - start) + "毫秒");// 26011毫秒,任务队列已满两次
                                                           // //25616毫秒 任务队列已满两次
                                                           // //24640毫秒 无

    }

}