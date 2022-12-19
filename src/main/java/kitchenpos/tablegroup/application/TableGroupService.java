package kitchenpos.tablegroup.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupedEvent;
import kitchenpos.tablegroup.domain.TableUnGroupedEvent;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@Service
public class TableGroupService {
	private final TableGroupRepository tableGroupRepository;
	private final ApplicationEventPublisher applicationEventPublisher;

	public TableGroupService(TableGroupRepository tableGroupRepository,
		ApplicationEventPublisher applicationEventPublisher) {
		this.tableGroupRepository = tableGroupRepository;
		this.applicationEventPublisher = applicationEventPublisher;

	}

	@Transactional
	public TableGroupResponse create(TableGroupRequest tableGroupRequest) {
		TableGroup saved = tableGroupRepository.save(TableGroup.of());
		applicationEventPublisher.publishEvent(
			new TableGroupedEvent(saved.getId(), tableGroupRequest.getOrderTableIds()));
		return TableGroupResponse.of(saved, tableGroupRequest.getOrderTableIds());
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		TableGroup tableGroup = findTableGroupById(tableGroupId);
		tableGroupRepository.delete(tableGroup);
		applicationEventPublisher.publishEvent(new TableUnGroupedEvent(tableGroupId));
	}

	private TableGroup findTableGroupById(Long tableGroupId) {
		return tableGroupRepository.findById(tableGroupId).orElseThrow(
			() -> new EntityNotFoundException(TableGroup.ENTITY_NAME, tableGroupId)
		);
	}

}
