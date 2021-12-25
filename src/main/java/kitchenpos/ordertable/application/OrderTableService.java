package kitchenpos.ordertable.application;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = orderTableRepository.save(orderTableRequest.toOrderTable());
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.listOf(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId) {
        final OrderTable orderTable = findOrderTableById(orderTableId);

        orderTable.changeEmpty();

        return OrderTableResponse.of(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
//        final int numberOfGuests = orderTable.getNumberOfGuests();
//
//        if (numberOfGuests < 0) {
//            throw new IllegalArgumentException();
//        }
//
//        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
//
//        if (savedOrderTable.isEmpty()) {
//            throw new IllegalArgumentException();
//        }
//
//        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(orderTable);
    }

    public OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
