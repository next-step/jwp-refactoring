package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;

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

	@Override
	public void validateChangeEmpty(Long orderTableId) {
		if (isCookingOrMeal(orderTableId)) {
			throw new IllegalArgumentException("조리중이거나 식사중인 테이블은 빈 테이블로 변경할 수 없습니다.");
		}
	}
	private boolean isCookingOrMeal(Long orderTableId) {
		return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, cookingAndMealStatus());
	}

	private boolean isCookingOrMeal(TableGroup tableGroup) {
		return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
			tableGroup.orderTableIds(),
			cookingAndMealStatus());
	}
	private List<OrderStatus> cookingAndMealStatus() {
		return Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
	}
}
