package kitchenpos.order.application.validator;

import static kitchenpos.common.exception.ErrorCode.ALREADY_COMPLETION_STATUS;
import static kitchenpos.common.exception.ErrorCode.CAN_NOT_ORDER;
import static kitchenpos.common.exception.ErrorCode.NOT_EXISTS_ORDER_LINE_ITEMS;
import static kitchenpos.common.exception.ErrorCode.NOT_SAME_BETWEEN_ORDER_LINE_ITEMS_AND_MENU_COUNT;

import java.util.List;
import java.util.Objects;
import kitchenpos.common.exception.KitchenposException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.request.OrderRequest;
import kitchenpos.table.application.TableService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {
    private final TableService tableService;
    private final MenuRepository menuRepository;

    public OrderValidator(
            final TableService tableService,
            final MenuRepository menuRepository
    ) {
        this.tableService = tableService;
        this.menuRepository = menuRepository;
    }

    public void validateCreate(OrderRequest orderRequest) {
        validateNullOrderLineItems(orderRequest.getOrderLineItems());
        validateEmptyTrue(orderRequest.getOrderTableId());
        validateOrderLineItems(orderRequest.getOrderLineItemsSize(), orderRequest.getMenuIds());
    }

    private void validateNullOrderLineItems(List<OrderLineItem> orderLineItems) {
        if(CollectionUtils.isEmpty(orderLineItems)){
            throw new KitchenposException(NOT_EXISTS_ORDER_LINE_ITEMS);
        }
    }

    private void validateEmptyTrue(Long orderTableId){
        if (tableService.findById(orderTableId).isEmpty()) {
            throw new KitchenposException(CAN_NOT_ORDER);
        }
    }

    private void validateOrderLineItems(int orderLineItemsSize, List<Long> menuIds) {
        if (orderLineItemsSize != getMenuCount(menuIds)) {
            throw new KitchenposException(NOT_SAME_BETWEEN_ORDER_LINE_ITEMS_AND_MENU_COUNT);
        }
    }

    private int getMenuCount(List<Long> menuIds){
        return menuRepository.countByIdIn(menuIds);
    }

    public static void isCompletionOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, orderStatus)) {
            throw new KitchenposException(ALREADY_COMPLETION_STATUS);
        }
    }

}
