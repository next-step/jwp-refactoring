package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.ErrorCode;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.exception.OrderException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.exception.TableException;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
	private final OrderTableRepository orderTableRepository;
	private final TableGroupRepository tableGroupRepository;

	public TableGroupService(final OrderTableRepository orderTableRepository,
		final TableGroupRepository tableGroupRepository) {
		this.orderTableRepository = orderTableRepository;
		this.tableGroupRepository = tableGroupRepository;
	}

	@Transactional
	public TableGroup create(final TableGroupRequest tableGroupRequest) {
		return tableGroupRepository.save(
			tableGroupRequest.toEntity(orderTablesFindIds(tableGroupRequest.getOrderTables())));
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		final TableGroup tableGroup = tableGroupFindById(tableGroupId);
		tableGroup.unGroup();
		tableGroupRepository.delete(tableGroup);
	}

	private TableGroup tableGroupFindById(Long id) {
		return tableGroupRepository.findById(id)
			.orElseThrow(() -> {
				throw new TableException(ErrorCode.TABLE_GROUP_IS_NULL);
			});
	}

	private OrderTable orderTableFindById(Long id) {
		return orderTableRepository.findById(id)
			.orElseThrow(() -> {
				throw new OrderException(ErrorCode.ORDER_TABLE_IS_NULL);
			});
	}

	private List<OrderTable> orderTablesFindIds(List<Long> ids) {
		return ids.stream()
			.map(this::orderTableFindById)
			.collect(Collectors.toList());
	}
}
