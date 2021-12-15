package kitchenpos.tablegroup.application;

import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableService orderTableService;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final OrderTableService orderTableService) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableService = orderTableService;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        final TableGroup tableGroup = TableGroup.of(makeOrderTables(orderTableIds));
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(savedTableGroup);
    }

    public void ungroup(final Long id) {
        TableGroup tableGroup = tableGroupRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        tableGroup.ungroup();
    }

    private List<OrderTable> makeOrderTables(List<Long> orderTableIds) {
        final List<OrderTable> orderTables = new ArrayList<>();
        for (Long orderTableId : orderTableIds) {
            orderTables.add(getOrderTableById(orderTableId));
        }
        return orderTables;
    }

    private OrderTable getOrderTableById(Long orderTableId) {
        return orderTableService.findById(orderTableId);
    }
}
