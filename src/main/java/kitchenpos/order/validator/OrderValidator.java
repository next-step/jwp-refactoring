package kitchenpos.order.validator;

import java.util.List;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(OrderRequest orderRequest) {
        validateOrderTable(orderRequest.getOrderTableId());
        validateOrderLineItem(orderRequest.getOrderLineItems());
    }

    private void validateOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 등록할 수 없습니다.");
        }
    }

    private void validateOrderLineItem(List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException("주문 항목이 1개 이상이어야 합니다.");
        }

        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));
        }
    }
}
