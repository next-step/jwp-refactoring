package kitchenpos.application;

import kitchenpos.repository.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.mapper.TableGroupMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final TableService tableService, final TableGroupRepository tableGroupRepository) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> savedOrderTables = tableService.findOrderTables(request.getOrderTables().stream()
                .map(TableGroupCreateRequest.OrderTable::getId)
                .collect(Collectors.toList()));

        existCheckOrderTable(savedOrderTables, request);

        final TableGroup tableGroup = TableGroup.builder().build();

        for (final OrderTable savedOrderTable : savedOrderTables) {
            tableGroup.saveOrderTable(savedOrderTable);
        }

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupMapper.toTableGroupResponse(savedTableGroup);
    }

    private void existCheckOrderTable(final List<OrderTable> savedOrderTables, final TableGroupCreateRequest request) {
        if (savedOrderTables.size() != request.getOrderTables().size()) {
            throw new EntityNotFoundException("orderTable Not Found");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        tableService.findAllByTableGroupId(tableGroupId)
                .forEach(OrderTable::ungroup);
    }
}
