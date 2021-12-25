package kitchenpos.order.table.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.table.domain.OrderTable;
import kitchenpos.order.table.domain.OrderTableRepository;
import kitchenpos.order.table.domain.TableGroup;
import kitchenpos.order.table.domain.TableGroupRepository;
import kitchenpos.order.table.dto.OrderTableRequest;
import kitchenpos.order.table.dto.TableGroupRequest;
import kitchenpos.order.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup tableGroup = tableGroupRequest.toEntity();
        final List<OrderTableRequest> orderTables = tableGroupRequest.getOrderTables();

        final List<Long> orderTableIds = tableGroupRequest.toOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        TableGroup saveTableGroup = tableGroupRepository.save(tableGroup);
        saveTableGroup.addAllOrderTables(savedOrderTables);
        return TableGroupResponse.of(saveTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {

        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("지정된 단체를 찾을 수 없습니다."));

        tableGroup.unGroup();
    }
}
