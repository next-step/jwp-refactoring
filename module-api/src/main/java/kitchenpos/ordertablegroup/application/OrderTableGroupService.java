package kitchenpos.ordertablegroup.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.ordertablegroup.domain.OrderTableGroup;
import kitchenpos.ordertablegroup.domain.OrderTableGroupRepository;
import kitchenpos.ordertablegroup.domain.OrderTableGroupValidator;
import kitchenpos.ordertablegroup.dto.OrderTableGroupCreateRequest;
import kitchenpos.ordertablegroup.dto.OrderTableGroupDto;

@Service
@Transactional(readOnly = true)
public class OrderTableGroupService {
	private final OrderTableGroupRepository orderTableGroupRepository;
	private final OrderTableGroupValidator orderTableGroupValidator;
	private final ApplicationEventPublisher applicationEventPublisher;

	public OrderTableGroupService(
		OrderTableGroupRepository orderTableGroupRepository,
		OrderTableGroupValidator orderTableGroupValidator,
		ApplicationEventPublisher applicationEventPublisher
	) {
		this.orderTableGroupRepository = orderTableGroupRepository;
		this.orderTableGroupValidator = orderTableGroupValidator;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Transactional
	public OrderTableGroupDto create(OrderTableGroupCreateRequest request) {
		OrderTableGroup orderTableGroup = orderTableGroupRepository.save(OrderTableGroup.newInstance());
		orderTableGroup.group(request.getOrderTableIds(), orderTableGroupValidator, applicationEventPublisher);
		orderTableGroupRepository.save(orderTableGroup);
		return OrderTableGroupDto.from(orderTableGroup);
	}

	@Transactional
	public void ungroup(Long id) {
		OrderTableGroup orderTableGroup = orderTableGroupRepository.findById(id)
			.orElseThrow(IllegalArgumentException::new);
		orderTableGroup.ungroup(orderTableGroupValidator, applicationEventPublisher);
		orderTableGroupRepository.save(orderTableGroup);
	}
}
