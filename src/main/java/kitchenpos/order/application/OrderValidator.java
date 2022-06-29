package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public long menuCountByIdIn(List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }

    public void validateOrderTableEmptyIsFalse(Long orderTableId) {
        if (!orderTableRepository.existsByIdAndEmptyIsFalse(orderTableId)) {
            throw new IllegalArgumentException("주문 가능한 테이블이 아닙니다.");
        }
    }
}
