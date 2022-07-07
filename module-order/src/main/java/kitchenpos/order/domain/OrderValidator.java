package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuRepository;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator extends AbstractAggregateRoot<OrderValidator> {
    private final MenuRepository menuRepository;

    public OrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validate(Order order) {
        registerEvent(new OrderTableEmptyValidateEvent(order));

        if (CollectionUtils.isEmpty(order.getOrderLineItems().values())) {
            throw new IllegalArgumentException("주문 항목이 존재하지 않습니다.");
        }

        if (order.getOrderLineItems().getMenuIds().size() != getMenuCount(order)) {
            throw new IllegalArgumentException("등록된 메뉴와 요청 메뉴가 일치하지 않습니다.");
        }
    }

    private long getMenuCount(Order order) {
        return menuRepository.countByIdIn(order.getOrderLineItems().getMenuIds());
    }
}
