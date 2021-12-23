package kitchenpos.ordertable.application;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.dao.TableGroupDao;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.TableGroupRequest;
import kitchenpos.ordertable.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private static final String ERROR_MESSAGE_NOT_EXIST_TABLE_GROUP = "테이블 그룹 정보가 없습니다.";

    private final TableGroupDao tableGroupDao;
    private final TableService tableService;

    public TableGroupService(TableGroupDao tableGroupDao,
        TableService tableService) {
        this.tableGroupDao = tableGroupDao;
        this.tableService = tableService;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTableRequest> orderTables = tableGroupRequest.getOrderTables();
        List<OrderTable> orderTabless = tableService.findOrderTables(orderTables);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        savedTableGroup.groupTables(orderTabless);
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroup(tableGroupId);
        tableGroup.ungroup();
        tableGroupDao.delete(tableGroup);
    }

    public TableGroup findTableGroup(Long tableGroupId) {
        return tableGroupDao.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException(ERROR_MESSAGE_NOT_EXIST_TABLE_GROUP));
    }
}
