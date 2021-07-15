package kitchenpos.table.application;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.order.application.OrderOrderTableValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.MisMatchedOrderTablesSizeException;
import kitchenpos.table.exception.NonEmptyOrderTableNotFoundException;
import kitchenpos.tablegroup.application.TableGroupOrderTableValidator;

@Component
public class OrderTableValidator implements TableGroupOrderTableValidator, OrderOrderTableValidator {
    private static final int ORDER_TABLE_MINIMUM_SIZE = 2;
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
        orderTableRepository.findByIdIn(orderTableIds)
                .forEach(this::validateOrderTableIsEmptyOrHasTableGroup);
    }

    @Override
    public void validateOrderTablesConditionForCreatingTableGroup(List<Long> orderTableIds) {
        if (orderTableIds.size() < ORDER_TABLE_MINIMUM_SIZE) {
            throw new IllegalArgumentException("정산 그룹 생성은 2개 이상의 테이블만 가능합니다.");
        }
        if (orderTableRepository.countByIdIn(orderTableIds) != orderTableIds.size()) {
            throw new MisMatchedOrderTablesSizeException("입력된 항목과 조회결과가 일치하지 않습니다.");
        }
        validateOrderTableIsEmptyOrHasTableGroups(orderTableIds);
    }

    @Override
    public void validateExistsOrderTableByIdAndEmptyIsFalse(Long orderTableId) {
        this.orderTableRepository.findByIdAndEmptyIsFalse(orderTableId)
                .orElseThrow(() -> new NonEmptyOrderTableNotFoundException("비어있지 않은 테이블 대상이 존재하지 않습니다. 입력 ID : " + orderTableId));
    }
}
