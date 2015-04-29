package io.github.raonifn.josso.multiidentitystore;

import java.util.List;

import org.josso.gateway.identity.exceptions.NoSuchUserException;
import org.josso.gateway.identity.exceptions.SSOIdentityException;
import org.josso.gateway.identity.service.BaseRole;
import org.josso.gateway.identity.service.BaseUser;
import org.josso.gateway.identity.service.store.IdentityStore;
import org.josso.gateway.identity.service.store.UserKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiIdentityStore implements IdentityStore {

	private static final Logger LOGGER = LoggerFactory.getLogger(MultiIdentityStore.class);

	private List<IdentityStore> internalStores;

	public MultiIdentityStore() {
	}

	public MultiIdentityStore(List<IdentityStore> internalStores) {
		this.internalStores = internalStores;
	}

	public List<IdentityStore> getInternalStores() {
		return internalStores;
	}

	public void setInternalStores(List<IdentityStore> internalStores) {
		this.internalStores = internalStores;
	}

	public BaseRole[] findRolesByUserKey(UserKey userKey) throws SSOIdentityException {
		for (IdentityStore store : internalStores) {
			BaseRole[] ret = store.findRolesByUserKey(userKey);
			if (ret != null) {
				return ret;
			}
		}
		return null;
	}

	public BaseUser loadUser(UserKey userKey) throws NoSuchUserException, SSOIdentityException {
		for (IdentityStore store : internalStores) {
			try {
				BaseUser ret = store.loadUser(userKey);
				return ret;
			} catch (NoSuchUserException ex) {
				// next
			}
		}
		return null;
	}

	public boolean userExists(UserKey userKey) throws SSOIdentityException {
		for (IdentityStore store : internalStores) {
			boolean ret = store.userExists(userKey);
			if (ret) {
				return ret;
			}
		}
		return false;
	}

}
