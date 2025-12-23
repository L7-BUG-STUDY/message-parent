package com.l7bug.message.infrastructure.dao;

import com.l7bug.database.config.DataBaseAutoConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * CurrentUserIdImpl
 *
 * @author Administrator
 * @since 2025/11/10 14:41
 */
@AllArgsConstructor
@Component
public class CurrentUserIdImpl implements DataBaseAutoConfiguration.CurrentUserId {
	@Override
	public Long getCurrentUserId() {
		return -1L;
	}
}
