package kitchenpos.tableGroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.orderTable.domain.OrderTable;
import kitchenpos.orderTable.domain.OrderTableRepository;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(request.getOrderTableIds());

        if (request.getSize() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블이 포함되어 있습니다.");
        }

        TableGroup tableGroup = TableGroup.createTableGroup(savedOrderTables);

        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return new TableGroupResponse(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findWithOrderTablesById(tableGroupId);

        final List<Long> orderTableIds = tableGroup.getOrderTableIds();

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        tableGroup.ungroup();

        tableGroupRepository.delete(tableGroup);
    }

    private TableGroup findWithOrderTablesById(final Long tableGroupId) {
        return tableGroupRepository.findWithOrderTablesById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("단체 지정 그룹이 존재하지 않습니다."));
    }
}
