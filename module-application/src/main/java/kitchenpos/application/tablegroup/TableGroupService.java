package kitchenpos.application.tablegroup;

import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.OrderTables;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.dto.ordertable.OrderTableRequest;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.repository.ordertable.OrderTableRepository;
import kitchenpos.repository.tablegroup.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        OrderTables orderTables = findOrderTables(request.getOrderTables());
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(orderTables));
        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("단체 지정된 그룹이 없습니다."));
        tableGroup.ungroup();
    }

    private OrderTables findOrderTables(List<OrderTableRequest> OrderTableRequests) {
        List<OrderTable> orderTables = OrderTableRequests.stream()
                .map(this::findByIdOrderTable)
                .collect(Collectors.toList());
        return new OrderTables(orderTables);
    }

    private OrderTable findByIdOrderTable(OrderTableRequest orderTableRequest) {
        return orderTableRepository.findById(orderTableRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));
    }
}
