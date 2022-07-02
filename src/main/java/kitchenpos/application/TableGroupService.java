package kitchenpos.application;

import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.validator.OrderValidator;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    @Autowired
    public TableGroupService(TableGroupRepository tableGroupRepository, OrderTableRepository orderTableRepository, OrderValidator orderValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        TableGroup.validateInputOrderTable(request.getOrderTables());

        final List<Long> orderTableIds = request.getOrderTables()
                                                .stream()
                                                .map(OrderTableRequest::getId)
                                                .collect(Collectors.toList());

        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        validatePresentOrderTable(request.getOrderTableSize(), orderTables.size());

        return tableGroupRepository.save(new TableGroup(orderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                                                    .orElseThrow(IllegalArgumentException::new);

        orderValidator.hasOrderStatusInCookingOrMeal(tableGroup.getOrderTables());

        tableGroup.ungroup();
    }

    private void validatePresentOrderTable(int inputSize, int presentSize) {
        if (inputSize != presentSize) {
            throw new IllegalArgumentException("요청 테이블과 실제 테이블 개수가 일치하지 않습니다.");
        }
    }
}
