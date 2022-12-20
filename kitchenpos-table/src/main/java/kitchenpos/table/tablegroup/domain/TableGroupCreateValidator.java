package kitchenpos.table.tablegroup.domain;

import kitchenpos.table.ordertable.domain.OrderTable;
import org.springframework.stereotype.Component;
import kitchenpos.table.ordertable.domain.OrderTableRepository;
import kitchenpos.table.tablegroup.dto.CreateTableGroupRequest;

import java.util.List;
import java.util.Objects;

@Component
public class TableGroupCreateValidator {

    private final OrderTableRepository tableRepository;

    public TableGroupCreateValidator(OrderTableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public void validate(CreateTableGroupRequest request) {
        final List<OrderTable> savedOrderTables = tableRepository.findAllByIdIn(request.getOrderTableIds());
        checkCreatable(savedOrderTables);
        checkedAlreadyExistGroup(savedOrderTables);
        checkedTableSize(savedOrderTables, request.getOrderTableIds());
    }

    private void checkCreatable(List<OrderTable> orderTableList) {
        validTableListSize(orderTableList);
        validEmptyTable(orderTableList);
        validNonNullTableGroupId(orderTableList);
    }

    private void validTableListSize(List<OrderTable> orderTableList) {
        if (orderTableList.isEmpty() || orderTableList.size() < 2) {
            throw new IllegalArgumentException("한 개 이상의 테이블이 있어야 합니다");
        }
    }

    private void validEmptyTable(List<OrderTable> orderTableList) {
        if (emptyTable(orderTableList)) {
            throw new IllegalArgumentException("단체 지정 주문 테이블에 비어 있는 주문 테이블이 포함 되어 있습니다");
        }
    }

    private boolean emptyTable(List<OrderTable> orderTableList) {
        return orderTableList.stream().anyMatch(OrderTable::isEmpty);
    }

    private void validNonNullTableGroupId(List<OrderTable> orderTableList) {
        if (checkNullTableGroupId(orderTableList)) {
            throw new IllegalArgumentException("단체 지정 값이 없는 주문 테이블이 포함되어 있습니다");
        }
    }

    private boolean checkNullTableGroupId(List<OrderTable> orderTableList) {
        return orderTableList.stream().anyMatch(it -> Objects.nonNull(it.getTableGroupId()));
    }

    private void checkedAlreadyExistGroup(List<OrderTable> savedOrderTables) {
        if (groupIdAlreadyExist(savedOrderTables)) {
            throw new IllegalArgumentException("이미 단체 지정 된 주문 테이블이 존재합니다");
        }
    }

    private boolean groupIdAlreadyExist(List<OrderTable> savedOrderTables) {
        return savedOrderTables.stream().anyMatch(it -> it.getTableGroupId() != null);
    }

    private void checkedTableSize(List<OrderTable> savedOrderTables, List<Long> request) {
        if (savedOrderTables.size() != request.size()) {
            throw new IllegalArgumentException("실제 저장 된 테이블 목록의 수와 요청한 테이블 목록의 수가 다릅니다");
        }
    }
}
