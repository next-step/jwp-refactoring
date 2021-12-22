package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validateCreateOrder(OrderRequest orderRequest) {
        checkUsableOrderTable(orderRequest.getOrderTableId());
        validateOrderItemRequests(orderRequest.getOrderLineItems());
    }

    private void checkUsableOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderItemRequests(List<OrderLineItemRequest> orderLineItemRequests) {
        orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .forEach(this::checkExistMenu);
    }

    private void checkExistMenu(Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw new IllegalArgumentException("메뉴가 존재하지 않습니다.");
        }
    }
}
