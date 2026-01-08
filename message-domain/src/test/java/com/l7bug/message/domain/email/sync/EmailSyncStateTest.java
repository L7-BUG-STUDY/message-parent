package com.l7bug.message.domain.email.sync;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailSyncStateTest {
	@Test
	void test() {
		EmailSyncState emailSyncState = new EmailSyncState(Mockito.mock());
		emailSyncState.setUsername("username");
		emailSyncState.setFolder("folder");
		emailSyncState.setUidValidity(1L);
		emailSyncState.setUid(1L);
	}
}
