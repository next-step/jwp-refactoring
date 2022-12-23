package ordertable.application;

import order.domain.Order;
import order.repository.OrderRepository;
import order.validator.OrderValidator;
import ordertable.domain.OrderTable;
import ordertable.dto.OrderTableRequest;
import ordertable.dto.OrderTableResponse;
import ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static common.ErrorMessage.NOT_FOUND_ORDER_TABLE;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public TableService(final OrderTableRepository orderTableRepository, final OrderValidator orderValidator,
                        final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {

        OrderTable orderTable = request.toOrderTable();

        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = findById(orderTableId);
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());
        orderValidator.validateOnGoingOrderStatus(orders);
        savedOrderTable.changeEmpty(orderTable.isEmpty());

        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = findById(orderTableId);

        savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    private OrderTable findById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_FOUND_ORDER_TABLE.getMessage(), orderTableId)));
    }
}
