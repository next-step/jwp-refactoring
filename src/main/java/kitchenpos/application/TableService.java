package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.*;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.NumberOfGuests;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderDao orderDao,
                        final OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable persistOrderTable = orderTableRepository.save(orderTableRequest.toEntity());
        return OrderTableResponse.of(persistOrderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.listOf(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("등록이 되지 않은 주문테이블은 상태를 변경할 수 없습니다."));

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리상태이거나 식사상태주문의 주문테이블은 상태를 변경할 수 없습니다.");
        }

        if (orderTableRequest.isEmpty()) {
            savedOrderTable.emptyOn();
            return OrderTableResponse.of(savedOrderTable);
        }

        savedOrderTable.emptyOff();
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("등록이 안된 주문테이블은 방문 손님 수를 수정할 수 없습니다."));
        NumberOfGuests numberOfGuests = NumberOfGuests.valueOf(orderTableRequest.getNumberOfGuests());
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.of(savedOrderTable);
    }
}
