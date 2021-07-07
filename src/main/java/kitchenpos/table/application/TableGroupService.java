package kitchenpos.table.application;

import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
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
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> orderTables = getOrderTables(tableGroupRequest);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    private List<OrderTable> getOrderTables(TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = tableGroupRequest.getOrderTables()
                .stream()
                .map(orderTableRequest -> findByOrderTable(orderTableRequest.getId()))
                .collect(Collectors.toList());

        return orderTables;
    }

    private OrderTable findByOrderTable(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 테이블입니다."));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(tableGroupId);

        orderTables.forEach(OrderTable::ungroup);
    }
}
