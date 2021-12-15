package kitchenpos.domain.order.application;

import kitchenpos.domain.menu.domain.MenuRepository;
import kitchenpos.domain.order.domain.EmptyTableValidatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final TableTranslator tableTranslator;
    private final ApplicationEventPublisher eventPublisher;

    public OrderValidator(
            final MenuRepository menuRepository,
            final TableTranslator tableTranslator,
            final ApplicationEventPublisher eventPublisher
    ) {
        this.menuRepository = menuRepository;
        this.tableTranslator = tableTranslator;
        this.eventPublisher = eventPublisher;
    }

    public void validateEmptyTable(Long orderTableId) {
        eventPublisher.publishEvent(new EmptyTableValidatedEvent(orderTableId));
    }

    public void validateMenus(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            throw new IllegalArgumentException();
        }

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }
}
