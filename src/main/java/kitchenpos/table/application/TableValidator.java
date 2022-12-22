package kitchenpos.table.application;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.TableGroup;

@Component
public class TableValidator implements OrderValidator {

	private final OrderRepository orderRepository;

	public TableValidator(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public void validateUngroup(TableGroup tableGroup) {
		if (isCookingOrMeal(tableGroup)) {
			throw new IllegalArgumentException("조리중이거나 식사중인 테이블은 테이블 그룹 해제할 수 없습니다.");
		}
	}

	private boolean isCookingOrMeal(TableGroup tableGroup) {
		return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
			tableGroup.orderTableIds(),
			Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
	}

}
