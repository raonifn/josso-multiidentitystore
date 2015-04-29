package io.github.raonifn.josso.multiidentitystore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.josso.auth.Credential;
import org.josso.auth.scheme.UsernameCredential;
import org.josso.auth.scheme.UsernamePasswordCredentialProvider;
import org.josso.gateway.identity.exceptions.NoSuchUserException;
import org.josso.gateway.identity.exceptions.SSOIdentityException;
import org.josso.gateway.identity.service.BaseRole;
import org.josso.gateway.identity.service.store.SimpleUserKey;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MultiIdentityStoreTest {

	private MultiIdentityStore bean;

	@Before
	public void before() {
		ApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext-test.xml");
		bean = (MultiIdentityStore) context.getBean("multi", MultiIdentityStore.class);
	}

	@Test
	public void testBean() {
		assertEquals(2, bean.getInternalStores().size());
	}

	@Test
	public void testUserExists() throws SSOIdentityException {
		assertTrue(bean.userExists(new SimpleUserKey("user1")));
		assertTrue(bean.userExists(new SimpleUserKey("user2")));
		assertFalse(bean.userExists(new SimpleUserKey("user3")));
	}

	@Test
	public void testLoadUser() throws SSOIdentityException {
		assertEquals("user1", bean.loadUser(new SimpleUserKey("user1")).getName());
		assertEquals("user2", bean.loadUser(new SimpleUserKey("user2")).getName());
	}

	@Test(expected = NoSuchUserException.class)
	public void testLoadUserThatDontExists() throws SSOIdentityException {
		bean.loadUser(new SimpleUserKey("user3"));
	}

	@Test
	public void testFindRolesByUserKey() throws SSOIdentityException {
		testFindRolesByUserKey("user1", "role1", "role2");
		testFindRolesByUserKey("user2", "role2");
	}

	@Test
	@Ignore
	public void test() throws SSOIdentityException {
		UsernamePasswordCredentialProvider provider = new UsernamePasswordCredentialProvider();
		SimpleUserKey user = new SimpleUserKey("user2");
		bean.loadUser(user);
		bean.loadUID(user, provider);
		Credential[] credentials = bean.loadCredentials(user, provider);
		System.out.println(Arrays.asList(credentials));
	}

	private void testFindRolesByUserKey(String userId, String... expectedRoles) throws SSOIdentityException {
		SimpleUserKey user = new SimpleUserKey(userId);
		bean.loadUser(user);
		BaseRole[] roles = bean.findRolesByUserKey(user);
		assertRoles(roles, expectedRoles);
	}

	private void assertRoles(BaseRole[] actual, String... expected) {
		assertEquals(expected.length, actual.length);

		List<String> roles = Arrays.asList(expected);
		for (BaseRole role : actual) {
			assertTrue(roles.contains(role.getName()));
		}
	}
}
