package kitchenpos.tablegroup.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.exception.MisMatchedOrderTablesSizeException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@Service
@Transactional
public class TableGroupService {
    private static final int ORDER_TABLE_MINIMUM_SIZE = 2;
    private final TableService tableService;
    private final OrderService orderService;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupService(final TableService tableService, final OrderService orderService, final TableGroupRepository tableGroupRepository,
                             final OrderTableRepository orderTableRepository) {
        this.tableService = tableService;
        this.orderService = orderService;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findByTableGroupId(tableGroupId));
        orderService.validateExistsOrdersStatusIsCookingOrMeal(orderTables.getOrderTableIds());
        orderTables.ungroupOrderTables();
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        OrderTables orderTables = getOrderTables(tableGroupRequest);
        TableGroup tableGroup = makeTableGroup(orderTables);
        return TableGroupResponse.of(tableGroup);
    }

    private OrderTables getOrderTables(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        validateMinimumOrderTableSize(orderTableIds);
        OrderTables orderTables = new OrderTables(tableService.findOrderTablesByIds(orderTableIds));
        validateMisMatchedOrderTableSize(orderTables, orderTableIds);
        return orderTables;
    }

    private TableGroup makeTableGroup(OrderTables orderTables) {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        tableGroup.addOrderTables(orderTables);
        return tableGroup;
    }

    private void validateMinimumOrderTableSize(List<Long> orderTableIds) {
        if (orderTableIds.size() < ORDER_TABLE_MINIMUM_SIZE) {
            throw new IllegalArgumentException("정산 그룹 생성은 2개 이상의 테이블만 가능합니다.");
        }
    }

    private void validateMisMatchedOrderTableSize(OrderTables orderTables, List<Long> orderTableIds) {
        if (!orderTables.isSameSize(orderTableIds.size())) {
            throw new MisMatchedOrderTablesSizeException("입력된 항목과 조회결과가 일치하지 않습니다.");
        }
    }
}
