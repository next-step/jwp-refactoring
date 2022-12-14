package kitchenpos.tablegroup.application;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.tablegroup.validator.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository,
            OrderRepository orderRepository,
            TableGroupValidator tableGroupValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        tableGroupValidator.validateCreateTableGroup(request);

        TableGroup savedTableGroup = tableGroupRepository.save(request.createTableGroup());
        savedTableGroup.setOrderTableIds(request.getOrderTableIds());
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        tableGroupValidator.validateUnGroup(tableGroup);

        tableGroup.ungroup();
        tableGroupRepository.save(tableGroup);
    }

    private TableGroup findTableGroupById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.TABLE_GROUP_IS_NOT_EXIST.getMessage()));
    }
}
