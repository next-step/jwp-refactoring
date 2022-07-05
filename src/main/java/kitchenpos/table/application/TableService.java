package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableChangeEmptyValidator;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class TableService {
    private static final String ORDER_TABLE_IS_NOT_EXIST = "주문테이블이 존재하지 않습니다";
    private final OrderTableRepository orderTableRepository;
    private final List<TableChangeEmptyValidator> tableChangeEmptyValidators;

    public TableService(OrderTableRepository orderTableRepository, List<TableChangeEmptyValidator> tableChangeEmptyValidators) {
        this.orderTableRepository = orderTableRepository;
        this.tableChangeEmptyValidators = tableChangeEmptyValidators;
    }

    @Transactional
    public TableResponse create(final TableRequest table) {
        OrderTable savedTable = orderTableRepository.save(table.toEntity());
        return new TableResponse(savedTable);
    }

    @Transactional(readOnly = true)
    public List<TableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(TableResponse::new)
                .collect(toList());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(ORDER_TABLE_IS_NOT_EXIST));
        tableChangeEmptyValidators.forEach(validator -> validator.validate(orderTableId));
        savedOrderTable.changeEmpty(empty);
        return new TableResponse(savedOrderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(ORDER_TABLE_IS_NOT_EXIST));
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return new TableResponse(savedOrderTable);
    }
}
