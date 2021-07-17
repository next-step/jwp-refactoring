package kitchenpos.table.application;

import kitchenpos.common.Exception.EmptyException;
import kitchenpos.common.Exception.NotExistException;
import kitchenpos.common.Exception.UnchangeableException;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderService orderService;

    public OrderTableService(OrderTableRepository orderTableRepository, OrderService orderService) {
        this.orderTableRepository = orderTableRepository;
        this.orderService = orderService;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRepository.save(orderTableRequest.toEntity());

        return OrderTableResponse.of(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTableEntities = orderTableRepository.findAll();
        return OrderTableResponse.ofList(orderTableEntities);

    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findById(orderTableId);
        orderStatusValidCheck(savedOrderTable);
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findById(orderTableId);
        changeNumberOfGuestsValidCheck(orderTableRequest, savedOrderTable);
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();
        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(savedOrderTable);
    }

    private void orderStatusValidCheck(OrderTable orderTable) {
        if (orderService.changeStatusValidCheck(orderTable)) {
            throw new UnchangeableException("주문이 조리나 식사 상태에서는 변경할 수 없습니다.");
        }
    }

    public OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(() -> new NotExistException("등록되지 않은 주문 테이블입니다."));
    }

    private void changeNumberOfGuestsValidCheck(OrderTableRequest orderTableRequest, OrderTable savedOrderTable) {
        numberOfGuestsValidCheck(orderTableRequest.getNumberOfGuests());
        emptyValidCheck(savedOrderTable.isEmpty());

    }

    private void emptyValidCheck(boolean empty) {
        if (empty) {
            throw new EmptyException("빈 주문 테이블입니다.");
        }
    }

    private void numberOfGuestsValidCheck(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new UnchangeableException("방문 고객 수는 0명 이상이어야 합니다.");
        }
    }


    public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

}
