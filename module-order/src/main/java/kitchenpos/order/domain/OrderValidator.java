package kitchenpos.order.domain;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository,
                          OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Order createValidation(OrderRequest orderRequest) {
        validateOrderItemSize(orderRequest);
        OrderTable orderTable = orderTableRepository.findByIdAndEmptyIsFalse(orderRequest.getOrderTableId())
                .orElseThrow(() -> new NoSuchElementException("주문이 가능한 테이블이 아닙니다"));

        final List<OrderLineItem> orderLineItems = validateExistMenuId(orderRequest.getOrderLineItems());

        return Order.createOrder(orderTable.getId(), OrderLineItems.from(orderLineItems));
    }

    private void validateOrderItemSize(OrderRequest orderRequest) {
        if (ObjectUtils.isEmpty(orderRequest.getOrderLineItems())|| orderRequest.getOrderLineItems().isEmpty()) {
            throw new IllegalArgumentException("주문항목은 있어야 합니다.");
        }
    }

    private List<OrderLineItem> validateExistMenuId(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(mapToOrderLineItem())
                .collect(Collectors.toList());
    }

    private Function<OrderLineItemRequest, OrderLineItem> mapToOrderLineItem() {
        return orderLineItemRequest -> {
            final Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(() -> new NoSuchElementException("요청한 메뉴가 존재하지 않습니다."));
            return OrderLineItem.of(menu.getId(), menu.getPrice().value(), menu.getName()
                    , orderLineItemRequest.getQuantity());
        };
    }

}
