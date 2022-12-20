package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;


    public OrderValidator(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validCreate(Long orderTableId, List<OrderLineItem> orderLineItems) {
        OrderTable orderTable = findOrderTableById(orderTableId);
        validOrderTable(orderTable);
        validOrderLineItems(orderLineItems);
        validMenu(mapToMenuIds(orderLineItems));
    }

    private List<Long> mapToMenuIds(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    private void validMenu(List<Long> menuIds) {
        menuIds.forEach(this::validMenuById);
    }

    private OrderTable findOrderTableById(long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));
    }

    private void validOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문테이블이 비어있습니다.");
        }
    }

    private void validOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("비어있는 주문항목은 등록할 수 없습니다.");
        }
    }

    private void validMenuById(long menuId) {
        menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));
    }
}
