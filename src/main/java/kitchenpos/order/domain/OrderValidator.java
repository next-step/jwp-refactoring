package kitchenpos.order.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.common.exception.KitchenposNotFoundException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository,
        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(OrderRequest orderRequest) {
        OrderLineItems orderLineItems = new OrderLineItems(orderRequest.getOrderLineItemsEntity());

        validTable(orderRequest);
        validateMenu(orderLineItems);
    }

    private void validTable(OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(KitchenposNotFoundException::new);
        orderTable.validateNotEmpty();
    }

    private void validateMenu(OrderLineItems orderLineItems) {
        orderLineItems.getOrderLineItems()
            .forEach(orderLineItem -> menuRepository.findById(orderLineItem.getMenuId())
                .orElseThrow(KitchenposNotFoundException::new));

        final List<Long> menuIds = orderLineItems.getMenuIds();
        orderLineItems.validateSize(menuRepository.countByIdIn(menuIds));
    }
}
