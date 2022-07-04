package kitchenpos.application.table;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.order.OrderDao;
import kitchenpos.dao.table.OrderTableDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.table.CreateOrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final CreateOrderTableRequest createOrderTableRequest) {
        OrderTable persistOrderTable = orderTableDao.save(createOrderTableRequest.toOrderTable());
        return OrderTableResponse.from(persistOrderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll().stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        // TODO : 위의 쿼리 조건과 해당 로직 조건을 병합, e.g. findByIdAndTableGroupIsNull
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        // TODO : 도메인 외부가 아닌 내부에서 필드를 수정
        savedOrderTable.setEmpty(orderTable.isEmpty());

        // TODO : 도메인 내부에서 변경된 사항에 대한 감지를 통해 수정
        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        // TODO : 도메인 내부에서 변경된 사항에 대한 감지를 통해 수정
        return orderTableDao.save(savedOrderTable);
    }
}
