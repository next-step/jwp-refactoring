package kitchenpos.table.application;

import kitchenpos.menu.infra.MenuRepository;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.request.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.infra.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator implements OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableValidator(final MenuRepository menuRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validateOrderRequest(final OrderRequest orderRequest) {
        orderRequest.checkOrderLineIsEmpty();
        final Long menuCount = menuRepository.countByIdIn(orderRequest.getMenuIds());
        orderRequest.checkOrderLineItemsExists(menuCount);
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        orderTable.checkOrderTableIsEmpty();
    }
}
