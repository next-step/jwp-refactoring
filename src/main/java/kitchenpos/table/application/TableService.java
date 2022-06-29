package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dao.OrderTableRepository;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderService orderService;

    public TableService(final OrderTableRepository orderTableRepository, final OrderService orderService) {
        this.orderTableRepository = orderTableRepository;
        this.orderService = orderService;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        return orderTableRepository.save(request.of());
    }

    public OrderTables list() {
        return new OrderTables(orderTableRepository.findAll());
    }

    public OrderTable getOrderTable(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id 는 null 이 아니여야 합니다.");
        }

        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 에 해당하는 주문 테이블을 찾을 수 없습니다."));
    }

    public OrderTables findOrderTablesByIds(List<Long> ids) {
        return new OrderTables(orderTableRepository.findAllById(ids));
    }

    public OrderTables findOrderTablesByTableGroupId(Long tableGroupId) {
        return new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final boolean changedEmpty) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        OrderStatus orderStatus = orderService.getOrderStatusByOrderTableId(orderTableId);

        savedOrderTable.updateEmpty(changedEmpty, orderStatus);

        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        savedOrderTable.updateNumberOfGuests(new NumberOfGuests(numberOfGuests));

        return savedOrderTable;
    }
}
