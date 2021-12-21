package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.dto.TableGroupSaveRequest;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.exception.TableGroupNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupSaveRequest request) {
        final List<OrderTable> orderTables = toOrderTables(request);
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(orderTables));
        return TableGroupResponse.of(tableGroup);
    }

    private List<OrderTable> toOrderTables(TableGroupSaveRequest request) {
        return request.getOrderTables().stream()
                .map(it -> orderTableRepository.findById(it.getId())
                        .orElseThrow(OrderTableNotFoundException::new)
                ).collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotFoundException::new);
        tableGroup.ungroup();
        tableGroupRepository.delete(tableGroup);
    }
}
