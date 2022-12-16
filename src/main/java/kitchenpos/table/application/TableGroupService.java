package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@Service
public class TableGroupService {
    private final TableService tableService;
    private final OrderService orderService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableService tableService, OrderService orderService,
            TableGroupRepository tableGroupRepository) {
        this.tableService = tableService;
        this.orderService = orderService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional(isolation = READ_COMMITTED)
    public TableGroup create(final TableGroup tableGroup) {
        final List<OrderTable> savedOrderTables = tableService.findAllByIdIn(tableGroup.tableIds());
        checkedTableSize(savedOrderTables, tableGroup.orderTables());
        tableGroup.updateTableGroup();
        return tableGroupRepository.save(TableGroup.of(LocalDateTime.now(), tableGroup.getOrderTableBag()));
    }

    private void checkedTableSize(List<OrderTable> savedOrderTables, List<OrderTable> orderTables) {
        if (savedOrderTables.size() != orderTables.size()) {
            throw new IllegalArgumentException("실제 저장 된 테이블 목록의 수와 요청한 테이블 목록의 수가 다릅니다");
        }
    }

    @Transactional(isolation = READ_COMMITTED)
    public void ungroup(final Long tableGroupId) {
        final TableGroup savedTableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("단체 지정 테이블을 찾을 수 없습니다"));
        savedTableGroup.unGroup();
        orderService.existsByOrderTableIdInAndOrderStatusIn(savedTableGroup.orderTableIds(),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
        tableGroupRepository.save(savedTableGroup);
    }
}
