package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.application.OrderTableOrderStatusValidatorImpl;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.CreateTableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    public static final String TABLE_GROUP_NOT_FOUND_ERROR_MESSAGE = "존재하지 않는 그룹 테이블 입니다.";

    private final OrderTableOrderStatusValidator orderTableOrderStatusValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
        OrderTableOrderStatusValidatorImpl orderStatusValidator,
        OrderTableRepository orderTableRepository,
        TableGroupRepository tableGroupRepository
    ) {
        this.orderTableOrderStatusValidator = orderStatusValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final CreateTableGroupRequest createTableGroupRequest) {
        List<OrderTable> groupTargetOrderTables = findGroupingTargetOrderTables(createTableGroupRequest);
        TableGroup tableGroup = createTableGroupRequest.toTableGroup(groupTargetOrderTables);
        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    public List<OrderTable> findGroupingTargetOrderTables(final CreateTableGroupRequest createTableGroupRequest) {
        return orderTableRepository.findAllByIdIn(createTableGroupRequest.getOrderTables());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup persistGroup = findTableGroupById(tableGroupId);
        orderTableOrderStatusValidator.validateOrderStatus(persistGroup.getOrderTablesIds());
        persistGroup.deallocateOrderTable();
    }

    private TableGroup findTableGroupById(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException(TABLE_GROUP_NOT_FOUND_ERROR_MESSAGE));
    }

    public TableGroupResponse findById(final Long tableGroupId) {
        return TableGroupResponse.from(findTableGroupById(tableGroupId));
    }
}
