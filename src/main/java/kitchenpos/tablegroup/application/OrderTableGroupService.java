package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderTableGroupService {
	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;
	private final TableGroupRepository tableGroupRepository;

	public OrderTableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
		this.tableGroupRepository = tableGroupRepository;
	}

	@Transactional
	public TableGroupResponse create(final TableGroupRequest request) {
		List<Long> orderTableIds = new ArrayList<>(request.getOrderTableIds());
		List<OrderTable> orderTableList = orderTableRepository.findAllByIdIn(orderTableIds);

		OrderTables orderTables = new OrderTables(orderTableList, request.getOrderTableIds().size());

		TableGroup savedTableGroup = tableGroupRepository.save(request.toEntity(orderTables));
		return TableGroupResponse.of(savedTableGroup);
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(() -> new IllegalArgumentException());
		tableGroup.unTableGroup();
		tableGroupRepository.deleteById(tableGroup.getId());
	}
}
