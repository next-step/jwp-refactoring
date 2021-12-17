package kitchenpos.domain.order.application;

import kitchenpos.domain.order.domain.EmptyTableValidatedEvent;
import kitchenpos.exception.BusinessException;
import kitchenpos.exception.ErrorCode;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class OrderValidator {

    private final MenuClient menuClient;
    private final ApplicationEventPublisher eventPublisher;

    public OrderValidator(
            final MenuClient menuClient,
            final ApplicationEventPublisher eventPublisher
    ) {
        this.menuClient = menuClient;
        this.eventPublisher = eventPublisher;
    }

    public void validateEmptyTable(Long orderTableId) {
        eventPublisher.publishEvent(new EmptyTableValidatedEvent(orderTableId));
    }

    public void validateMenus(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            throw new BusinessException(ErrorCode.EMPTY_MENUES);
        }
        menuClient.validateMenuExist(menuIds);
    }
}
