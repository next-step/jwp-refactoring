package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;

@Transactional
@Service
public class TableGroupService {
	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;
	private final TableGroupRepository tableGroupRepository;

	public TableGroupService(final OrderRepository orderRepository,
		final OrderTableRepository orderTableRepository,
		final TableGroupRepository tableGroupRepository) {
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
		this.tableGroupRepository = tableGroupRepository;
	}

	public TableGroupResponse create(final TableGroupRequest request) {
		List<OrderTable> tables = getOrderTables(request.getOrderTableIds());
		TableGroup tableGroup = TableGroup.create(tables);
		tableGroup = tableGroupRepository.save(tableGroup);
		return new TableGroupResponse(tableGroup);
	}

	private List<OrderTable> getOrderTables(List<Long> tableIds) {
		return orderTableRepository.findAllById(tableIds);
	}

	public void ungroup(final Long tableGroupId) {
		TableGroup tableGroup = getTableGroupById(tableGroupId);
		List<Long> orderTableIds = tableGroup.getOrderTables().toList().stream().map(OrderTable::getId).collect(
			Collectors.toList());
		if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
			orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
			throw new AppException(ErrorCode.WRONG_INPUT, "조리 중이거나 식사 중인 테이블이 있으면 단체 해제가 안됩니다");
		}
		tableGroup.unGroup();
	}

	private TableGroup getTableGroupById(Long tableGroupId) {
		return tableGroupRepository.findById(tableGroupId)
			.orElseThrow(() -> new AppException(ErrorCode.WRONG_INPUT, "단체를 찾을 수 없습니다"));
	}

}
