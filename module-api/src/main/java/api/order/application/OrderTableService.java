package api.order.application;

import api.order.application.exception.NotExistOrderTableException;
import api.order.dto.OrderTableRequest;
import api.order.dto.OrderTableResponse;
import domain.order.OrderTable;
import domain.order.OrderTableRepository;
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
        OrderTable orderTable = OrderTable.of(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> findOrderTableResponses() {
        return OrderTableResponse.ofList(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public OrderTable findById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(NotExistOrderTableException::new);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> findByIdIn(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findByIdIn(orderTableIds);
        if (orderTables.isEmpty()) {
            throw new NotExistOrderTableException();
        }

        return orderTables;
    }
}
