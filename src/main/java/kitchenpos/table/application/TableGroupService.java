package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.NoResultDataException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
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

    public TableGroupService(OrderTableDao orderTableDao,
        TableGroupDao tableGroupDao) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(convert(tableGroupRequest));
        final OrderTables savedOrderTables = OrderTables.of(orderTables);

        validIsNotEqualsSize(savedOrderTables, tableGroupRequest.getOrderTables());

        TableGroup tableGroup = TableGroup.of(savedOrderTables.getList());
        return TableGroupResponse.of(tableGroupDao.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
            .orElseThrow(NoResultDataException::new);
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
