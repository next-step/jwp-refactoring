package kitchenpos.order.application;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.TableGroupRepository;

@Service
public class TableGroupService {
    private final TableService tableService;
    private final OrderRepository orderRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableService tableService, OrderRepository orderRepository,
        TableGroupRepository tableGroupRepository) {
        this.tableService = tableService;
        this.orderRepository = orderRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTables();
        final List<OrderTable> findOrderTables = tableService.findByOrderTableIds(orderTableIds);

        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.of(findOrderTables));
        return new TableGroupResponse(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup findTableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_DATA));

        findTableGroup.validateUngroup();
        findTableGroup.ungroup();
        tableGroupRepository.delete(findTableGroup);
    }
}
