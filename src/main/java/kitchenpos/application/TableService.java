package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableService(final TableGroupRepository tableGroupRepository,
                        final OrderTableRepository orderTableRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        TableGroup tableGroup = findTableGroupById(request.getTableGroupId());

        OrderTable orderTable =
                OrderTable.of(tableGroup, request.getNumberOfGuests(), request.isEmpty());

        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    private TableGroup findTableGroupById(final Long tableGroupId) {
        if(Objects.isNull(tableGroupId)) {
            return null;
        }

        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("테이블 그룹을 찾을 수 없습니다."));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeEmpty(orderTable.isEmpty());

        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }
}
