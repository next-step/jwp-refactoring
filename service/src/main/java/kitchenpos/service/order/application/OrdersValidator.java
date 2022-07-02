package kitchenpos.service.order.application;

import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.service.order.dto.OrderLineItemRequest;
import kitchenpos.service.order.dto.OrdersRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class OrdersValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrdersValidator(
            MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(OrdersRequest request) {
        if (CollectionUtils.isEmpty(request.getOrderLineItems())) {
            throw new IllegalArgumentException("주문 항목은 반드시 존재해야 합니다.");
        }

        if (request.getOrderLineItems().size() != menuRepository.countByIdIn(
                request.getOrderLineItems().stream().map(OrderLineItemRequest::getMenuId)
                        .collect(Collectors.toList()))) {
            throw new IllegalArgumentException("주문 항목 갯수가 적절하지 않습니다.");
        }

        final OrderTable orderTable =
                orderTableRepository.findByIdAndEmptyIsFalse(request.getOrderTableId()).orElseThrow(NoSuchElementException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있으면 주문할 수 없습니다.");
        }
    }
}
