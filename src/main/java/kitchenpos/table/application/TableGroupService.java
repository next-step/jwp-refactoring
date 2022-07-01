package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.infrastructure.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.infrastructure.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = mapToOrderTableIds(tableGroupRequest);
        final List<OrderTable> savedOrderTables = validateExistsOrderTables(orderTableIds);
        OrderTables orderTables = OrderTables.of(savedOrderTables);

        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        orderTables.addTableGroup(tableGroup);

        return TableGroupResponse.of(tableGroup);
    }

    private List<Long> mapToOrderTableIds(TableGroupRequest tableGroupRequest) {
        return tableGroupRequest.getOrderTables().stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    private List<OrderTable> validateExistsOrderTables(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다.");
        }
        return savedOrderTables;
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        final List<OrderTable> orderTables = findAllOrderTables(tableGroupId);

        for (final OrderTable orderTable : orderTables) {
            orderTable.unGroupTable();
        }
    }

    private List<OrderTable> findAllOrderTables(Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }
}
