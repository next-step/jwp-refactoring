package kitchenpos.table.application;

import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
        orderTableGroupRepository.save(tableGroup);
    }

    @Transactional(readOnly = true)
    protected TableGroup getOrderTableGroupById(final Long tableGroupId) {
        return orderTableGroupRepository.findById(tableGroupId).orElseThrow(() -> new RuntimeException());
    }

    @Transactional(readOnly = true)
    protected void validationByNewTableGroup(final TableGroup tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTableIds();

        Long orderTableCount = orderTableRepository.countByIdIn(orderTableIds);
        if (!tableGroup.isSameSize(orderTableCount)) {
            throw new IllegalArgumentException("주문 테이블들은 사전에 등록되어 있어야 한다.");
        }

    }
}
