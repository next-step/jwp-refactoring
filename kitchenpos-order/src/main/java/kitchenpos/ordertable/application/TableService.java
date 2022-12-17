package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTableValidator;
import kitchenpos.ordertable.dto.TableRequest;
import kitchenpos.ordertable.dto.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {
    private static final String NOT_EXIST_TABLE = "테이블이 존재하지 않습니다.";

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(OrderTableRepository orderTableRepository, OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableResponse create(TableRequest request) {
        OrderTable savedTable = orderTableRepository.save(request.toEntity());
        return TableResponse.from(savedTable);
    }

    public List<TableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(TableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableResponse changeEmpty(Long orderTableId, boolean empty) {
        OrderTable savedOrderTable = findOrderTable(orderTableId);
        orderTableValidator.validateOrderStatus(savedOrderTable.getId());
        savedOrderTable.changeEmpty(empty);
        return TableResponse.from(savedOrderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(Long orderTableId, int numberOfGuests) {
        OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return TableResponse.from(savedOrderTable);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_TABLE));
    }
}
