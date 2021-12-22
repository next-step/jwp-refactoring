package kitchenpos.tablegroup.application;

import kitchenpos.order.application.OrderStatusService;
import kitchenpos.tablegroup.domain.OrderTableIdsTableGroupValidator;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.exception.CanNotUnGroupException;
import kitchenpos.tablegroup.infra.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private static final String NOT_FOUND_TABLE_GROUP_ERROR_MESSAGE = "해당 단체 지정을 찾지 못하여 해산할 수 없습니다.";
    private static final String IS_COOKING_ERROR_MESSAGE = "조리나 식사 상태일 경우가 아닐 경우에만 해산 할 수 있습니다.";
    private final OrderStatusService orderStatusService;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableIdsTableGroupValidator orderTableIdsTableGroupValidator;

    public TableGroupService(OrderStatusService orderStatusService,
                             TableGroupRepository tableGroupRepository,
                             OrderTableIdsTableGroupValidator orderTableIdsTableGroupValidator) {
        this.orderStatusService = orderStatusService;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableIdsTableGroupValidator = orderTableIdsTableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTableIds();
        orderTableIdsTableGroupValidator.validate(orderTableIds);
        return TableGroupResponse.of(tableGroupRepository.save(request.toEntity()));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> {
                    throw new CanNotUnGroupException(NOT_FOUND_TABLE_GROUP_ERROR_MESSAGE);
                });
        if (orderStatusService.isCookingOrMealStateByOrderTableIds(tableGroup.getOrderTableIds())) {
            throw new CanNotUnGroupException(IS_COOKING_ERROR_MESSAGE);
        }
        tableGroupRepository.deleteById(tableGroupId);
    }
}
