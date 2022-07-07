package kitchenpos.ordertable.validator;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.exception.IllegalOrderLineItemException;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.exception.IllegalOrderTableException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidatorImpl implements OrderValidator {
    private final TableService tableService;
    private final MenuService menuService;

    public static final String ERROR_ORDER_TABLE_EMPTY = "주문테이블은 비어있을 수 없습니다.";
    public static final String ERROR_ORDER_LINE_ITEM_TOO_SMALL = "주문항목 개수는 %d 이하일 수 없습니다.";
    public static final String ERROR_ORDER_LINE_ITEM_INVALID_MENU = "주문항목에 잘못된 메뉴가 포함되어 있습니다.";
    public static final int MINIMUM_ORDER_LINE_ITEM_NUMBER = 0;

    public OrderValidatorImpl(TableService tableService, MenuService menuService) {
        this.tableService = tableService;
        this.menuService = menuService;
    }

    @Override
    public void validate(Order order){
        validateOrderLineItems(order);
        validateOrderTableEmpty(order);
    }

    private void validateOrderLineItems(Order order) {
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        validateOrderLineItemsSize(orderLineItems);
        validateOrderLineItemsMenu(orderLineItems);
    }

    private void validateOrderTableEmpty(Order order) {
        OrderTable orderTable = tableService.findOrderTableById(order.getOrderTableId());
        if (orderTable.isEmpty()) {
            throw new IllegalOrderTableException(ERROR_ORDER_TABLE_EMPTY);
        }
    }

    private void validateOrderLineItemsSize( List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null || orderLineItems.size() <= MINIMUM_ORDER_LINE_ITEM_NUMBER) {
            throw new IllegalOrderLineItemException(
                    String.format(ERROR_ORDER_LINE_ITEM_TOO_SMALL, MINIMUM_ORDER_LINE_ITEM_NUMBER)
            );
        }
    }

    private void validateOrderLineItemsMenu(List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                .map(orderLineItem -> orderLineItem.getMenuId())
                .collect(Collectors.toList());
        List<Menu> menus = menuService.findMenusByIdList(menuIds);
        if (menuIds.size() != menus.size()) {
            throw new IllegalOrderLineItemException(ERROR_ORDER_LINE_ITEM_INVALID_MENU);
        }
    }
}
