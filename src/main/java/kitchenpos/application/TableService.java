package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TableService {
    private OrderTableRepository orderTableRepository;
    private OrderRepository orderRepository;

    @Autowired
    public TableService(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest request) {
        return orderTableRepository.save(new OrderTable(request.getNumberOfGuests(), request.isEmpty()));
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                                                    .orElseThrow(NoSuchElementException::new);

        List<Order> orders = orderRepository.findByOrderTable(orderTable);
        if (orders.stream().anyMatch(Order::hasOrderStatusInCookingOrMeal)) {
            throw new IllegalArgumentException("주문 상태가 조리 또는 식사인 테이블은 주문 등록 가능 상태를 변경할 수 없습니다.");
        }

        orderTable.changeEmpty(request.isEmpty());

        return orderTableRepository.save(orderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                                                    .orElseThrow(NoSuchElementException::new);

        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return orderTableRepository.save(orderTable);
    }
}
