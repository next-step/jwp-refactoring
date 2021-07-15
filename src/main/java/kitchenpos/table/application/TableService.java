package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableLinker;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableLinker orderTableLinker;

    public TableService(final OrderTableRepository orderTableRepository, OrderTableLinker orderTableLinker) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableLinker = orderTableLinker;
    }

    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.from(orderTableRepository.save(
                new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty())));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> tables = orderTableRepository.findAll();
        return tables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        orderTableLinker.validateOrderStatusByOrderTableId(orderTableId);
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
