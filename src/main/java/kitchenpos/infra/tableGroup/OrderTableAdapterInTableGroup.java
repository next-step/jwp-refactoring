package kitchenpos.infra.tableGroup;

import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.domain.tableGroup.SafeOrderTableInTableGroup;
import kitchenpos.domain.tableGroup.exceptions.InvalidTableGroupTryException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableAdapterInTableGroup implements SafeOrderTableInTableGroup {
    private final OrderTableRepository orderTableRepository;

    public OrderTableAdapterInTableGroup(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void canGroupTheseTables(final List<Long> orderTableIds) {
        if (orderTableRepository.countAllByIdIn(orderTableIds) != orderTableIds.size()) {
            throw new InvalidTableGroupTryException("존재하지 않는 주문 테이블을 단체 지정할 수 없습니다.");
        }
        if (orderTableRepository.countAllByIdInAndEmptyIs(orderTableIds, false) != 0) {
            throw new InvalidTableGroupTryException("빈 주문 테이블들로만 단체 지정할 수 있습니다.");
        }
    }
}
