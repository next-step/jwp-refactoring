package kitchenpos.tablegroup.application;

import kitchenpos.advice.exception.OrderTableException;
import kitchenpos.advice.exception.TableGroupException;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.TableService;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final TableService tableService,
        final TableGroupRepository tableGroupRepository) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        tableGroupRequest.validateOrderTableSize();
        List<OrderTable> orderTables = tableService.findAllByIdIn(tableGroupRequest.getOrderTableIds());
        return tableGroupRepository.save(new TableGroup(orderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroupById(tableGroupId);
        final List<OrderTable> orderTables = tableService.findAllByTableGroupId(tableGroupId);
        orderTables.forEach(OrderTable::validateOrderStatusNotInCookingAndMeal);

        tableGroup.ungroup();
        tableGroupRepository.delete(tableGroup);
    }

    public TableGroup findTableGroupById(Long id) {
        return tableGroupRepository.findById(id).orElseThrow(() -> new TableGroupException("테이블 그룹이 존재하지 않습니다", id));
    }
}
