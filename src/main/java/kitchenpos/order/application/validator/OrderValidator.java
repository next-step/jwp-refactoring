package kitchenpos.order.application.validator;

import static kitchenpos.exception.ErrorCode.ALREADY_COMPLETION_STATUS;
import static kitchenpos.exception.ErrorCode.NOT_EXISTS_ORDER_LINE_ITEMS;
import static kitchenpos.exception.ErrorCode.NOT_SAME_BETWEEN_ORDER_LINE_ITEMS_AND_MENU_COUNT;

import java.util.List;
import java.util.Objects;
import kitchenpos.exception.KitchenposException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.request.OrderRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final TableEmptyValidator tableEmptyValidator;

    public OrderValidator(
            final MenuRepository menuRepository,
            final TableEmptyValidator tableEmptyValidator
    ) {
        this.menuRepository = menuRepository;
        this.tableEmptyValidator = tableEmptyValidator;
    }

    public void validateCreate(OrderRequest orderRequest) {
        validateNullOrderLineItems(orderRequest.getOrderLineItems());
        validateEmptyTrue(orderRequest.getOrderTableId());
        validateOrderLineItems(orderRequest.getOrderLineItemsSize(), menuRepository.countByIdIn(orderRequest.getMenuIds()));
    }

    private void validateNullOrderLineItems(List<OrderLineItem> orderLineItems) {
        if(CollectionUtils.isEmpty(orderLineItems)){
            throw new KitchenposException(NOT_EXISTS_ORDER_LINE_ITEMS);
        }
    }

    private void validateEmptyTrue(Long orderTableId){
        tableEmptyValidator.validateEmptyTrue(orderTableId);
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
