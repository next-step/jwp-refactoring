package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.exception.TableGroupNotFoundException;
import kitchenpos.exception.TableNotFoundException;
import kitchenpos.repos.OrderTableRepository;
import kitchenpos.repos.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> orderTables = getTables(tableGroupRequest.getOrderTableIds());
        final TableGroup tableGroup = tableGroupRequest.toTableGroup(orderTables);

        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        tableGroup.validateUngroup();
        tableGroupRepository.delete(tableGroup);
    }

    private List<OrderTable> getTables(List<Long> tableIds) {
        return tableIds.stream()
                .map(it -> findOrderTableById(it))
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(TableNotFoundException::new);
    }

    private TableGroup findTableGroupById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(TableGroupNotFoundException::new);
    }
}
