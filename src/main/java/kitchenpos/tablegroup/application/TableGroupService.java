package kitchenpos.tablegroup.application;

import java.util.List;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableService tableService, TableGroupRepository tableGroupRepository) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> savedOrderTables = tableService.findAllByIdIn(tableGroupRequest.getOrderTableIds());

        return tableGroupRepository.save(new TableGroup(savedOrderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "등록 되지 않은 단체 지정은 해제할 수 없습니다[tableGroupId:" + tableGroupId + "]"));

        tableGroup.unGroup();
    }
}
