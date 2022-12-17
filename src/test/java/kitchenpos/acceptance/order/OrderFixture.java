package kitchenpos.acceptance.order;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.ui.dto.MenuResponse;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderTableResponse;

public class OrderFixture {

	public static OrderRequest 주문(OrderTableResponse orderTable,
										List<MenuResponse> menus) {
		return new OrderRequest(orderTable.getId(),
								menus.stream()
									.collect(
										Collectors.toMap(MenuResponse::getId, menu -> 1, Integer::sum)));
	}
}
