package kitchenpos.table.application;

import kitchenpos.exception.TableInUseException;
import kitchenpos.support.OrderSupport;
import kitchenpos.table.dao.TableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableService {
    private static final int EMPTY_COUNT = 0;

    private final OrderSupport orderSupport;
    private final TableRepository tableRepository;

    public TableService(OrderSupport orderSupport, TableRepository tableRepository) {
        this.orderSupport = orderSupport;
        this.tableRepository = tableRepository;
    }

    public TableResponse create() {
        return fromEntity(tableRepository.save(new OrderTable()));
    }

    @Transactional(readOnly = true)
    public List<TableResponse> list() {
        return tableRepository.findAll()
                .stream()
                .map(TableService::fromEntity)
                .collect(Collectors.toList());
    }

    public TableResponse update(Long tableId, TableRequest request) {
        OrderTable savedOrderTable = tableRepository.findById(tableId)
                .orElseThrow(EntityNotFoundException::new);

        if (request.getNumberOfGuests() == EMPTY_COUNT) {
            validateEmptyTable(savedOrderTable);
        }

        savedOrderTable.update(request.getNumberOfGuests());
        return fromEntity(savedOrderTable);
    }

    public static TableResponse fromEntity(OrderTable table) {
        return new TableResponse(table.getId(), table.getTableGroupId(), table.getNumberOfGuests());
    }

    private void validateEmptyTable(OrderTable savedOrderTable) {
        if (savedOrderTable.isGrouped()) {
            throw new TableInUseException("그룹이 지어진 테이블은 변경 할 수 없습니다.");
        }

        if (orderSupport.isUsingTable(savedOrderTable)) {
            throw new TableInUseException("이용중인 테이블은 변경 할 수 없습니다.");
        }
    }
}
