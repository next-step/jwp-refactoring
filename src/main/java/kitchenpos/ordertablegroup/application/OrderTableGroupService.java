package kitchenpos.ordertablegroup.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertablegroup.domain.OrderTableGroup;
import kitchenpos.ordertablegroup.domain.OrderTableGroupRepository;
import kitchenpos.ordertablegroup.dto.OrderTableGroupCreateRequest;
import kitchenpos.ordertablegroup.dto.OrderTableGroupDto;

@Service
@Transactional(readOnly = true)
public class OrderTableGroupService {
	private final OrderTableGroupRepository orderTableGroupRepository;
	private final OrderTableRepository orderTableRepository;

	public OrderTableGroupService(
		OrderTableGroupRepository orderTableGroupRepository,
		OrderTableRepository orderTableRepository
	) {
		this.orderTableGroupRepository = orderTableGroupRepository;
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderTableGroupDto create(OrderTableGroupCreateRequest request) {
		List<OrderTable> orderTables = findOrderTablesBy(request.getOrderTableIds());
		OrderTableGroup orderTableGroup = orderTableGroupRepository.save(OrderTableGroup.from(orderTables));
		return OrderTableGroupDto.from(orderTableGroup);
	}

	private List<OrderTable> findOrderTablesBy(List<Long> orderTableIds) {
		List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
		if (orderTables.size() != orderTableIds.size()) {
			throw new IllegalArgumentException();
		}
		return orderTables;
	}

	@Transactional
	public void ungroup(Long id) {
		OrderTableGroup orderTableGroup = orderTableGroupRepository.findById(id)
			.orElseThrow(IllegalArgumentException::new);

		orderTableGroup.ungroup();
	}
}
