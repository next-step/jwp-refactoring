package kitchenpos.tablegroup.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public TableGroupResponse create(TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = tableGroupRequest.getOrderTableRequests().stream()
                .map(tableId->orderTableRepository.findById(tableId).orElseThrow(
                        ()-> new EntityNotFoundException("주문테이블", tableId)
                )).collect(Collectors.toList());

        TableGroup saved = tableGroupRepository.save(TableGroup.of(OrderTables.of(orderTables)));
        return TableGroupResponse.of(saved);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(
                ()-> new EntityNotFoundException("단체지정", tableGroupId)
        );
        tableGroup.unGroup();
    }


}
