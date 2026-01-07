package com.l7bug.message.infrastructure.dao.repository;

import com.l7bug.message.infrastructure.dao.dataobject.EmailRecordDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * EmailRecordRepostionry
 *
 * @author Administrator
 * @since 2026/1/7 11:46
 */
@Repository
public interface EmailRecordRepository extends JpaRepository<EmailRecordDo, Long> {
}
