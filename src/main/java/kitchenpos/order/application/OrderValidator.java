package kitchenpos.order.application;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validateSave(OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블 ID 입니다."));

        validateOrderLineItemsEmpty(orderRequest);
        validateOrderLineItemsSize(orderRequest);
        validateOrderTableIsEmpty(orderTable);
    }

    private void validateOrderLineItemsEmpty(OrderRequest orderRequest) {
        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }
    }

    private void validateOrderLineItemsSize(OrderRequest orderRequest) {
        long menuCount = menuRepository.countByIdIn(orderRequest.getMenuIds());
        if (orderRequest.getOrderLineItemsSize() != menuCount) {
            throw new IllegalArgumentException("주문 항목에 등록되어 있지 않은 메뉴가 존재합니다.");
        }
    }

    private void validateOrderTableIsEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다.");
        }
    }
}
