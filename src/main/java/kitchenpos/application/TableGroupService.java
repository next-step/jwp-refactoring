package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final List<OrderTable> savedOrderTables = tableService.findAllByIdIn(tableGroup.tableIds());
        checkedTableSize(savedOrderTables, tableGroup.getOrderTables());
        tableGroup.checkCreatable();
        tableGroup.setCreatedDate(LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        tableGroup.updateTableGroup();
        return savedTableGroup;
    }

    private void checkedTableSize(List<OrderTable> savedOrderTables, List<OrderTable> orderTables) {
        if (savedOrderTables.size() != orderTables.size()) {
            throw new IllegalArgumentException("실제 저장 된 테이블 목록의 수와 요청한 테이블 목록의 수가 다릅니다");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup savedTableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("단체 지정 테이블을 찾을 수 없습니다"));
        savedTableGroup.unGroup();
        orderService.existsByOrderTableIdInAndOrderStatusIn(savedTableGroup.orderTableIds(),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
        tableGroupRepository.save(savedTableGroup);
    }
}
