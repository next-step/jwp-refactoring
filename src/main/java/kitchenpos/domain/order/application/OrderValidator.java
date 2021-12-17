package kitchenpos.domain.order.application;

import kitchenpos.domain.menu.domain.MenuRepository;
import kitchenpos.domain.order.domain.EmptyTableValidatedEvent;
import kitchenpos.exception.BusinessException;
import kitchenpos.exception.ErrorCode;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderValidator(
            final MenuRepository menuRepository,
            final ApplicationEventPublisher eventPublisher
    ) {
        this.menuRepository = menuRepository;
        this.eventPublisher = eventPublisher;
    }

    public void validateEmptyTable(Long orderTableId) {
        eventPublisher.publishEvent(new EmptyTableValidatedEvent(orderTableId));
    }

    public void validateMenus(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            throw new BusinessException(ErrorCode.EMPTY_MENUES);
        }

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new BusinessException(ErrorCode.MENU_NOT_EXIST);
        }
    }
}
