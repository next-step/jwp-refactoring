package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository, TableGroupValidator tableGroupValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> savedOrderTables = findOrderTables(tableGroupRequest.findOrderTableIds());
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(new OrderTables(savedOrderTables)));
        changeTableGroupId(savedOrderTables, savedTableGroup);
        return TableGroupResponse.of(savedTableGroup);
    }

    private void changeTableGroupId(List<OrderTable> savedOrderTables, TableGroup savedTableGroup) {
        for (OrderTable orderTable : savedOrderTables) {
            orderTable.changeTableGroupId(savedTableGroup.getId());
        }
    }

    @Transactional(readOnly = true)
    public List<OrderTable> findOrderTables(List<Long> orderTableIds) {
        return orderTableRepository.findAllById(orderTableIds);
    }

    private List<Long> findOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(EntityNotFoundException::new);
        tableGroupValidator.validationUpgroup(findOrderTableIds(tableGroup.getOrderTables()));
        tableGroup.upgroup();
    }
}
