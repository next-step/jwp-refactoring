package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableRequest;
import kitchenpos.dto.TableResponse;
import kitchenpos.exception.TableInUseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableService {
    private static final int EMPTY_COUNT = 0;

    private final OrderDao orderDao;
    private final TableRepository tableRepository;

    public TableService(OrderDao orderDao, TableRepository tableRepository) {
        this.orderDao = orderDao;
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
        OrderTable savedOrderTable = tableRepository.getOne(tableId);

        if (request.getNumberOfGuests() == EMPTY_COUNT) {
            validateEmptyTable(savedOrderTable);
        }

        savedOrderTable.update(request.getNumberOfGuests());
        return fromEntity(savedOrderTable);
    }

    public static TableResponse fromEntity(OrderTable table) {
        return TableResponse.builder()
                .id(table.getId())
                .tableGroupId(table.getTableGroupId())
                .numberOfGuests(table.getNumberOfGuests())
                .build();
    }

    private void validateEmptyTable(OrderTable savedOrderTable) {
        if (savedOrderTable.isGroupped()) {
            throw new TableInUseException("그룹이 지어진 테이블은 변경 할 수 없습니다.");
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(savedOrderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new TableInUseException("이용중인 테이블은 변경 할 수 없습니다.");
        }
    }
}
