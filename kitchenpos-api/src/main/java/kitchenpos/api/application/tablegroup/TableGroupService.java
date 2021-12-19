package kitchenpos.api.application.tablegroup;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.domain.table.OrderTable;
import kitchenpos.common.domain.tablegroup.TableGroup;
import kitchenpos.common.domain.tablegroup.TableGroupRepository;
import kitchenpos.common.domain.tablegroup.TableGroupValidator;
import kitchenpos.common.dto.tablegroup.OrderTableIdRequest;
import kitchenpos.common.dto.tablegroup.TableGroupRequest;
import kitchenpos.common.dto.tablegroup.TableGroupResponse;
import kitchenpos.common.utils.StreamUtils;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final TableGroupValidator tableGroupValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = StreamUtils.mapToList(tableGroupRequest.getOrderTables(), OrderTableIdRequest::getId);
        List<OrderTable> orderTables = tableGroupValidator.getValidGroupOrderTables(orderTableIds);
        TableGroup tableGroup = tableGroupRepository.save(TableGroup.create());
        tableGroup.group(orderTableIds);

        return TableGroupResponse.from(tableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroup(tableGroupId);
        tableGroupValidator.validateNotCompletionOrder(tableGroup);

        tableGroup.ungroup();
    }

    private TableGroup findTableGroup(Long id) {
        return tableGroupRepository.findById(id)
                                   .orElseThrow(EntityNotFoundException::new);
    }
}
