package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTableGroupRepository;
import kitchenpos.table.dto.TableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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

    public TableGroup create(final List<Long> tableIds) {
        OrderTable[] OrderTableArray = tableIds.stream()
            .map(this::getOrderTable)
            .toArray(OrderTable[]::new);

        return create(new TableGroup(OrderTableArray));
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = getOrderTableGroupById(tableGroupId);
        tableGroup.remove();
        orderTableGroupRepository.save(tableGroup);
    }

    @Transactional(readOnly = true)
    protected OrderTable getOrderTable(final Long tableId) {
        return orderTableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }

    @Transactional(readOnly = true)
    protected TableGroup getOrderTableGroupById(final Long tableGroupId) {
        return orderTableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹입니다."));
    }

    protected void validationByNewTableGroup(final TableGroup tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTableIds();

        Long orderTableCount = orderTableRepository.countByIdIn(orderTableIds);
        if (!tableGroup.isSameSize(orderTableCount)) {
            throw new IllegalArgumentException("주문 테이블들은 사전에 등록되어 있어야 한다.");
        }

    }
}
