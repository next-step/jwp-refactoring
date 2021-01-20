package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.domain.model.OrderTable;
import kitchenpos.domain.model.OrderStatus;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.domain.repository.OrderDao;
import kitchenpos.domain.repository.OrderTableDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    public OrderTableDto create(final OrderTableCreateRequest orderTable) {
        OrderTable savedTable = orderTableDao.save(orderTable.toEntity());
        return OrderTableDto.of(savedTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableDto> list() {
        return orderTableDao.findAll().stream()
                .map(OrderTableDto::of)
                .collect(Collectors.toList());
    }

    public OrderTableDto changeEmpty(final Long orderTableId, boolean empty) {
        final OrderTable savedOrderTable = findById(orderTableId);

        validate(savedOrderTable);

        savedOrderTable.changeEmpty(empty);

        OrderTable savedTable = orderTableDao.save(savedOrderTable);

        return OrderTableDto.of(savedTable);
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = findById(orderTableId);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        OrderTable savedTable = orderTableDao.save(savedOrderTable);

        return OrderTableDto.of(savedTable);
    }

    private void validate(OrderTable savedOrderTable) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                savedOrderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문 테이블이 조리, 식사 상태일 경우에는 테이블 상태 비우기가 불가능합니다.");
        }
    }

    private OrderTable findById(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("등록 된 주문 테이블이 존재하지 않습니다."));
    }
}

