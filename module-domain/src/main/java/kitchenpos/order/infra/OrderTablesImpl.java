package kitchenpos.order.infra;

import org.springframework.stereotype.Component;

import kitchenpos.order.domain.OrderTables;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;

@Component
public class OrderTablesImpl implements OrderTables {
	private final OrderTableRepository orderTableRepository;

	public OrderTablesImpl(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	@Override
	public boolean contains(Long id) {
		return orderTableRepository.findById(id).isPresent();
	}

	@Override
	public kitchenpos.order.domain.OrderTable findById(Long id) {
		OrderTable orderTable = orderTableRepository.findById(id).orElseThrow(NoSuchFieldError::new);
		return kitchenpos.order.domain.OrderTable.from(orderTable.isEmpty());
	}
}
