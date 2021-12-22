package kitchenpos.tablegroup.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.tablegroup.domain.OrderTableIdsTableGroupValidator;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.infra.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderService orderService;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableIdsTableGroupValidator orderTableIdsTableGroupValidator;

    public TableGroupService(OrderService orderService, TableGroupRepository tableGroupRepository,
                             OrderTableIdsTableGroupValidator orderTableIdsTableGroupValidator) {
        this.orderService = orderService;
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
                    throw new IllegalArgumentException("해당 단체 지정을 찾지 못하였습니다.");
                });

        if (orderService.isCookingOrMealStateByOrderTableIds(tableGroup.getOrderTableIds())) {
            throw new IllegalArgumentException();
        }

        tableGroupRepository.deleteById(tableGroupId);
    }
}
