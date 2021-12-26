package kitchenpos.tablegroup.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.NotFoundOrderTableException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupExternalValidator;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupAddRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.exception.NotFoundTableGroupException;

@Service
public class TableGroupService {

	private final TableGroupExternalValidator tableGroupExternalValidator;
	private final OrderTableRepository orderTableRepository;
	private final TableGroupRepository tableGroupRepository;

	public TableGroupService(TableGroupExternalValidator tableGroupExternalValidator,
		OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
		this.tableGroupExternalValidator = tableGroupExternalValidator;
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
		tableGroup.ungroup(tableGroupExternalValidator);
	}

	private TableGroup findTableGroup(Long id) {
		return tableGroupRepository.findById(id)
			.orElseThrow(NotFoundTableGroupException::new);
	}
}
