package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableValidator tableValidator;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository,
                             TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroup) {
        List<Long> orderTableIds = getOrderTableIds(tableGroup);
        List<OrderTable> orderTables = findOrderTablesByIds(orderTableIds);

        TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.from(orderTables));
        orderTables.forEach(i->i.group(savedTableGroup.getId()));
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        tableValidator.validOrdersCompletionByTableGroup(tableGroup);
        tableGroup.ungroup();
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("단체지정을 찾을 수 없습니다."));
    }

    private List<Long> getOrderTableIds(TableGroupRequest tableGroup) {
        return tableGroup
                .getOrderTables()
                .stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    private List<OrderTable> findOrderTablesByIds(List<Long> orderTableIds) {
        return orderTableIds.stream()
                .map(this::findOrderTableById)
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(long id) {
        return orderTableRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블 입니다."));
    }
}
