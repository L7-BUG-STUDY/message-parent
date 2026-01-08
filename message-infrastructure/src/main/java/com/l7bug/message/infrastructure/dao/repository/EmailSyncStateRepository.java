package com.l7bug.message.infrastructure.dao.repository;

import com.l7bug.message.infrastructure.dao.dataobject.EmailSyncStateDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * EmailSyncStateRepository
 *
 * @author Administrator
 * @since 2026/1/8 17:53
 */
@Repository
public interface EmailSyncStateRepository extends JpaRepository<EmailSyncStateDo, Long> {
	Optional<EmailSyncStateDo> findFirstByUsernameAndFolderAndUidValidityOrderById(String username, String folder, Long uidValidity);
}
