package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.dto.CreateOrderRequest;
import kitchenpos.order.dto.CreateOrderTableItemRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderCreationValidator {

    public static final String EMPTY_ORDER_TABLE_ERROR_MESSAGE = "주문 테이블이 비어있는 경우 주문을 생성할 수 없습니다.";
    public static final String NOT_FOUND_ORDER_TABLE_ERROR_MESSAGE = "존재하지 않는 주문 테이블 입니다.";
    public static final String NOT_EXIST_MENU_CONTAINS_ERROR_MESSAGE = "존재하지 않는 메뉴입니다.";
    public static final String EMPTY_ORDER_LINE_ITEM_ERROR_MESSAGE = "주문 항목이 없는 경우 주문을 생성할 수 없습니다.";

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderCreationValidator(
        MenuRepository menuRepository,
        OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(final CreateOrderRequest createOrderRequest) {
        validateExistsMenus(createOrderRequest.getOrderLineItemRequests());
        validateExistOrderTable(createOrderRequest.getOrderTableId());
    }

    private void validateExistOrderTable(final Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_ORDER_TABLE_ERROR_MESSAGE));
        validateOrderTableIsEmpty(orderTable);
    }

    private void validateOrderTableIsEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_ORDER_TABLE_ERROR_MESSAGE);
        }
    }

    private void validateExistsMenus(List<CreateOrderTableItemRequest> orderLineItemRequests) {
        validateOrderLineItemIsEmpty(orderLineItemRequests);
        List<Menu> findMenus = menuRepository.findAllById(extractMenuIds(orderLineItemRequests));
        if (findMenus.size() != orderLineItemRequests.size()) {
            throw new IllegalArgumentException(NOT_EXIST_MENU_CONTAINS_ERROR_MESSAGE);
        }
    }

    private void validateOrderLineItemIsEmpty(List<CreateOrderTableItemRequest> createOrderTableItemRequests) {
        if (createOrderTableItemRequests == null || createOrderTableItemRequests.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_ORDER_LINE_ITEM_ERROR_MESSAGE);
        }
    }

    private List<Long> extractMenuIds(List<CreateOrderTableItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
            .map(CreateOrderTableItemRequest::getMenuId)
            .collect(Collectors.toList());
    }
}
