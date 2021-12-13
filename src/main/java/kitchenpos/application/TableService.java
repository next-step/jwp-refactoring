package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final TableRequest request) {
        return orderTableRepository.save(request.toOrderTable());
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final TableRequest request) {
        checkCompleteTable(orderTableId);

        final OrderTable table = getOrderTable(orderTableId);
        table.checkInTableGroup();
        table.changeEmpty(request.isEmpty());

        return table;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final TableRequest request) {
        final OrderTable table = getOrderTable(orderTableId);
        table.checkEmpty();
        table.changeNumberOfGuests(request.getNumberOfGuests());

        return table;
    }

    private void checkCompleteTable(Long orderTableId) {
        final Optional<Order> savedOrder = orderRepository.findByOrderTableId(orderTableId);
        if (!savedOrder.isPresent()) {
            throw new IllegalArgumentException();
        }
        final Order order = savedOrder.get();
        if (!order.isComplete()) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
