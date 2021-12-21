package kitchenpos.application;

import java.security.InvalidParameterException;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.order.TableGroupRequest;
import kitchenpos.dto.order.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
        final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroup) {
        final List<OrderTable> orderTables = orderTableDao.findAllById(
            tableGroup.getOrderTableIds());

        if (tableGroup.getOrderTableSize() != orderTables.size()) {
            throw new IllegalArgumentException("단체 지정에 속하는 주문테이블은 모두 등록되어있어야합니다.");
        }

        return TableGroupResponse.of(
            tableGroupDao.save(tableGroup.toTableGroup(orderTables)));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
            .orElseThrow(InvalidParameterException::new);

        Orders orders = Orders.of(orderDao.findAllByOrderTableIn(tableGroup.getOrderTables()));
        tableGroup.ungroup(orders);
    }
}
