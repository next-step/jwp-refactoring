package kitchenpos.table.application;

import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    private static final String ERROR_MESSAGE_ORDER_TABLE_NOT_FOUND_FORMAT = "주문 테이블을 찾을 수 없습니다. ID : %d";
    private static final String ERROR_MESSAGE_CHANGE_GUEST_IS_EMPTY_TABLE = "비어있는 테이블은 인원 수를 변경할 수 없습니다. ID : %d";
    private static final String ERROR_MESSAGE_NOT_FOUND_BY_ORDER_TABLE_FORMAT = "주문이 존재하지 않습니다. Order Table ID : %d";

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest request) {
        return OrderTableResponse.from(orderTableRepository.save(request.toEntity()));
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId) {
        OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.validateGrouped();
        findByOrderTableId(savedOrderTable.id()).validateCookingAndMeal();
        savedOrderTable.changeEmpty();
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = findById(orderTableId);
        if (savedOrderTable.isEmpty()) {
            throw new InvalidParameterException(String.format(ERROR_MESSAGE_CHANGE_GUEST_IS_EMPTY_TABLE, orderTableId));
        }
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(savedOrderTable);
    }

    private OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_ORDER_TABLE_NOT_FOUND_FORMAT,
                        orderTableId)));
    }

    private Order findByOrderTableId(Long id) {
        return orderRepository.findByOrderTableId(id)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_BY_ORDER_TABLE_FORMAT, id)));
    }
}
