package kitchenpos.order.validator;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static kitchenpos.common.Messages.*;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateCreateOrder(OrderRequest orderRequest) {
        validateOrderTable(orderRequest.getOrderTableId());
        validateOrderLineItems(orderRequest.getOrderLineItems());
        validateMenu(orderRequest.getOrderLineItems());
    }

    private void validateOrderTable(Long orderTableId) {
        if (!orderTableRepository.existsById(orderTableId)) {
            throw new NoSuchElementException(ORDER_TABLE_NOT_EXISTS);
        }
    }

    private void validateOrderLineItems(List<OrderLineItemRequest> orderLineItemRequest) {
        if (CollectionUtils.isEmpty(orderLineItemRequest)) {
            throw new IllegalArgumentException(ORDER_LINE_ITEM_REQUIRED);
        }
    }

    private void validateMenu(List<OrderLineItemRequest> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        List<Menu> menus = menuRepository.findByIdIn(menuIds);

        if (menus.size() != menuIds.size()) {
            throw new NoSuchElementException(MENU_FIND_IN_NO_SUCH);
        }
    }

    public void validateChangeOrderStatus(Order order) {
        if (order.getOrderStatus().equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException(ORDER_STATUS_CHANGE_CANNOT_COMPLETION);
        }
    }
}
