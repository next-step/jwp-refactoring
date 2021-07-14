package kitchenpos.table.application;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Component
public class OrderTableValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderTableValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateOrderTableIsEmptyOrHasTableGroup(OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            throw new IllegalArgumentException("정산 그룹에 포함된 테이블을 새로운 정산그룹에 포함시킬 수 없습니다.");
        }
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있지 않은 테이블은 정산 그룹에 포함시킬 수 없습니다.");
        }
    }

    public void validateOrderTableIsEmptyOrHasTableGroups(List<Long> orderTableIds) {
        orderTableRepository.findByIdIn(orderTableIds).forEach(this::validateOrderTableIsEmptyOrHasTableGroup);
    }
}
