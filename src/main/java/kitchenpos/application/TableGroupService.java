package kitchenpos.application;

import java.util.ArrayList;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableRequest;
import kitchenpos.validate.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    private final TableGroupValidator validator;

    public TableGroupService(
            final OrderDao orderDao,
            final OrderTableDao orderTableDao,
            final TableGroupDao tableGroupDao,
            final TableGroupValidator validator) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.validator = validator;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        List<TableRequest> tableRequests = tableGroupRequest.getOrderTables();
        validator.validateRequest(tableRequests);

        List<OrderTable> orderTables = getOrderTables(tableRequests);
        validator.validateCreate(tableRequests, orderTables);

        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(orderTables));
        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.grouping(savedTableGroup);
        }

        return savedTableGroup;
    }

    private List<OrderTable> getOrderTables(List<TableRequest> tableRequests) {
        List<OrderTable> orderTables = new ArrayList<>();
        for (TableRequest orderTable : tableRequests) {
            orderTables.add(orderTableDao.findById(orderTable.getId())
                    .orElseThrow(IllegalArgumentException::new));
        }
        return orderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.upGroup();
        }
    }
}
