package kitchenpos.table.application;

import java.util.List;
import kitchenpos.exception.CannotCreateException;
import kitchenpos.exception.NotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository,
        TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final List<Long> ids) {

        final List<OrderTable> orderTables = findOrderTables(ids);

        validateTableSize(ids, orderTables);

        TableGroup tableGroup = TableGroup.create();
        tableGroup.addOrderTables(orderTables);

        TableGroup persist = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.of(persist);
    }

    private void validateTableSize(List<Long> ids, List<OrderTable> orderTables) {
        if (ids.size() != orderTables.size()) {
            throw new CannotCreateException("등록되어 있는 테이블만 단체지정이 가능합니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new NotFoundException("해당하는 단체를 찾을 수 없습니다."));
        tableGroup.clearOrderTable();
    }

    private List<OrderTable> findOrderTables(List<Long> ids) {
        return orderTableRepository.findAllByIdIn(ids);
    }
}
