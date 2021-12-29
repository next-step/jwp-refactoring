package kitchenpos.table.application;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.domain.TableGroupDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupCreateRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        List<OrderTable> orderTables = findOrderTables(tableGroupCreateRequest.getOrderTableIds());
        TableGroup tableGroup = tableGroupCreateRequest.toEntity(orderTables);
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 단체입니다."));
        tableGroup.ungroup();
    }

    private List<OrderTable> findOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블이 포함되어 있습니다.");
        }
        return orderTables;
    }
}
