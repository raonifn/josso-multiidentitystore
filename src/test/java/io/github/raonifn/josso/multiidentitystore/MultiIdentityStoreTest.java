package io.github.raonifn.josso.multiidentitystore;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MultiIdentityStoreTest {

	@Test
	public void testBeanGetsCreated() {
		ApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext-test.xml");

		MultiIdentityStore bean = (MultiIdentityStore) context.getBean("bla", MultiIdentityStore.class);
		assertEquals(1, bean.getInternalStores().size());
	}
}
