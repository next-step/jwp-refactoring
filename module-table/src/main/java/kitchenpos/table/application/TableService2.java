package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.domain.TableRepository;
import kitchenpos.table.domain.TableExternalValidator;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TableService2 {
    private final TableExternalValidator tableExternalValidator;
    private final TableRepository tableRepository;

    public TableService2(final TableExternalValidator tableExternalValidator, final TableRepository tableRepository) {
        this.tableExternalValidator = tableExternalValidator;
        this.tableRepository = tableRepository;
    }

    public TableResponse create(final TableRequest request) {
        return TableResponse.from(tableRepository.save(request.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<TableResponse> findAllTables() {
        return TableResponse.ofList(tableRepository.findAll());
    }

    public TableResponse changeEmpty(final Long orderTableId, final TableRequest request) {
        OrderTableEntity table = findTableById(orderTableId);
        tableExternalValidator.validateTableInUse(orderTableId);
        table.changeEmpty(request.isEmpty());
        return TableResponse.from(table);
    }

    private OrderTableEntity findTableById(Long orderTableId) {
        return tableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
    }

    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableRequest request) {
        OrderTableEntity table = findTableById(orderTableId);
        table.changeNumberOfGuests(request.getNumberOfGuests());
        return TableResponse.from(table);
    }
}
