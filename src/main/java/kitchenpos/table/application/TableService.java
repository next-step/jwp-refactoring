package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.validator.TableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class TableService {
    public static final String CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE = "변경하는 손님수는 0명보다 작을 수 없습니다.";
    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(final OrderTableRepository orderTableRepository, final TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTable orderTable) {
        orderTable.setTableGroup(null);
        OrderTable save = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(save);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId).orElseThrow(EntityNotFoundException::new);
        tableValidator.validateNotComplete(orderTable.getId());
        orderTable.empty();
        return OrderTableResponse.of(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsRequest request) {
        OrderTable orderTable = findOrderTable(orderTableId);
        orderTable.changeSitNumberOfGuest(request.getNumberOfGuests());
        return OrderTableResponse.of(orderTable);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
