package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final TableValidator tableValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(
        final TableValidator tableValidator,
        final OrderTableRepository orderTableRepository) {
        this.tableValidator = tableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableResponse create(final TableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toEntity();
        return TableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<TableResponse> list() {
        return TableResponse.from(orderTableRepository.findAll());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableRequest orderTable) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));

        savedOrderTable.changeEmpty(tableValidator, orderTable.isEmpty());
        return TableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableRequest orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("0 이하의 방문객 수로는 변경할 수 없습니다.");
        }
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));

        savedOrderTable.changeNumberOfGuests(tableValidator, numberOfGuests);
        return TableResponse.from(orderTableRepository.save(savedOrderTable));
    }
}
