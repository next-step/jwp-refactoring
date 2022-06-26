package kitchenpos.tableGroup.application;

import kitchenpos.core.exception.BadRequestException;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.core.exception.NotFoundException;
import kitchenpos.order.application.OrderTableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.request.TableGroupRequest;
import kitchenpos.tableGroup.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableService orderTableService;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository, OrderTableService orderTableService) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> orderTables = validateOrderTables(request.getOrderTableIds());

        TableGroup tableGroup = TableGroup.from(orderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.toResponse(savedTableGroup);
    }

    private List<OrderTable> validateOrderTables(List<Long> orderTableIds) {
        validateOrderTableSize(orderTableIds);

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTableEqualsSize(savedOrderTables, orderTableIds);
        return savedOrderTables;
    }

    private void validateOrderTableSize(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new CannotCreateException(ExceptionType.ORDER_TABLE_AT_LEAST_TWO);
        }
    }

    private void validateOrderTableEqualsSize(List<OrderTable> savedOrderTables, List<Long> orderTableIds)  {
        if (savedOrderTables.size() != orderTableIds.size()) {
            throw new BadRequestException(ExceptionType.CONTAINS_NOT_EXIST_ORDER_TABLE);
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_EXIST_TABLE_GROUP));

        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroup);
        orderTableService.validateOrderTablesStatus(orderTables);
        tableGroup.unGroup();
    }
}
