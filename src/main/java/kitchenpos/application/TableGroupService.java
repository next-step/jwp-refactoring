package kitchenpos.application;

import kitchenpos.common.NotFoundException;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest_Create;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
	static final String CANNOT_FIND_ORDER_TABLE = "Cannot find OrderTable by tableId";
	static final String MSG_CANNOT_FIND_TABLE_GROUP = "Cannot find TableGroup by tableGroupId";
	private final OrderTableDao orderTableDao;
	private final TableGroupDao tableGroupDao;

	public TableGroupService(final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
		this.orderTableDao = orderTableDao;
		this.tableGroupDao = tableGroupDao;
	}

	@Transactional
	public TableGroupResponse create(TableGroupRequest_Create request) {
		List<OrderTable> orderTables = orderTableDao.findAllByIdIn(request.getTableIds());
		if (request.getTableIds().size() != orderTables.size()) {
			throw new NotFoundException(CANNOT_FIND_ORDER_TABLE);
		}

		TableGroup tableGroup = tableGroupDao.save(TableGroup.fromGroupingTables(orderTables));
		return TableGroupResponse.of(tableGroup);
	}

	@Transactional
	public void ungroup(long tableGroupId) {
		TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_TABLE_GROUP));
		tableGroup.ungroupTables();
		tableGroupDao.delete(tableGroup);
	}
}
