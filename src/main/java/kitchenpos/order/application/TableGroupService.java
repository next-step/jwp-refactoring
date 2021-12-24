package kitchenpos.order.application;

import kitchenpos.order.application.exception.TableGroupNotFoundException;
import kitchenpos.order.application.exception.TableNotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<Long> tableIds = request.getTableIds();
        final List<OrderTable> orderTables = getTables(tableIds);

        final TableGroup tableGroup = request.toEntity(orderTables);
        final TableGroup persistTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(persistTableGroup, persistTableGroup.getOrderTables());
    }

    @Transactional
    public void ungroup(final Long id) {
        TableGroup tableGroup = getTableGroup(id);
        tableGroup.validateStatus();
        tableGroupRepository.delete(tableGroup);
    }

    private List<OrderTable> getTables(List<Long> tableIds) {
        return tableIds.stream()
                .map(this::getTable)
                .collect(Collectors.toList());
    }

    private OrderTable getTable(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(TableNotFoundException::new);
    }

    private TableGroup getTableGroup(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(TableGroupNotFoundException::new);
    }
}
