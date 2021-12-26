package kitchenpos.order.application;

import kitchenpos.common.exception.OrderTableEmptyException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;
    private final TableService tableService;

    public OrderValidator(
            OrderTableRepository orderTableRepository
            , MenuRepository menuRepository
            , TableService tableService
    ) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
        this.tableService = tableService;
    }

    public void validateCreate(Order order) {
        validateOrderTable(order.getOrderTableId());
        validateOrderLineItems(order.getOrderLineItems());
    }

    public void validateChangeOrderStatus(Order order) {
        if (order.getOrderStatus().isCompletion()) {
            throw new IllegalArgumentException("주문 상태가 완료인 경우 주문 상태를 수정할 수 없습니다.");
        }
    }

    private void validateOrderTable(Long orderTableId) {
        validateOrderTableExists(orderTableId);
        validateOrderTableIsEmpty(orderTableId);
    }

    private void validateOrderLineItems(OrderLineItems orderLineItems) {
        validateOrderLineItemsSize(orderLineItems);
        validateOrderLineItemsMenuSize(orderLineItems);
    }

    private void validateOrderTableExists(Long orderTableId) {
        if (!orderTableRepository.existsById(orderTableId)) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다.");
        }
    }

    private void validateOrderTableIsEmpty(Long orderTableId) {
        OrderTable orderTable = tableService.findById(orderTableId);
        if (orderTable.getEmpty().isEmpty()) {
            throw new OrderTableEmptyException();
        }
    }

    private void validateOrderLineItemsSize(OrderLineItems orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems.getOrderLineItems())) {
            throw new IllegalArgumentException("주문 항목은 1개 이상이어야 합니다.");
        }
    }

    private void validateOrderLineItemsMenuSize(OrderLineItems orderLineItems) {
        List<Long> menuIds = orderLineItems.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        List<Menu> menus = menuRepository.findByIdIn(menuIds);
        if (menus.size() != menuIds.size()) {
            throw new IllegalArgumentException("메뉴 수가 일치하지 않습니다.");
        }
    }
}
