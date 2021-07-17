package kitchenpos.order.domain;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Menus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final MenuRepository menuRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(final OrderRequest orderRequest) {
        final List<Long> menuIds = orderRequest.menuIds();
        final Menus menus = new Menus(menuRepository.findAllByIdIn(menuIds));
        validateMenuSize(menuIds, menus);

        final OrderTable orderTable = findOrderTable(orderRequest.getOrderTableId());
        validateOrderTable(orderTable);

        validateOrderLineItems(orderRequest);
    }

    private void validateMenuSize(final List<Long> menuIds, final Menus menus) {
        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException("등록되지 않은 메뉴가 있습니다.");
        }
    }

    private OrderTable findOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에는 주문할 수 없습니다.");
        }
    }

    private void validateOrderLineItems(final OrderRequest orderRequest) {
        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new IllegalArgumentException("빈 주문 아이템은 추가할 수 없습니다.");
        }
    }
}
