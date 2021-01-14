package kitchenpos.table.service;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableGroupServiceJpa {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableServiceJpa orderTableServiceJpa;

    public TableGroupServiceJpa(TableGroupRepository tableGroupRepository, OrderTableServiceJpa orderTableServiceJpa) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableServiceJpa = orderTableServiceJpa;
    }

    public TableGroupResponse create(TableGroupRequest request) {
        checkGroupValidation(request);
        TableGroup tableGroup = new TableGroup();
        tableGroup.addAll(findOrderTables(request));
        TableGroup save = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(save);
    }

    private void checkGroupValidation(TableGroupRequest request) {
        if (request.getOrderTables().isEmpty() || request.getOrderTables().size() < 2) {
            throw new IllegalArgumentException("단체 테이블을 지정할 수 없습니다.");
        }
    }

    private List<OrderTable> findOrderTables(TableGroupRequest request) {
        return request.getOrderTables()
                .stream()
                .map(OrderTableIdRequest::getId)
                .map(orderTableServiceJpa::findById)
                .collect(Collectors.toList());
    }


    public void ungroup(long id) {
        // TODO: check exist order table status
        final List<OrderTable> orderTables = findGroupById(id).getOrderTables();
        orderTables.forEach(OrderTable::removeGroupTable);
    }

    @Transactional(readOnly = true)
    public TableGroup findGroupById(long id) {
        return tableGroupRepository
                .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("테이블 그룹을 찾을수 없습니다."));
    }
}
