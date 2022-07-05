package kitchenpos.application.table;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.order.OrderDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.table.CreateOrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    public static final String ORDER_TABLE_NOT_FOUND_ERROR_MESSAGE = "요청에 해당하는 주문 테이블을 찾지 못했습니다.";

    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderDao orderDao, OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
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
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
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
