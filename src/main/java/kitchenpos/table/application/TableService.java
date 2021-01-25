package kitchenpos.table.application;

import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.exception.EmptyOrderTableException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.InvalidOrderTablesException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    //private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        //this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable persistOrderTable = orderTableRepository.save(orderTableRequest.toOrderTable());
        return OrderTableResponse.of(persistOrderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

//    @Transactional
//    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
//        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
//                .orElseThrow(IllegalArgumentException::new);
//
//        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
//            throw new IllegalArgumentException();
//        }
//
//        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
//                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
//            throw new IllegalArgumentException();
//        }
//
//        savedOrderTable.setEmpty(orderTable.isEmpty());
//
//        return orderTableRepository.save(savedOrderTable);
//    }
//
//    @Transactional
//    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
//        final int numberOfGuests = orderTable.getNumberOfGuests();
//
//        if (numberOfGuests < 0) {
//            throw new IllegalArgumentException();
//        }
//
//        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
//                .orElseThrow(IllegalArgumentException::new);
//
//        if (savedOrderTable.isEmpty()) {
//            throw new IllegalArgumentException();
//        }
//
//        savedOrderTable.setNumberOfGuests(numberOfGuests);
//
//        return orderTableRepository.save(savedOrderTable);
//    }
//
    public OrderTables findOrderTablesById(List<Long> orderTableIds) {
        List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);

        checkExistsOrderTables(orderTableIds);

        return new OrderTables(savedOrderTables);
    }

    private void checkExistsOrderTables(List<Long> orderTableIds) {
        if (orderTableIds.size() != orderTableRepository.countByIdIn(orderTableIds)) {
            throw new NotFoundEntityException("등록되어 있지 않은 주문 테이블이 있습니다.");
        }
    }

    public OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundEntityException("해당 주문 테이블이 등록되어 있지 않습니다."));
    }
}
