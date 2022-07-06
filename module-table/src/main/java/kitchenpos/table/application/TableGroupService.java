package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.TableUngroupDomainService;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.NotExistTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableUngroupDomainService tableUngroupDomainService;

    public TableGroupService(OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository,
                             TableUngroupDomainService tableUngroupDomainService) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableUngroupDomainService = tableUngroupDomainService;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new NotExistTableException();
        }
        return TableGroupResponse.of(tableGroupRepository.save(new TableGroup(savedOrderTables)));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        tableUngroupDomainService.ungroup(tableGroupId);
    }

}
