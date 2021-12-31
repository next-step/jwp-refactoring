package kitchenpos.table.application;

import java.util.*;
import java.util.stream.*;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import kitchenpos.common.*;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.*;
import kitchenpos.table.repository.*;

@Service
public class TableService {
    private static final String ORDER_TABLE = "주문 테이블";
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
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

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException(ORDER_TABLE));

        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }
}
