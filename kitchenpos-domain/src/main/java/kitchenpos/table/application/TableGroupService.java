package kitchenpos.table.application;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.event.TableEvent;

@Transactional
@Service
public class TableGroupService {
	private final OrderTableRepository orderTableRepository;
	private final TableGroupRepository tableGroupRepository;
	private final TableGroupValidator tableGroupValidator;
	private final ApplicationEventPublisher eventPublisher;

	public TableGroupService(final OrderTableRepository orderTableRepository,
		final TableGroupRepository tableGroupRepository,
		TableGroupValidator tableGroupValidator,
		ApplicationEventPublisher eventPublisher) {

		this.orderTableRepository = orderTableRepository;
		this.tableGroupRepository = tableGroupRepository;
		this.tableGroupValidator = tableGroupValidator;
		this.eventPublisher = eventPublisher;
	}

	public TableGroupResponse create(final TableGroupRequest request) {
		tableGroupValidator.validateCreate(request.getOrderTableIds());
		TableGroup tableGroup = TableGroup.create();
		tableGroup = tableGroupRepository.save(tableGroup);
		List<OrderTable> tables = getOrderTables(request.getOrderTableIds());
		eventPublisher.publishEvent(TableEvent.Grouped.from(tableGroup, tables));
		return new TableGroupResponse(tableGroup, tables);
	}

	private List<OrderTable> getOrderTables(List<Long> tableIds) {
		return orderTableRepository.findAllById(tableIds);
	}

	public void ungroup(final Long tableGroupId) {
		TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
			.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "테이블 그룹을 찾을 수 없습니다"));
		eventPublisher.publishEvent(TableEvent.Ungrouped.from(tableGroup.getId()));
		tableGroupRepository.delete(tableGroup);
	}

}
