package kitchenpos.order.application;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.dto.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.from(
                orderTableRepository.save(
                        OrderTable.of(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty())));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.from(savedOrderTable);
    }

    public OrderTables findOrderTables(final TableGroupRequest tableGroupRequest) {
        List<Long> requestOrderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> savedOrderTable = orderTableRepository.findAllById(requestOrderTableIds);
        if (requestOrderTableIds.size() != savedOrderTable.size()) {
            throw new IllegalArgumentException("등록하려는 주문 테이블이 등록되어있지 않습니다.");
        }
        return OrderTables.from(savedOrderTable);
    }

    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }
}
