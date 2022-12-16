package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private static final String ERROR_MESSAGE_NOT_FOUND_ORDER_TABLE_FORMAT = "존재하지 않는 주문 테이블입니다. ID : %d";
    private static final String ERROR_MESSAGE_NOT_FOUND_MENU_FORMAT = "메뉴가 존재하지 않습니다. ID : %d";
    public static final String ERROR_MESSAGE_IS_EMPTY_ORDER_TABLE = "비어있는 테이블입니다.";
    public static final String ERROR_MESSAGE_NOT_EXIST_MENU = "등록되지 않은 메뉴가 있습니다.";

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public List<OrderLineItem> validate(OrderRequest request) {
        validateIsNotEmpty(validateEmptyOrderTable(request.getOrderTableId()));
        validateExistMenu(request.getOrderLineItems().stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList()));
        return toOrderLineItems(request.getOrderLineItems());
    }

    private OrderTable validateEmptyOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(ERROR_MESSAGE_NOT_FOUND_ORDER_TABLE_FORMAT, orderTableId)));
    }

    private void validateIsNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidParameterException(ERROR_MESSAGE_IS_EMPTY_ORDER_TABLE);
        }
    }

    private void validateExistMenu(List<Long> menuIds) {
        Long count = menuRepository.countAllByIdIn(menuIds);

        if (count != menuIds.size()) {
            throw new InvalidParameterException(ERROR_MESSAGE_NOT_EXIST_MENU);
        }
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> requests) {
        return requests.stream()
                .map(request -> OrderLineItem.of(findOrderMenu(request.getMenuId()), request.getQuantity()))
                .collect(Collectors.toList());
    }

    private OrderMenu findOrderMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_MENU_FORMAT, menuId)));
        return OrderMenu.of(menu);
    }
}
