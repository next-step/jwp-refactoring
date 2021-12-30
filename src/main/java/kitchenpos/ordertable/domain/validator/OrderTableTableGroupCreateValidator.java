package kitchenpos.ordertable.domain.validator;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.exception.IllegalOrderTableIdsException;
import kitchenpos.ordertable.infra.OrderTableRepository;
import kitchenpos.tablegroup.domain.validator.TableGroupCreateValidator;
import kitchenpos.tablegroup.exception.CanNotGroupException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableTableGroupCreateValidator implements TableGroupCreateValidator {
    private static final String IS_NOT_EMPTY_ERROR_MESSAGE = "주문 테이블이 빈상태일 떄만 단체지정을 생성할 수 있습니다.";
    private static final String ILLEGAL_IDS_ERROR_MESSAGE = "올바르지 않는 아이디 목록 입니다.";
    private final OrderTableRepository orderTableRepository;

    public OrderTableTableGroupCreateValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = validateExistOrderTableIds(orderTableIds);
        validateEmptyOrderTable(savedOrderTables);
    }


    private void validateEmptyOrderTable(List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty()) {
                throw new CanNotGroupException(IS_NOT_EMPTY_ERROR_MESSAGE);
            }
        }
    }

    private List<OrderTable> validateExistOrderTableIds(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalOrderTableIdsException(ILLEGAL_IDS_ERROR_MESSAGE);
        }
        return savedOrderTables;
    }
}

