package kitchenpos.tableGroup.application;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.domain.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(
            TableGroupRepository tableGroupRepository,
            TableService tableService,
            TableGroupValidator tableGroupValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        OrderTables orderTables = tableService.getOrderTables(groupIds(request));
        tableGroupValidator.validate(request.getOrderTables(), orderTables);

        TableGroup tableGroup = new TableGroup(orderTables);

        final TableGroup persistTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(persistTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        tableGroup.ungroup();
        tableGroupRepository.save(tableGroup);
    }

    public TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId).orElseThrow(() -> new IllegalArgumentException("단체 테이블이 존재하지 않습니다."));
    }

    private List<Long> groupIds(TableGroupRequest request) {
        return request.getOrderTables()
                .stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }


}
