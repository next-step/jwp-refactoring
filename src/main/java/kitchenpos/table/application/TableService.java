package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dao.OrderTableRepository;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderService orderService;

    public TableService(final OrderTableRepository orderTableRepository, @Lazy final OrderService orderService) {
        this.orderTableRepository = orderTableRepository;
        this.orderService = orderService;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        return OrderTableResponse.from(
                orderTableRepository.save(request.of())
        );
    }

    public List<OrderTableResponse> list() {
        return new OrderTables(orderTableRepository.findAll()).toResponse();
    }

    public OrderTable getOrderTable(final Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id 는 null 이 아니여야 합니다.");
        }

        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 에 해당하는 주문 테이블을 찾을 수 없습니다."));
    }

    public OrderTables findOrderTablesByIds(final List<Long> ids) {
        return new OrderTables(orderTableRepository.findAllById(ids));
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean changedEmpty) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        OrderStatus orderStatus = orderService.getOrderStatusByOrderTableId(orderTableId);

        savedOrderTable.updateEmpty(changedEmpty, orderStatus);

        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        savedOrderTable.updateNumberOfGuests(new NumberOfGuests(numberOfGuests));

        return OrderTableResponse.from(savedOrderTable);
    }
}
