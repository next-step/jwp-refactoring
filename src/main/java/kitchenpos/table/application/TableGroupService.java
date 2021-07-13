package kitchenpos.table.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.TableGroupException;

@Service
public class TableGroupService {

	private final OrderRepository orderRepository;
	private final TableGroupRepository tableGroupRepository;
	private final TableRepository tableRepository;

	public TableGroupService(final OrderRepository orderRepository,
		final TableGroupRepository tableGroupRepository, final TableRepository tableRepository) {
		this.orderRepository = orderRepository;
		this.tableGroupRepository = tableGroupRepository;
		this.tableRepository = tableRepository;
	}

	@Transactional
	public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
		final List<OrderTable> savedOrderTables = tableRepository.findAllByIdIn(tableGroupRequest.getOrderTableIds());
		final TableGroup savedTableGroup = tableGroupRepository.save(tableGroupRequest.toTableGroup(savedOrderTables));
		return TableGroupResponse.of(savedTableGroup);
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		final TableGroup savedTableGroup = tableGroupRepository.findById(tableGroupId)
			.orElseThrow(() -> new TableGroupException("존재하지 않는 단체 지정입니다."));

		if(isUngroup(savedTableGroup.getOrderTables())){
			throw new TableGroupException("조리중이거나 식사중일 경우 단체지정을 해체할 수 없다.");
		}

		savedTableGroup.unGroup();

	}

	private boolean isUngroup(List<OrderTable> orderTables) {
		return orderTables.stream()
			.anyMatch(orderTable -> {
				Order order = orderRepository.findByOrderTableId(orderTable.getId());
				return order.getOrderStatus() != OrderStatus.COMPLETION;
			});
	}
}
