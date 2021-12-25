package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTableValidateEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidator {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final MenuClient menuClient;

    public OrderValidator(ApplicationEventPublisher applicationEventPublisher, MenuClient menuClient) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.menuClient = menuClient;
    }

    public void validateCreateOrder(Order order) {
        checkUsableOrderTable(order.getOrderTableId());
        validateOrderItemRequests(order.getOrderLineItems());
    }

    private void checkUsableOrderTable(Long orderTableId) {
        applicationEventPublisher.publishEvent(OrderTableValidateEvent.of(orderTableId));
    }

    private void validateOrderItemRequests(List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (!menuClient.isExistMenuByIds(menuIds)) {
            throw new IllegalArgumentException("메뉴 검증에 실패하였습니다.");
        }
    }
}
