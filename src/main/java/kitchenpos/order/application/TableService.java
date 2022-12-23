package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(OrderTableRequest request) {
        return OrderTableResponse.of(orderTableRepository.save(request.toOrderTable()));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.toResponselist(orderTableRepository.findAll());
    }

    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableRequest request) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new EntityNotFoundException());
        List<Order> orders = orderRepository.findAllByOrderTableId(savedOrderTable.getId());

        savedOrderTable.changeEmpty(request.isEmpty(), orders);

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new EntityNotFoundException());
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }
}