package kitchenpos.ordertable.application;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;

    public OrderTableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.save(request.toEntity(null, request.getNumberOfGuests(), request.isEmpty()));
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.changeEmpty(request.isEmpty());
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }

    public OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("테이블이 등록되어 있어야 한다."));
    }

    public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
        return orderTableRepository.findAllById(orderTableIds);
    }
}
