package kitchenpos.table.application;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {
    private static final String ERROR_MESSAGE_ORDER_TABLE_NOT_FOUND_FORMAT = "주문 테이블을 찾을 수 없습니다. ID : %d";

    private final TableValidator tableValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(TableValidator tableValidator, OrderTableRepository orderTableRepository) {
        this.tableValidator = tableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest request) {
        return OrderTableResponse.from(orderTableRepository.save(request.toEntity()));
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId) {
        OrderTable orderTable = findById(orderTableId);
        orderTable.validateChangeEmpty(tableValidator);
        orderTable.changeEmpty();
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        OrderTable orderTable = findById(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_ORDER_TABLE_NOT_FOUND_FORMAT,
                        orderTableId)));
    }
}
