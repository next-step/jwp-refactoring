package kitchenpos.table.application;

import static kitchenpos.order.domain.OrderStatus.canNotChangeOrderTableStatuses;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.dto.CreateOrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    public static final String ORDER_TABLE_NOT_FOUND_ERROR_MESSAGE = "요청에 해당하는 주문 테이블을 찾지 못했습니다.";
    public static final String COOKING_OR_MEAL_ORDER_TABLE_CHANGE_EMPTY_ERROR_MESSAGE = "조리중, 식사중인 주문 테이블이 포함되어 있어 단체 지정을 해제 할 수 없습니다.";

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final CreateOrderTableRequest createOrderTableRequest) {
        OrderTable persistOrderTable = orderTableRepository.save(createOrderTableRequest.toOrderTable());
        return OrderTableResponse.from(persistOrderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable persistOrderTable = findOrderTableById(orderTableId);
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, canNotChangeOrderTableStatuses())) {
            throw new IllegalArgumentException(COOKING_OR_MEAL_ORDER_TABLE_CHANGE_EMPTY_ERROR_MESSAGE);
        }
        persistOrderTable.changeEmpty(orderTable.isEmpty());
        return OrderTableResponse.from(persistOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());
        return OrderTableResponse.from(savedOrderTable);
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_TABLE_NOT_FOUND_ERROR_MESSAGE));
    }
}
