package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderValidator {
	private final MenuRepository menuRepository;
	private final OrderTableRepository orderTableRepository;

	public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
		this.menuRepository = menuRepository;
		this.orderTableRepository = orderTableRepository;
	}

	public void validate(OrderRequest orderRequest) {
		if (orderRequest.getOrderLineItemRequestSize() != menuRepository.countByIdIn(orderRequest.getMenuIds())) {
			throw new IllegalArgumentException("존재하지 않는 메뉴가 포함되어 있습니다.");
		}

		if (!orderTableRepository.existsByIdAndEmptyFalse(orderRequest.getOrderTableId())) {
			throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다");
		}
	}
}
