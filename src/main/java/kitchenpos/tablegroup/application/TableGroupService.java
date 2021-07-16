package kitchenpos.tablegroup.application;

import kitchenpos.exception.TableGroupException;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Transactional
public class TableGroupService {

    private  static final String MINIMUM_GROUP_TABLE_COUNT_ERROR_MESSAGE = "2개 이상의 테이블을 그룹핑할 수 있습니다.";
    public static final String NOT_FOUND_TABLE_GORUP_ERROR_MESSAGE = "테이블 그룹이 존재하지 않습니다.";

    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;

    public TableGroupService(TableGroupRepository tableGroupRepository, TableService tableService) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
    }

    public TableGroupResponse create(final TableGroupRequest request) {
        checkValidOrderTableCount(request.getOrderTableIds());
        OrderTables orderTables = tableService.findAllByIds(request.getOrderTableIds());
        validateOrderTables(orderTables, request);
        TableGroup tableGroup = request.toTableGroup();
        tableGroup.addOrderTables(orderTables);
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new TableGroupException(NOT_FOUND_TABLE_GORUP_ERROR_MESSAGE));
        tableGroup.checkValidOrderStatusCompletion();
        tableGroup.updateUnGroup();
    }

    private void validateOrderTables(final OrderTables orderTables, final TableGroupRequest request) {
        orderTables.checkValidEqualToRequestSize(request.getOrderTableIds());
        orderTables.checkValidEmptyTableGroup();
    }

    private void checkValidOrderTableCount(final List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new TableGroupException(MINIMUM_GROUP_TABLE_COUNT_ERROR_MESSAGE);
        }
    }
}
