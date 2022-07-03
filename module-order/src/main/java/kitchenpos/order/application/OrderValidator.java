package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.CannotCreateException;
import kitchenpos.common.exception.ExceptionType;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.request.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private final MenuService menuService;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuService menuService, OrderTableRepository orderTableRepository) {
        this.menuService = menuService;
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

        if (orderLineItemRequests.size() != menuService.countByIdIn(menuIds)) {
            throw new CannotCreateException(ExceptionType.CONTAINS_NOT_EXIST_MENU);
        }
    }

    private void validateExistOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_EXIST_ORDER_TABLE));

        if (orderTable.isEmpty()) {
            throw new BadRequestException(ExceptionType.EMPTY_TABLE);
        }
    }
}
