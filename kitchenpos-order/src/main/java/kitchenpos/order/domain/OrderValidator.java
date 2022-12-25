package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(OrderRequest request) {
        validateNotEmptyOrderTable(request);
        validateNotEmptyOrderLineItems(request.toOrderLineItems());
    }

    private void validateNotEmptyOrderTable(OrderRequest request) {
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블 입니다.");
        }
    }

    private void validateNotEmptyOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }
        validateExistsAllMenus(orderLineItems);
    }

    private void validateExistsAllMenus(List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = toMenuIds(orderLineItems);
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 있습니다.");
        }
    }

    private List<Long> toMenuIds(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
    }
}
