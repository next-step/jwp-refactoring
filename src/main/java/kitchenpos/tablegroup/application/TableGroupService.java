package kitchenpos.tablegroup.application;

import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static java.util.stream.Collectors.*;

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
        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.newInstance());
        final OrderTables orderTables = makeOrderTables(tableGroupRequest);
        orderTables.group(savedTableGroup.getId());
        return TableGroupResponse.from(savedTableGroup, orderTables);
    }

    public void ungroup(final Long id) {
        if (!tableGroupRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        final OrderTables orderTables = orderTableService.findAllByTableGroupId(id);
        orderTables.ungroup();
    }

    private OrderTables makeOrderTables(TableGroupRequest tableGroupRequest) {
        return tableGroupRequest.getOrderTableIds()
                .stream()
                .map(this::getOrderTableById)
                .collect(collectingAndThen(toList(), OrderTables::new));
    }

    private OrderTable getOrderTableById(Long orderTableId) {
        return orderTableService.findById(orderTableId);
    }
}
