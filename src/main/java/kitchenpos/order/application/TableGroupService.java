package kitchenpos.order.application;

import common.application.NotFoundException;
import domain.order.OrderTable;
import domain.order.OrderTableRepository;
import domain.order.TableGroup;
import domain.order.TableGroupRepository;
import kitchenpos.order.dto.TableGroupRequest_Create;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
	static final String CANNOT_FIND_ORDER_TABLE = "Cannot find OrderTable by tableId";
	static final String MSG_CANNOT_FIND_TABLE_GROUP = "Cannot find TableGroup by tableGroupId";
	private final OrderTableRepository orderTableRepository;
	private final TableGroupRepository tableGroupRepository;

	public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
		this.orderTableRepository = orderTableRepository;
		this.tableGroupRepository = tableGroupRepository;
	}

	@Transactional
	public TableGroupResponse create(TableGroupRequest_Create request) {
		List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(request.getOrderTables());
		if (request.getOrderTables().size() != orderTables.size()) {
			throw new NotFoundException(CANNOT_FIND_ORDER_TABLE);
		}

		TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
		tableGroup.groupTables(orderTables);
		return TableGroupResponse.of(tableGroup);
	}

	@Transactional
	public void ungroup(long tableGroupId) {
		TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_TABLE_GROUP));
		tableGroup.ungroupTables();
		tableGroupRepository.delete(tableGroup);
	}
}
