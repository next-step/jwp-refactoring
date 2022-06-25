package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.order.domain.OrderStatusV2;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTableV2;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository,
                        OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTableV2 orderTable = orderTableRequest.toOrderTable();
        final OrderTableV2 persist = orderTableRepository.save(orderTable);
        return persist.toOrderTableResponse();
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableV2::toOrderTableResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId) {
        final OrderTableV2 savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.existTableGroupId()) {
            throw new IllegalArgumentException();
        }

        if (orderRepository.existsOrdersV2ByOrderTableIdAndOrderStatusNot(orderTableId, OrderStatusV2.COMPLETION)) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.empty();
        return savedOrderTable.toOrderTableResponse();
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTableV2 savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return savedOrderTable.toOrderTableResponse();
    }
}
