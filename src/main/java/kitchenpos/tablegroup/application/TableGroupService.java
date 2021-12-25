package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.application.OrderService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.NotFoundOrderTableException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupAddRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.exception.CanNotUngroupByOrderStatusException;
import kitchenpos.tablegroup.exception.NotFoundTableGroupException;

@Service
public class TableGroupService {

	private final OrderService orderService;
	private final OrderTableRepository orderTableRepository;
	private final TableGroupRepository tableGroupRepository;

	public TableGroupService(final OrderService orderService, final OrderTableRepository orderTableRepository,
		final TableGroupRepository tableGroupRepository) {
		this.orderService = orderService;
		this.orderTableRepository = orderTableRepository;
		this.tableGroupRepository = tableGroupRepository;
	}

	@Transactional
	public TableGroupResponse create(final TableGroupAddRequest request) {
		final List<OrderTable> orderTables = findOrderTables(request.getOrderTableIds());
		final TableGroup tableGroup = tableGroupRepository.save(
			request.toEntity(orderTables)
		);
		return TableGroupResponse.of(tableGroup);
	}

	private List<OrderTable> findOrderTables(List<Long> ids) {
		final List<OrderTable> orderTables = orderTableRepository.findAllById(ids);
		if (orderTables.size() != ids.size()) {
			throw new NotFoundOrderTableException();
		}
		return orderTables;
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		final TableGroup tableGroup = findTableGroup(tableGroupId);
		validateUngroup(tableGroup.getOrderTables());
		tableGroup.ungroup();
	}

	private TableGroup findTableGroup(Long id) {
		return tableGroupRepository.findById(id)
			.orElseThrow(NotFoundTableGroupException::new);
	}

	private void validateUngroup(final List<OrderTable> orderTables) {
		final List<Long> orderTableIds = orderTables.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());

		if (orderService.existsOrderStatusCookingOrMeal(orderTableIds)) {
			throw new CanNotUngroupByOrderStatusException();
		}
	}
}
