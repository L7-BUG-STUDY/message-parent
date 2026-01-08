package com.l7bug.message.infrastructure.gateway;

import com.l7bug.message.domain.email.sync.EmailSyncState;
import com.l7bug.message.domain.email.sync.EmailSyncStateGateway;
import com.l7bug.message.infrastructure.dao.dataobject.EmailSyncStateDo;
import com.l7bug.message.infrastructure.dao.repository.EmailSyncStateRepository;
import com.l7bug.message.infrastructure.mapstruct.EmailSyncStateDoMapstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * EmailSyncStateGatewayImpl
 *
 * @author Administrator
 * @since 2026/1/8 18:05
 */
@AllArgsConstructor
@Component
public class EmailSyncStateGatewayImpl implements EmailSyncStateGateway {
	private final EmailSyncStateRepository emailSyncStateRepository;
	private final EmailSyncStateDoMapstruct emailSyncStateDoMapstruct;

	@Override
	public boolean save(EmailSyncState save) {
		EmailSyncStateDo entity = emailSyncStateDoMapstruct.mapDo(save);
		emailSyncStateRepository.save(entity);
		save.setId(entity.getId());
		return true;
	}

	@Override
	public Optional<EmailSyncState> findLast(String username, String folder, Long uidValidity) {
		return this.emailSyncStateRepository
			.findFirstByUsernameAndFolderAndUidValidityOrderById(username, folder, uidValidity)
			.map(emailSyncStateDoMapstruct::mapDomain);
	}
}
