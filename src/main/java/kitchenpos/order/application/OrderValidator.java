package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.core.exception.BadRequestException;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.core.exception.NotFoundException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.request.OrderLineItemRequest;
import kitchenpos.order.domain.request.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
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

    public void creatingValidate(OrderRequest orderRequest) {
        validateOrderLineExist(orderRequest.getOrderLineItemRequests());
        validateExistMenu(orderRequest.getOrderLineItemRequests());
        validateExistOrderTable(orderRequest.getOrderTableId());
    }

    private void validateOrderLineExist(List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new BadRequestException(ExceptionType.EMPTY_ORDER_LINE_ITEM);
        }
    }

    private void validateExistMenu(List<OrderLineItemRequest> orderLineItemRequests) {
        List<Long> menuIds = orderLineItemRequests.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new CannotCreateException(ExceptionType.CONTAINS_NOT_EXIST_MENU);
        }
    }

    private void validateExistOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_EXIST_ORDER_TABLE));

        orderTable.validateIsEmpty();
    }
}
