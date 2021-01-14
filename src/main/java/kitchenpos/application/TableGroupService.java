package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.TableGroup;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final TableGroupDao tableGroupDao;
    private final OrderTableService orderTableService;

    public TableGroupService(final OrderDao orderDao,
          final TableGroupDao tableGroupDao,
          OrderTableService orderTableService) {
        this.orderDao = orderDao;
        this.tableGroupDao = tableGroupDao;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("단체 지정은 주문 테이블이 최소 2개 이상이어야 합니다.");
        }

        final List<Long> orderTableIds = orderTables.stream()
              .map(OrderTable::getId)
              .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableService.findAllByOrderTableIds(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("주문 테이블 정보를 찾을 수 없습니다.");
        }

        tableGroup.setCreatedDate(LocalDateTime.now());

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroup(tableGroupId);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableService.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("단체지정 해제가 불가능한 테이블입니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.unTableGroup();
        }
    }
}
