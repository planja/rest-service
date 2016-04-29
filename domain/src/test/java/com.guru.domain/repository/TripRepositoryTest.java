package com.guru.domain.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.guru.domain.config.TestDataConfig;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestDataConfig.class)
//@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
//@DatabaseSetup("classpath:TripRepoTest.xml")
public class TripRepositoryTest {

    @Test
    public void test() {
        Assert.assertTrue(true);
    }


}
