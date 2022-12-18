package kitchenpos.tablegroup.application;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.utils.Message.NOT_EXISTS_ORDER_TABLE;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }


    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> savedOrderTables = findOrderTablesByIds(request.getOrderTableIds());
        TableGroup saveTableGroup = tableGroupRepository.save(TableGroup.from(savedOrderTables));
        return TableGroupResponse.from(saveTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = OrderTables.from(orderTableRepository.findAllByTableGroupId(tableGroupId));
        orderTables.unGroup();
    }

    private List<OrderTable> findOrderTablesByIds(List<Long> orderTableIdRequests) {
        return orderTableIdRequests
                .stream()
                .map(this::findOrderTableById)
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_ORDER_TABLE));
    }
}
