package kitchenpos.application.table;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.order.OrderService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderService orderService;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderService orderService, OrderTableRepository orderTableRepository) {
        this.orderService = orderService;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toOrderTable();
        orderTable.ungroup();

        orderTableRepository.save(orderTable);

        return OrderTableResponse.of(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = this.findById(orderTableId);
        validateExistStartedOrderReadyStatus(orderTableId);

        orderTable.changeEmpty(orderTableRequest.isEmpty());

        return OrderTableResponse.of(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = this.findById(orderTableId);
        orderTable.updateNumberOfGuests(orderTableRequest.getNumberOfGuests());

        return OrderTableResponse.of(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    private OrderTable findById(Long id) {
        return this.orderTableRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));
    }

    private void validateExistStartedOrderReadyStatus(Long orderTableId) {
        if (orderService.existsUnCompleteStatusByOrderTableId(orderTableId)) {
            throw new IllegalArgumentException("이미 주문 준비를 시작한 테이블이 있습니다.");
        }
    }

}
