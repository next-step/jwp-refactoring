package kitchenpos.table.service;

import java.util.List;
import java.util.Objects;
import kitchenpos.table.domain.entity.OrderTable;
import kitchenpos.table.domain.entity.OrderTableRepository;
import kitchenpos.table.domain.value.NumberOfGuests;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.NotFoundOrderTableException;
import kitchenpos.table.exception.OrderTableHasTableGroupException;
import kitchenpos.table.exception.OrderTableIsEmptyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(OrderTableRepository orderTableRepository, TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    public OrderTableResponse create(OrderTableRequest orderTableRequest) {
        NumberOfGuests numberOfGuests = NumberOfGuests.of(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(orderTableRepository.save(new OrderTable(numberOfGuests)));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.ofList(orderTableRepository.findAll());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId,
        OrderTableRequest orderTableRequest) {

        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        //1. 합석테이블이 있는지(테이블그룹) 확인한다.
        validateOrderTableHasTableGroup(savedOrderTable);
        //2. 주문상태가 조리,식사 중인지 확인한다. 계산완료된 주문만 빈테이블로 만들수 있다.

        tableValidator.validateOrderStatusInCookingOrMeal(orderTableId);

        //3. 빈테이블로 만든다.
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        if (savedOrderTable.isEmpty()) {
            throw new OrderTableIsEmptyException();
        }
        savedOrderTable
            .changeNumberOfGuests(NumberOfGuests.of(orderTableRequest.getNumberOfGuests()));
        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    private void validateOrderTableHasTableGroup(OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new OrderTableHasTableGroupException();
        }
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(NotFoundOrderTableException::new);
    }


}
