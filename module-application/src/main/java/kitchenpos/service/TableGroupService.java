package kitchenpos.service;


import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupRequest.OrderTableRequest;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;

    private final OrderTableRepository orderTableRepository;

    public TableGroupService(final TableGroupRepository tableGroupRepository, final OrderTableRepository orderTableRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTable> orderTables = toOrderTables(request.getOrderTables());
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(orderTables));
        return TableGroupResponse.of(savedTableGroup);
    }

    private List<OrderTable> toOrderTables(final List<OrderTableRequest> orderTables) {
        return orderTables.stream()
                .map(this::toOrderTable)
                .collect(Collectors.toList());
    }

    private OrderTable toOrderTable(final OrderTableRequest orderTableRequest) {
        Long orderTableId = orderTableRequest.getId();
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%d에 해당하는 주문 테이블이 없습니다.", orderTableId)));
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%d에 해당하는 단체 지정이 없습니다.", tableGroupId)));
        tableGroup.ungroup();
    }
}
