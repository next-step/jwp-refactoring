package kitchenpos.table.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.event.OrderTableGroupEvent;
import kitchenpos.table.event.OrderTableUngroupEvent;
import kitchenpos.table.exception.TableGroupException;

@Service
public class TableGroupService {

	private final TableGroupRepository tableGroupRepository;

	private final ApplicationEventPublisher eventPublisher;

	public TableGroupService(final TableGroupRepository tableGroupRepository,
		final ApplicationEventPublisher eventPublisher) {
		this.tableGroupRepository = tableGroupRepository;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
		final TableGroup savedTableGroup = tableGroupRepository.save(tableGroupRequest.toTableGroup());
		eventPublisher.publishEvent(new OrderTableGroupEvent(savedTableGroup, tableGroupRequest.getOrderTableIds()));
		return TableGroupResponse.of(savedTableGroup);
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
			.orElseThrow(() -> new TableGroupException("존재하지 않는 단체 지정입니다."));
		eventPublisher.publishEvent(new OrderTableUngroupEvent(tableGroup));
	}

}
