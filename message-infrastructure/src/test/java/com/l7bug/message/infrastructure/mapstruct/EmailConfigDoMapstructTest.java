package com.l7bug.message.infrastructure.mapstruct;

import com.l7bug.message.domain.email.EmailConfig;
import com.l7bug.message.domain.email.EmailConfigGateway;
import com.l7bug.message.domain.email.EmailType;
import com.l7bug.message.infrastructure.dao.dataobject.EmailConfigDo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

/**
 * EmailConfigDoMapstruct 单元测试
 * 测试数据对象与领域对象之间的映射功能
 */
@ExtendWith(MockitoExtension.class)
class EmailConfigDoMapstructTest {

	@Mock
	private EmailConfigGateway emailConfigGateway;

	@InjectMocks
	private EmailConfigDoMapstruct emailConfigDoMapstruct = new EmailConfigDoMapstructImpl();

	@Test
	void testMapDo() {
		// 准备测试数据
		EmailConfig emailConfig = new EmailConfig(emailConfigGateway);
		emailConfig.setId(1L);
		emailConfig.setType(EmailType.WX_WORK);  // 使用实际存在的枚举值
		emailConfig.setUsername("test@example.com");
		emailConfig.setPassword("password123");
		emailConfig.setConnection(true);

		// 执行映射
		EmailConfigDo result = emailConfigDoMapstruct.mapDo(emailConfig);

		// 验证结果
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getType()).isEqualTo("WX_WORK");  // 枚举值转换为字符串
		assertThat(result.getUsername()).isEqualTo("test@example.com");
		assertThat(result.getPassword()).isEqualTo("password123");
		assertThat(result.getConnection()).isTrue();
	}

	@Test
	void testMapDomain() {
		// 准备测试数据
		EmailConfigDo emailConfigDo = new EmailConfigDo();
		emailConfigDo.setId(1L);
		emailConfigDo.setType("WX_WORK");  // 使用实际存在的枚举名称
		emailConfigDo.setUsername("test@example.com");
		emailConfigDo.setPassword("password123");
		emailConfigDo.setConnection(true);

		// 执行映射
		EmailConfig result = emailConfigDoMapstruct.mapDomain(emailConfigDo);

		// 验证结果
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getType()).isEqualTo(EmailType.WX_WORK);  // 字符串转换为枚举值
		assertThat(result.getUsername()).isEqualTo("test@example.com");
		assertThat(result.getPassword()).isEqualTo("password123");
		assertThat(result.getConnection()).isTrue();

		// 由于emailConfigGateway是私有的，我们不能直接访问它，但可以通过测试其功能来验证
		// 例如，我们可以验证testConnection方法是否可以正常工作，这表明gateway已正确注入
		boolean connectionResult = result.testConnection();
		verify(emailConfigGateway, times(1)).testConnection(any(EmailConfig.class));
	}

	@Test
	void testCreateDomain() {
		// 执行创建
		EmailConfig result = emailConfigDoMapstruct.createDomain();

		// 验证结果
		assertThat(result).isNotNull();
		// 由于gateway是私有的，我们不能直接验证，但可以验证testConnection方法是否调用
		assertThatNoException().isThrownBy(() -> result.testConnection());
	}
}
