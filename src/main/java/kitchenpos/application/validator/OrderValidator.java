package kitchenpos.application.validator;

import static kitchenpos.exception.ErrorCode.ALREADY_COMPLETION_STATUS;
import static kitchenpos.exception.ErrorCode.CAN_NOT_ORDER;
import static kitchenpos.exception.ErrorCode.NOT_EXISTS_ORDER_LINE_ITEMS;
import static kitchenpos.exception.ErrorCode.NOT_SAME_BETWEEN_ORDER_LINE_ITEMS_AND_MENU_COUNT;

import java.util.List;
import java.util.Objects;
import kitchenpos.application.TableService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.KitchenposException;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final TableService tableService;

    public OrderValidator(final MenuRepository menuRepository, final TableService tableService) {
        this.menuRepository = menuRepository;
        this.tableService = tableService;
    }

    public void validateCreate(Order order) {
        validateNullOrderLineItems(order.getOrderLineItems());
        validateEmptyTrue(tableService.findById(order.getOrderTableId()));
        validateOrderLineItems(order.getOrderLineItemsSize(), menuRepository.countByIdIn(order.getMenuIds()));
    }

    private void validateNullOrderLineItems(List<OrderLineItem> orderLineItems) {
        if(CollectionUtils.isEmpty(orderLineItems)){
            throw new KitchenposException(NOT_EXISTS_ORDER_LINE_ITEMS);
        }
    }

    private void validateEmptyTrue(OrderTable orderTable){
        if (orderTable.isEmpty()) {
            throw new KitchenposException(CAN_NOT_ORDER);
        }
    }

    private void validateOrderLineItems(int orderLineItemsSize, int menuCount){
        if (orderLineItemsSize != menuCount) {
            throw new KitchenposException(NOT_SAME_BETWEEN_ORDER_LINE_ITEMS_AND_MENU_COUNT);
        }
    }

    public static void isCompletionOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, orderStatus)) {
            throw new KitchenposException(ALREADY_COMPLETION_STATUS);
        }
    }
}
