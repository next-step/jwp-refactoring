package kitchenpos.order.application;

import kitchenpos.common.exception.CannotFindException;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.common.exception.Message.ERROR_TABLE_GROUP_NOT_FOUND;

@Service
@Transactional
public class TableGroupService {

    private final TableGroupValidator tableGroupValidator;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final TableGroupValidator tableGroupValidator, final TableGroupRepository tableGroupRepository) {
        this.tableGroupValidator = tableGroupValidator;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        tableGroupValidator.validateGrouping(tableGroupRequest);
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        return new TableGroupResponse(savedTableGroup.getId(), savedTableGroup.getCreatedDate());
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new CannotFindException(ERROR_TABLE_GROUP_NOT_FOUND));
        List<OrderTable> orderTables = tableGroupValidator.validateUngrouping(tableGroup.getId());
        unGroupOrderTables(orderTables);
    }

    private void unGroupOrderTables(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.unGroup();
        }
    }
}
