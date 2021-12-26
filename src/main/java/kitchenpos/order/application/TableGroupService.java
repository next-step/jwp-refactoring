package kitchenpos.order.application;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> savedOrderTables = findOrderTables(tableGroupRequest);
        final TableGroup savedTableGroup = TableGroup.from(savedOrderTables);
        return TableGroupResponse.from(tableGroupRepository.save(savedTableGroup));
    }

    private List<OrderTable> findOrderTables(final TableGroupRequest tableGroupRequest) {
        List<Long> requestOrderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> savedOrderTable = orderTableRepository.findAllById(requestOrderTableIds);
        if (requestOrderTableIds.size() != savedOrderTable.size()) {
            throw new IllegalArgumentException("등록하려는 주문 테이블이 등록되어있지 않습니다.");
        }
        return savedOrderTable;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        orderTables.forEach(OrderTable::ungroup);
    }
}
