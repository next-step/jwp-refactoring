package kitchenpos.ordertable.application;

import kitchenpos.ordertable.domain.*;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.TableGroupRequest;
import kitchenpos.ordertable.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.common.Message.ERROR_TABLES_SHOULD_ALL_BE_REGISTERED_TO_BE_GROUPED;

@Service
@Transactional
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = collectOrderTableIds(tableGroupRequest);
        final List<OrderTable> savedOrderTables = findOrderTablesByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException(ERROR_TABLES_SHOULD_ALL_BE_REGISTERED_TO_BE_GROUPED.showText());
        }

        TableGroup tableGroup = new TableGroup(new OrderTables((savedOrderTables)));
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    private List<OrderTable> findOrderTablesByIdIn(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    private List<Long> collectOrderTableIds(TableGroupRequest tableGroupRequest) {
        return tableGroupRequest.getOrderTables().stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    /* TableGroup 리팩토링 시 반영 */
//    public void ungroup(final Long tableGroupId) {
//        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
//        orderTables.forEach(OrderTable::unGroup);
//    }
}
