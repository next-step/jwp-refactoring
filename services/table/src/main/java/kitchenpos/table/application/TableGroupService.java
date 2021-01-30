package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupDao;
import kitchenpos.table.dto.TableGroupReponse;
import kitchenpos.table.dto.TableGroupRequest;

@Service
@Transactional
public class TableGroupService {
	private final TableService tableService;
	private final TableGroupDao tableGroupDao;

	public TableGroupService(final TableService tableService,
		final TableGroupDao tableGroupDao) {
		this.tableService = tableService;
		this.tableGroupDao = tableGroupDao;
	}

	public TableGroupReponse create(final TableGroupRequest request) {
		Set<OrderTable> orderTables = Optional.ofNullable(request.getOrderTables())
			.orElseGet(Collections::emptyList)
			.stream().map(it -> tableService.findById(it.getId())
			)
			.collect(Collectors.toSet());

		return TableGroupReponse.from(tableGroupDao.save(new TableGroup.Builder()
			.createdDate(LocalDateTime.now())
			.orderTables(orderTables)
			.build()));
	}

	public void ungroup(final Long tableGroupId) {
		final Set<OrderTable> orderTables = tableService.findAllByTableGroupId(tableGroupId);

		for (OrderTable orderTable : orderTables) {
			orderTable.ungroup();
		}
	}
}
