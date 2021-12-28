package table.application;

import java.util.*;
import java.util.stream.*;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import common.*;
import order.domain.*;
import order.repository.*;
import table.domain.*;
import table.dto.*;
import table.repository.*;

@Service
public class TableService {
    private static final String ORDER_TABLE = "주문 테이블";
    private static final String ORDER_IS_NOT_COMPLETED_EXCEPTION_STATEMENT = "주문 완료가 되지 않았습니다.";

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse saveOrderTable(OrderTableRequest orderTableRequest) {
        OrderTable orderTable = OrderTable.of(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> findAll() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse cleanTable(final Long orderTableId) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException(ORDER_TABLE));

        checkCompletion(savedOrderTable);
        savedOrderTable.cleanTable();

        return OrderTableResponse.of(savedOrderTable);
    }

    private void checkCompletion(OrderTable savedOrderTable) {
        final List<Order> orders = orderRepository.findAllByOrderTable(savedOrderTable);
        boolean isCompleted = orders.stream()
            .allMatch(it -> it.getOrderStatus().equals(OrderStatus.COMPLETION));
        if (!isCompleted) {
            throw new IllegalArgumentException(ORDER_IS_NOT_COMPLETED_EXCEPTION_STATEMENT);
        }
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException(ORDER_TABLE));

        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }
}
