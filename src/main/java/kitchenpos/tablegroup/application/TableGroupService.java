package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.validator.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(TableGroupRepository tableGroupRepository, TableGroupValidator tableGroupValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        tableGroupValidator.validateCreateRequestTableGroup(tableGroupRequest);

        List<Long> requestOrderTablesIds = convertOrderTableIds(tableGroupRequest.getOrderTableIds());
        tableGroupValidator.validateCreateOrderTableGroup(requestOrderTablesIds);

        TableGroup tableGroup = tableGroupRepository.save(TableGroup.of(LocalDateTime.now()));
        tableGroup.createOrderTableIds(requestOrderTablesIds);

        return TableGroupResponse.of(tableGroup);
    }

    private List<Long> convertOrderTableIds(List<OrderTableIdRequest> OrderTableIdRequests) {
        return OrderTableIdRequests.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(NoSuchElementException::new);

        tableGroupValidator.validateUnGroup(tableGroup);
        tableGroup.unGroup();
    }
}
