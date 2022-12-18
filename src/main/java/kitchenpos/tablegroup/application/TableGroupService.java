package kitchenpos.tablegroup.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.dto.TableRequest;
import kitchenpos.tablegroup.message.TableGroupMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        TableGroup tableGroup = mapToTableGroup(request);
        tableGroup.group();
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    private TableGroup mapToTableGroup(TableGroupCreateRequest request) {
        List<OrderTable> orderTableItems = orderTableRepository.findAllById(request.getTableIds());
        validateSavedOrderTables(orderTableItems, request.getTableRequests());
        return new TableGroup(orderTableItems);
    }

    private void validateSavedOrderTables(List<OrderTable> orderTables, List<TableRequest> tableRequests) {
        if (orderTables.size() != tableRequests.size()) {
            throw new IllegalArgumentException(TableGroupMessage.CREATE_ERROR_NOT_EQUAL_TABLE_SIZE.message());
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(EntityNotFoundException::new);
        tableGroup.unGroup();
    }
}
