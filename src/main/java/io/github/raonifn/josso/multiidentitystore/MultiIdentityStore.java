package io.github.raonifn.josso.multiidentitystore;

import java.util.List;

import org.josso.auth.Credential;
import org.josso.auth.CredentialKey;
import org.josso.auth.CredentialProvider;
import org.josso.auth.CredentialStore;
import org.josso.gateway.identity.exceptions.NoSuchUserException;
import org.josso.gateway.identity.exceptions.SSOIdentityException;
import org.josso.gateway.identity.service.BaseRole;
import org.josso.gateway.identity.service.BaseUser;
import org.josso.gateway.identity.service.store.AbstractStore;
import org.josso.gateway.identity.service.store.IdentityStore;
import org.josso.gateway.identity.service.store.UserKey;

public class MultiIdentityStore extends AbstractStore {

	private List<AbstractStore> internalStores;

	public MultiIdentityStore() {
	}

	public MultiIdentityStore(List<AbstractStore> internalStores) {
		this.internalStores = internalStores;
	}

	public List<AbstractStore> getInternalStores() {
		return internalStores;
	}

	public void setInternalStores(List<AbstractStore> internalStores) {
		this.internalStores = internalStores;
	}

	public BaseRole[] findRolesByUserKey(UserKey userKey) throws SSOIdentityException {
		for (IdentityStore store : internalStores) {
			BaseRole[] ret = store.findRolesByUserKey(userKey);
			if (ret != null && ret.length > 0) {
				return ret;
			}
		}
		return new BaseRole[0];
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

		throw new NoSuchUserException(userKey);
	}

	public Credential[] loadCredentials(CredentialKey credentialKey, CredentialProvider provider) throws SSOIdentityException {
		for (CredentialStore store : internalStores) {
			Credential[] ret = store.loadCredentials(credentialKey, provider);
			if (ret != null) {
				return ret;
			}
		}
		return null;
	}

	public String loadUID(CredentialKey credentialKey, CredentialProvider provider) throws SSOIdentityException {
		for (CredentialStore store : internalStores) {
			String ret = store.loadUID(credentialKey, provider);
			if (ret != null) {
				return ret;
			}
		}
		return null;
	}

}
