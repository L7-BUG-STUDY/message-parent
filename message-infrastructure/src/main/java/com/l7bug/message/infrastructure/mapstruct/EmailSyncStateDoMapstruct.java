package com.l7bug.message.infrastructure.mapstruct;

import com.l7bug.message.domain.email.sync.EmailSyncState;
import com.l7bug.message.domain.email.sync.EmailSyncStateGateway;
import com.l7bug.message.infrastructure.dao.dataobject.EmailSyncStateDo;
import jakarta.annotation.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.context.ApplicationContext;

/**
 * EmailConfigDoMapstruct
 * <p>
 * 邮件配置数据对象与领域对象映射器
 * 该映射器负责在基础设施层的数据对象(EmailConfigDo)和领域层的领域对象(EmailConfig)之间进行转换
 *
 * @author Administrator
 * @since 2025/12/23 15:44
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class EmailSyncStateDoMapstruct {
	@Resource
	private ApplicationContext applicationContext;

	/**
	 * 创建领域对象实例
	 * 通过注入的EmailConfigGateway创建一个新的EmailConfig领域对象
	 *
	 * @return EmailConfig领域对象实例
	 */
	public EmailSyncState createDomain() {
		return new EmailSyncState(applicationContext.getBean(EmailSyncStateGateway.class));
	}

	/**
	 * 将领域对象映射为数据对象
	 * 将EmailConfig领域对象转换为EmailConfigDo数据对象，用于持久化存储
	 *
	 * @param domain 邮件配置领域对象
	 * @return EmailConfigDo数据对象
	 */
	public abstract EmailSyncStateDo mapDo(EmailSyncState domain);

	/**
	 * 将数据对象映射为领域对象
	 * 将EmailConfigDo数据对象转换为EmailConfig领域对象，用于业务逻辑处理
	 *
	 * @param entity 邮件配置数据对象
	 * @return EmailConfig领域对象
	 */
	public abstract EmailSyncState mapDomain(EmailSyncStateDo entity);
}
