package kitchenpos.application;

import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.validator.OrderTableValidator;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    @Autowired
    public TableGroupService(TableGroupRepository tableGroupRepository, OrderTableRepository orderTableRepository, OrderTableValidator orderTableValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        validateRequestOrderTables(request.getOrderTables());

        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(getOrderTableIds(request));

        validatePresentOrderTable(request.getOrderTableSize(), orderTables.size());

        TableGroup saved = tableGroupRepository.save(new TableGroup());
        saved.group(orderTables);
        return saved;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                                                    .orElseThrow(IllegalArgumentException::new);

        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroup);
        validatePresentOrderTable(orderTables.size());
        orderTableValidator.hasOrderStatusInCookingOrMeal(orderTables);

        tableGroup.ungroup(orderTables);
    }

    private List<Long> getOrderTableIds(TableGroupRequest request) {
        return request.getOrderTables()
                      .stream()
                      .map(OrderTableRequest::getId)
                      .collect(Collectors.toList());
    }

    private void validateRequestOrderTables(List<OrderTableRequest> requests) {
        if (Objects.isNull(requests) || requests.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validatePresentOrderTable(int inputSize, int presentSize) {
        if (inputSize != presentSize) {
            throw new IllegalArgumentException("요청 테이블과 실제 테이블 개수가 일치하지 않습니다.");
        }
    }

    private void validatePresentOrderTable(int presentSize) {
        if (presentSize == 0) {
            throw new NoSuchElementException("단체 지정된 주문 테이블이 존재하지 않습니다.");
        }
    }
}
