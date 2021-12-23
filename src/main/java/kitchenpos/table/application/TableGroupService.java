package kitchenpos.table.application;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.NoResultDataException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Amount;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.domain.OrderTableStatus;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupDao;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    private final OrderDao orderDao;

    public TableGroupService(OrderTableDao orderTableDao,
        TableGroupDao tableGroupDao,
        OrderDao orderDao) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.orderDao = orderDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {

        final List<Long> orderTableIds = convert(tableGroupRequest);
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);
        final OrderTables savedOrderTables = OrderTables.of(orderTables);

        validIsNotEqualsSize(savedOrderTables, tableGroupRequest.getOrderTables());

        TableGroup tableGroup = TableGroup.of(savedOrderTables.getList());
        return TableGroupResponse.of(tableGroupDao.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
            .orElseThrow(NoResultDataException::new);

        List<OrderStatus> savedOrderStatus = orderDao.findOrderStatusByOrderTableIn(tableGroup.getOrderTableIds());
        OrderStatus.validStatusIsCookingOrMealThrow(savedOrderStatus);

        tableGroup.unGroup();
    }

    private List<Long> convert(TableGroupRequest tableGroupRequest) {
        List<OrderTableRequest> orderTables = tableGroupRequest.getOrderTables();
        return orderTables.stream()
            .map(OrderTableRequest::getId)
            .collect(Collectors.toList());
    }

    public void validIsNotEqualsSize(OrderTables savedOrderTables,
        List<OrderTableRequest> orderTableRequests) {
        if (savedOrderTables.size() != orderTableRequests.size()) {
            throw new IllegalArgumentException();
        }
    }

}
