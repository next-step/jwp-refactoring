package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderTableDao orderTableDao,
        final TableGroupDao tableGroupDao) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTable> savedOrderTables = getOrderTables(request);
        TableGroup tableGroup = request.toTableGroup(savedOrderTables);
        return TableGroupResponse.from(tableGroupDao.save(tableGroup));
    }

    private List<OrderTable> getOrderTables(TableGroupRequest request) {
        return request.getOrderTables().stream()
            .map(orderTableRequest -> orderTableDao.findById(orderTableRequest.getId()).orElseThrow(
                () -> new IllegalArgumentException("단체 지정할 테이블 중 존재하지 않는 테이블이 존재 합니다.")))
            .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹 입니다."));
        tableGroup.ungroup();
    }
}
