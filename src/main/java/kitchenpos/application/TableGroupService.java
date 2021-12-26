package kitchenpos.application;

import kitchenpos.dao.TableGroupDao;
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
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final TableService tableService, final TableGroupDao tableGroupDao) {
        this.tableService = tableService;
        this.tableGroupDao = tableGroupDao;
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

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

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
