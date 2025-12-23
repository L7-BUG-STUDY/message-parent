package com.l7bug.message.infrastructure.dao.repository;

import com.l7bug.message.infrastructure.dao.dataobject.EmailConfigDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * EmailConfigRepository
 *
 * @author Administrator
 * @since 2025/12/23 14:58
 */
@Repository
public interface EmailConfigRepository extends JpaRepository<EmailConfigDo, Long> {
}
