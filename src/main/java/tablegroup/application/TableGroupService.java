package tablegroup.application;

import kitchenpos.exception.CannotFindException;
import kitchenpos.ordertable.domain.*;
import tablegroup.domain.TableGroupValidator;
import tablegroup.dto.TableGroupRequest;
import tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tablegroup.domain.TableGroup;
import tablegroup.domain.TableGroupRepository;

import java.util.List;

import static kitchenpos.common.Message.ERROR_TABLE_GROUP_NOT_FOUND;

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
        return TableGroupResponse.of(tableGroupRepository.save(new TableGroup()));
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
