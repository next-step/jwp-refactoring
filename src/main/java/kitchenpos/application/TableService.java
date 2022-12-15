package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.exception.AlreadyJoinedTableGroupException;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.OrderTableResponse;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderDao orderDao,
                        final OrderTableDao orderTableDao,
                        OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest request) {
        OrderTable2 orderTable = request.toOrderTable();
        orderTable = orderTableRepository.save(orderTable);

        return new OrderTableResponse(orderTable);
    }

    @Transactional
    public OrderTable2 create(final OrderTable2 orderTable) {
        orderTable.detachTableGroup();
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.of(orderTableRepository.findAll());
    }

    public List<OrderTable2> findAll() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableRequest request) {
        return new OrderTableResponse(changeEmpty(orderTableId, request.toOrderTable()));
    }

    @Transactional
    public OrderTable2 changeEmpty(Long orderTableId, OrderTable2 orderTable) {
        final OrderTable2 savedOrderTable = findById(orderTableId);

        if (savedOrderTable.hasTableGroup()) {
            throw new AlreadyJoinedTableGroupException();
        }

        // TODO check orderStatus is complete
        // if (orderDao.existsByOrderTableIdAndOrderStatusIn(
        //         orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
        //     throw new IllegalArgumentException();
        // }

        savedOrderTable.changeEmpty(orderTable.isEmpty());

        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableRequest request) {
        return new OrderTableResponse(changeNumberOfGuests(orderTableId, request.toOrderTable()));
    }

    @Transactional
    public OrderTable2 changeNumberOfGuests(Long orderTableId, OrderTable2 orderTable) {
        final OrderTable2 savedOrderTable = findById(orderTableId);

        savedOrderTable.changeNumberOfGuests(orderTable);

        return orderTable;
    }

    private OrderTable2 findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(EntityNotFoundException::new);
    }

    public List<OrderTable2> findAllById(List<Long> orderTableId) {
        return orderTableRepository.findAllById(orderTableId);
    }
}
