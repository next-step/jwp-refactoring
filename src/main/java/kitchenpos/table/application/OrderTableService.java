package kitchenpos.table.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class OrderTableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(OrderDao orderDao, OrderTableDao orderTableDao, OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        orderTable.setTableGroupId(null);

        return orderTableDao.save(orderTable);
    }

    @Transactional
    public OrderTableResponse createTemp(final OrderTableRequest orderTableRequest) {
        OrderTableEntity orderTableEntity = orderTableRepository.save(orderTableRequest.toEntity());

        return OrderTableResponse.of(orderTableEntity);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문 테이블입니다."));

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("단체 지정된 테이블입니다.");
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문이 조리나 식사 상태입니다.");
        }

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문한 고객 수는 0명 이상이어야 합니다.");
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문 테이블입니다."));

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 주문 테이블입니다.");
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return orderTableDao.save(savedOrderTable);
    }
}
