package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dao.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;
    private final OrderService orderService;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final TableService tableService,
            final OrderService orderService
    ) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
        this.orderService = orderService;
    }

    public TableGroup getTableGroup(final Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 에 해당하는 단체 지정 정보를 찾을 수 없습니다."));
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        OrderTables orderTables = tableService.findOrderTablesByIds(request.getOrderTables());

        checkOrderTableCount(orderTables, request.getOrderTables());

        return tableGroupRepository.save(new TableGroup(orderTables));
    }

    @Transactional
    public void ungroup(final Long id) {
        TableGroup tableGroup = getTableGroup(id);
        List<OrderTable> orderTables = tableGroup.getOrderTables();

        possibleUngroupTableGroup(orderTables);

        tableGroup.ungroup();
    }

    private void checkOrderTableCount(final OrderTables orderTables, final List<Long> ids) {
        if (orderTables.getValue().size() != ids.size()) {
            throw new IllegalArgumentException("생성 요청된 단체 지정에 존재하지 않는 주문 테이블이 있습니다.");
        }
    }

    private void possibleUngroupTableGroup(final List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .mapToLong(OrderTable::getId)
                .boxed()
                .collect(Collectors.toList());

        if (isPossibleUngroupTable(orderTableIds)) {
            throw new IllegalArgumentException("그룹 해제를 하려는 테이블 중 요리중 또는 식사중인 테이블이 존재합니다.");
        }
    }

    private boolean isPossibleUngroupTable(List<Long> orderTableIds) {
        return orderService.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)
        );
    }
}
