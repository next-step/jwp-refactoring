package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.exception.CreateTableGroupException;
import kitchenpos.exception.DontUnGroupException;
import kitchenpos.exception.NotFoundTableGroupException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    public static final String CREATE_TABLE_GROUP_DEFAULT_RULE = "테이블이 없거나 1개 이하인 경우에는 단체를 생성할 수 없습니다.";
    public static final String CREATE_TABLE_GROUP_ORDER_TABLE_MISMATCH = "요청한 테이블 정보와 현재 테이블 정보가 일치하지 않습니다.";
    public static final String CREATE_TABLE_GROUP_ORDER_TABLE_STATUS = "단체로 지정할 테이블이 빈 테이블이거나 이미 단체가 지정된 경우 단체를 생성할 수 없습니다.";
    private static final int MIN_ORDER_TABLE_COUNT = 2;

    private final OrderService orderService;
    private final OrderTableService orderTableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderService orderService,
                             final OrderTableService orderTableService,
                             final TableGroupRepository tableGroupRepository) {
        this.orderService = orderService;
        this.orderTableService = orderTableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = orderTableService.findOrderTables(tableGroupRequest.getOrderTables());
        validateTableGroupOrderTables(tableGroupRequest.getOrderTables(), orderTables);

        TableGroup tableGroup = tableGroupRepository.save(TableGroup.from(orderTables));
        tableGroup.assignedOrderTables(orderTables);
        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = this.findTableGroup(tableGroupId);
        validateUnGroup(tableGroup);

        for (final OrderTable orderTable : tableGroup.findOrderTables()) {
            orderTable.ungroup();
        }
    }

    private void validateUnGroup(TableGroup tableGroup) {
        if (orderService.isExistDontUnGroupState(tableGroup.findOrderTables())) {
            throw new DontUnGroupException();
        }
    }

    private TableGroup findTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotFoundTableGroupException(tableGroupId));
    }

    private void validateTableGroupOrderTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (isCreateTableGroup(orderTableIds)) {
            throw new CreateTableGroupException(CREATE_TABLE_GROUP_DEFAULT_RULE);
        }

        if (orderTableIds.size() != orderTables.size()) {
            throw new CreateTableGroupException(CREATE_TABLE_GROUP_ORDER_TABLE_MISMATCH);
        }

        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || orderTable.getTableGroup() != null) {
                throw new CreateTableGroupException(CREATE_TABLE_GROUP_ORDER_TABLE_STATUS);
            }
        }
    }

    private boolean isCreateTableGroup(List<Long> orderTableIds) {
        return CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MIN_ORDER_TABLE_COUNT;
    }
}
