package ktichenpos.table.application;

import java.util.List;
import ktichenpos.table.domain.OrderTable;
import ktichenpos.table.domain.OrderTableRepository;
import ktichenpos.table.domain.TableGroup;
import ktichenpos.table.domain.TableGroupRepository;
import ktichenpos.table.domain.TableUngroupDomainService;
import ktichenpos.table.dto.TableGroupRequest;
import ktichenpos.table.dto.TableGroupResponse;
import ktichenpos.table.exception.NotExistTableException;
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
