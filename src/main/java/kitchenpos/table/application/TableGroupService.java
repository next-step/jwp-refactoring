package kitchenpos.table.application;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import kitchenpos.orders.repository.OrderRepository;
import kitchenpos.orders.repository.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.TableGroupDao;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupDao tableGroupDao) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(tableGroupRequest.getOrderTableIds());
        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(orderTables));
        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);
        tableGroup.ungroup();
    }
}
