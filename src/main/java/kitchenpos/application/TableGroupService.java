package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(TableGroupRequest request) {
        request.validateRequestedSizeOfOrderTables();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(request.getOrderTableIds());
        request.validateSavedSizeOfOrderTables(savedOrderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.createTableGroup(LocalDateTime.now(), savedOrderTables));
        savedOrderTables.forEach(ot -> ot.occupy(savedTableGroup));
        return TableGroupResponse.from(savedTableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findById(tableGroupId);

        final List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        tableGroup.clearTables();
    }

    public TableGroup findById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TableGroup id:" + id + "가 존재하지 않습니다."));
    }
}
