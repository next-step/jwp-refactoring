package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(OrderTableRepository orderTableRepository, OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = orderTableRequest.toOrderTable();

        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable persistOrderTable = findOrderTableById(orderTableId);
        persistOrderTable.changeEmpty(orderTableValidator, orderTableRequest.getEmpty());

        return OrderTableResponse.of(persistOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable persistOrderTable = findOrderTableById(orderTableId);
        persistOrderTable.changeNumberOfGuests(orderTableValidator, orderTableRequest.getNumberOfGuests());

        return OrderTableResponse.of(persistOrderTable);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new java.lang.IllegalArgumentException("해당 주문 테이블을 찾을 수 없습니다."));
    }
}
