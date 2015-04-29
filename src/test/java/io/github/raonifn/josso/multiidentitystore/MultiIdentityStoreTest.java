package io.github.raonifn.josso.multiidentitystore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.josso.gateway.identity.exceptions.SSOIdentityException;
import org.josso.gateway.identity.service.store.SimpleUserKey;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MultiIdentityStoreTest {

	private MultiIdentityStore bean;

	@Before
	public void before() {
		ApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext-test.xml");
		bean = (MultiIdentityStore) context.getBean("bla", MultiIdentityStore.class);
	}

	@Test
	public void testBean() {
		assertEquals(2, bean.getInternalStores().size());
	}

	@Test
	public void testUserExists() throws SSOIdentityException {
		ApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext-test.xml");

		MultiIdentityStore bean = (MultiIdentityStore) context.getBean("bla", MultiIdentityStore.class);

		assertTrue(bean.userExists(new SimpleUserKey("user1")));
		assertTrue(bean.userExists(new SimpleUserKey("user2")));
		assertFalse(bean.userExists(new SimpleUserKey("user3")));
	}
}
