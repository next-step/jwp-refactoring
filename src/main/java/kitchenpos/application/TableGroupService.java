package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.exception.GroupTableRequestException;

@Transactional(readOnly = true)
@Service
public class TableGroupService {
    public static final int LEAST_ORDERS_COUNT = 2;

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(request.getOrderTables());
        validateGroup(request.getOrderTables(), orderTables);
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        for (OrderTable orderTable : orderTables) {
            orderTable.group(tableGroup);
        }
        return TableGroupResponse.of(tableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private void validateGroup(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < LEAST_ORDERS_COUNT
            || orderTables.size() != orderTableIds.size()) {
            throw new GroupTableRequestException();
        }
    }
}
