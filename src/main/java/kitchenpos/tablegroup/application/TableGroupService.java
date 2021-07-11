package kitchenpos.tablegroup.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.table.domain.OrderTables.MINIMUM_SIZE;

@Service
public class TableGroupService {

    private final OrderTableService orderTableService;
    private final TableGroupRepository tableGroupRepository;
    private final OrderService orderService;

    public TableGroupService(OrderTableService orderTableService, TableGroupRepository tableGroupRepository, OrderService orderService) {
        this.orderTableService = orderTableService;
        this.tableGroupRepository = tableGroupRepository;
        this.orderService = orderService;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = getOrderTableIds(tableGroupRequest.getOrderTables());
        final OrderTables savedOrderTables = findOrderTables(orderTableIds);
        TableGroup tableGroup = new TableGroup(savedOrderTables);
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        final TableGroup saveTableGroup = findById(tableGroupId);
        orderStatusCheck(saveTableGroup.getOrderTables());
        saveTableGroup.unGroup();
    }

    private OrderTables findOrderTables(List<Long> orderTableIds) {
        List<OrderTable> savedOrderTables = orderTableService.findAllByIdIn(orderTableIds);
        allRegisteredCheck(orderTableIds, savedOrderTables);
        return new OrderTables(savedOrderTables);
    }

    private void allRegisteredCheck(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("등록되지 않은 주문 테이블이 있습니다.");
        }
    }

    private List<Long> getOrderTableIds(List<OrderTableRequest> orderTables) {
        sizeValidCheck(orderTables);
        return orderTables.stream().map(OrderTableRequest::getId).collect(Collectors.toList());
    }

    private void sizeValidCheck(List<OrderTableRequest> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_SIZE) {
            throw new IllegalArgumentException("주문 테이블이 2개 이상이어야 합니다.");
        }
    }

    private void orderStatusCheck(List<OrderTable> orderTables) {
        if (orderService.existsByOrderTableIdInAndOrderStatusIn(
                orderTables, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문이 조리나 식사 상태에서는 변경할 수 없습니다.");
        }
    }

    private TableGroup findById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId).orElseThrow(() -> new IllegalArgumentException("단체 지정된 ID가 아닙니다."));
    }
}
