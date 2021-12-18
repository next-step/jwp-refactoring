package kitchenpos.tablegroup.application;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTableValidator;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static java.util.stream.Collectors.*;

@Service
@Transactional
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final OrderTableRepository orderTableRepository,
                             final OrderTableValidator orderTableValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.newInstance());
        final OrderTables orderTables = makeOrderTables(tableGroupRequest);
        orderTables.group(savedTableGroup.getId());
        return TableGroupResponse.from(savedTableGroup, orderTables);
    }

    public void ungroup(final Long id) {
        if (!tableGroupRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        final OrderTables orderTables = findOrderTables(id);
        orderTables.ungroup(orderTableValidator);
    }

    private OrderTables makeOrderTables(TableGroupRequest tableGroupRequest) {
        return tableGroupRequest.getOrderTableIds()
                .stream()
                .map(this::findOrderTableById)
                .collect(collectingAndThen(toList(), OrderTables::new));
    }

    private OrderTables findOrderTables(Long tableGroupId) {
        return new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(EntityNotFoundException::new);
    }
}
