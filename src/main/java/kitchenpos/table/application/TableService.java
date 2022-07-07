package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {
    private final OrderService orderService;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderService orderService, final OrderTableRepository orderTableRepository) {
        this.orderService = orderService;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = OrderTable.of(request.getNumberOfGuests(), request.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> listAll() {
        return OrderTableResponse.from(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);

        if (orderService.existOrderBeforeCompletion(savedOrderTable)) {
            throw new IllegalArgumentException("계산 완료되지 않은 주문이 있어서 빈 테이블로 만들 수 없습니다.");
        }

        savedOrderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        NumberOfGuests newNumberOfGuests = NumberOfGuests.from(request.getNumberOfGuests());

        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        savedOrderTable.changeNumberOfGuests(newNumberOfGuests);

        return OrderTableResponse.from(savedOrderTable);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("주문 테이블(%d)를 찾을 수 없습니다.", orderTableId)));
    }
}
