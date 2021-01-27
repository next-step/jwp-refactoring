package kitchenpos.web.table.application;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import kitchenpos.web.orders.repository.OrderTableRepository;
import kitchenpos.domain.table.domain.OrderTable;
import kitchenpos.domain.table.domain.TableGroup;
import kitchenpos.web.table.dto.TableGroupRequest;
import kitchenpos.web.table.dto.TableGroupResponse;
import kitchenpos.web.table.repository.TableGroupRepository;

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
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(tableGroupRequest.getOrderTableIds());
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(orderTables));
        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);
        tableGroup.ungroup();
    }
}
