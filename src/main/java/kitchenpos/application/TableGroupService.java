package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
	private final OrderTableRepository orderTableRepository;
	private final TableGroupRepository tableGroupRepository;

	public TableGroupService(
		OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
		this.orderTableRepository = orderTableRepository;
		this.tableGroupRepository = tableGroupRepository;
	}

	@Transactional
	public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
		List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
		List<OrderTable> orderTables = orderTableIds.stream().map(this::findOrderTable).collect(Collectors.toList());
		TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
		return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
	}

	private OrderTable findOrderTable(Long id) {
		return orderTableRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("id에 해당하는 주문 테이블을 찾을 수 없습니다."));
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
			.orElseThrow(() -> new IllegalArgumentException("id에 해당하는 단체지정을 찾을 수 없습니다."));
		tableGroup.unGroup();
	}
}
