package kitchenpos.tablegroup.application;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class TableGroupService {

    private static final int MIN_ORDER_TABLE_COUNT = 2;

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        validateOrderTableIds(orderTableIds);

        final TableGroup tableGroup = TableGroup.of(tableGroupRequest.getCreatedDate(), makeOrderTables(orderTableIds));
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long id) {
        TableGroup tableGroup = tableGroupRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        tableGroup.ungroup();
    }

    private void validateOrderTableIds(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MIN_ORDER_TABLE_COUNT) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> makeOrderTables(List<Long> orderTableIds) {
        final List<OrderTable> orderTables = new ArrayList<>();
        for (Long orderTableId : orderTableIds) {
            OrderTable orderTable = orderTableRepository.findById(orderTableId)
                    .orElseThrow(IllegalArgumentException::new);
            orderTables.add(orderTable);
        }
        return orderTables;
    }
}
