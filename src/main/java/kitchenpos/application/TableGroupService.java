package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.exception.TableGroupNotFoundException;

@Service
public class TableGroupService {

    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableService tableService, TableGroupRepository tableGroupRepository) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        List<OrderTable> orderTables = request.getOrderTables()
            .stream()
            .map(req -> tableService.findById(req.getId()))
            .collect(Collectors.toList());

        return tableGroupRepository.save(new TableGroup(orderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findById(tableGroupId);
        tableGroup.ungroupAll();

        tableGroupRepository.delete(tableGroup);
    }

    private TableGroup findById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new TableGroupNotFoundException("해당 ID의 테이블 그룹이 존재하지 않습니다."));
    }
}
