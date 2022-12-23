package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTable> savedOrderTables = getOrderTables(request);
        TableGroup tableGroup = request.toTableGroup(savedOrderTables);
        //validateTableGroup(tableGroup);

        validateOrderTablesStatus(savedOrderTables);

        tableGroup.setCreatedDate(LocalDateTime.now());

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        updateOrderTables(savedOrderTables, savedTableGroup.getId());

        savedTableGroup.setOrderTables(savedOrderTables);
        return TableGroupResponse.from(savedTableGroup);
    }

    private void updateOrderTables(List<OrderTable> savedOrderTables, Long tableGroupId) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            savedOrderTable.setEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
    }

    private void validateOrderTablesStatus(List<OrderTable> orderTables) {
        for (final OrderTable savedOrderTable : orderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    private List<OrderTable> getOrderTables(TableGroupRequest request) {
        return request.getOrderTables().stream()
            .map(orderTableRequest -> orderTableDao.findById(orderTableRequest.getId()).orElseThrow(() -> new IllegalArgumentException("단체 지정할 테이블 중 존재하지 않는 테이블이 존재 합니다.")))
            .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹 입니다."));
        tableGroup.ungroup();
    }
}
