package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.OrderTableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableGroupRepository orderTableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository, final OrderTableGroupRepository orderTableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableGroupRepository = orderTableGroupRepository;
    }

    public TableGroup create(final TableGroup tableGroup) {
        validationByNewTableGroup(tableGroup);

        return orderTableGroupRepository.save(tableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = getOrderTableGroupById(tableGroupId);

        tableGroup.remove();
    }

    @Transactional(readOnly = true)
    protected TableGroup getOrderTableGroupById(final Long tableGroupId) {
        return orderTableGroupRepository.findById(tableGroupId).orElseThrow(() -> new RuntimeException());
    }

    //TODO : 도메인으로 이동 방법 고민
    @Transactional(readOnly = true)
    protected void validationByNewTableGroup(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderTables.size() != orderTableRepository.countByIdIn(orderTableIds)) {
            throw new IllegalArgumentException("주문 테이블들은 사전에 등록되어 있어야 한다.");
        }
    }
}
