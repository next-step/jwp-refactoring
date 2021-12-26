package kitchenpos.order.application;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.dao.TableGroupDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> savedOrderTables = findOrderTables(tableGroupRequest);

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }

        List<OrderTable> orderTables = tableGroupRequest.getOrderTables()
                .stream()
                .map(orderTableRequest -> OrderTable.of(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty()))
                .collect(Collectors.toList());

        final TableGroup savedTableGroup = tableGroupDao.save(TableGroup.from(orderTables));

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            savedOrderTable.setEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTableDao.save(orderTable);
        }
    }

    private List<OrderTable> findOrderTables(final TableGroupRequest tableGroupRequest) {
        List<Long> requestOrderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> savedOrderTable = orderTableDao.findAllByIdIn(requestOrderTableIds);
        if (requestOrderTableIds.size() != savedOrderTable.size()) {
            throw new IllegalArgumentException("등록하려는 주문 테이블이 등록되어있지 않습니다.");
        }
        return savedOrderTable;
    }
}
