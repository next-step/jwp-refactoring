package kitchenpos.table.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderDao orderDao;

    public TableService(OrderTableRepository orderTableRepository, OrderDao orderDao) {
        this.orderTableRepository = orderTableRepository;
        this.orderDao = orderDao;
    }

    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = request.toOrderTable();
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream().map(OrderTableResponse::of).collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = this.findById(orderTableId);
        orderTable.checkValidEmptyTableGroup();
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        orderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.of(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = this.findById(orderTableId);
        orderTable.checkIsEmpty();
        orderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(orderTable);
    }

    private OrderTable findById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("미등록 주문 테이블 입니다."));
    }

    public OrderTables findAllByIds(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        return new OrderTables(orderTables);

    }

    public OrderTables findAllByTableGroupId(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        return new OrderTables(orderTables);
    }

    public OrderTable findOrderTable(Long orderTableId) {
        return findById(orderTableId);
    }
}
