package kitchenpos.acceptance.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.ui.dto.MenuResponse;
import kitchenpos.order.ui.dto.OrderRequest;
import kitchenpos.table.ui.dto.OrderTableResponse;

public class OrderFixture {

	public static OrderRequest 주문(OrderTableResponse orderTable,
								  List<MenuResponse> menus) {
		return new OrderRequest(orderTable.getId(),
								menus.stream()
									 .collect(
										 Collectors.toMap(MenuResponse::getId, menu -> 1, Integer::sum)));
	}
}
