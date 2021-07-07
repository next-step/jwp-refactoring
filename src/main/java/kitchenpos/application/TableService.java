package kitchenpos.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.exception.OrderTableNotFoundException;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderDao orderDao, OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest request) {
        return orderTableRepository.save(request.toEntity());
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = findById(orderTableId);

        if (orderDao.existsByOrderTableIdAndOrderStatusIn( // TODO 추후 리팩터링
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeEmpty(request.isEmpty());

        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTable) {
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());

        return savedOrderTable;
    }

    private OrderTable findById(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new OrderTableNotFoundException("해당 ID 의 주문 테이블이 존재하지 않습니다."));
    }
}
