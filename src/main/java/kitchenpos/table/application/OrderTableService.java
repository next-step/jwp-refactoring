package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.NotFoundOrderTableException;
import kitchenpos.table.util.OrderTableMapper;
import kitchenpos.table.util.OrderTableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderTableService {
    private final OrderTableMapper orderTableMapper;
    private final OrderTableValidator orderTableValidator;
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(OrderTableMapper orderTableMapper, OrderTableValidator orderTableValidator, OrderTableRepository orderTableRepository) {
        this.orderTableMapper = orderTableMapper;
        this.orderTableValidator = orderTableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableMapper.mapFormToOrderTable(orderTableRequest);
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId) {
        OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.validateToEmpty(orderTableValidator);
        return OrderTableResponse.of(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(orderTable);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundOrderTableException());
    }
}
