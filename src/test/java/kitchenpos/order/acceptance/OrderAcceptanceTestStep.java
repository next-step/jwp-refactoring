package kitchenpos.order.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToLongFunction;

import org.assertj.core.util.Lists;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTestStep;
import kitchenpos.menu.acceptance.menu.MenuAcceptanceTestStep;
import kitchenpos.menu.ui.dto.MenuResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.dto.OrderRequest;
import kitchenpos.order.ui.dto.OrderResponse;
import kitchenpos.order.ui.dto.OrderStatusRequest;
import kitchenpos.table.ui.dto.OrderTableResponse;

public class OrderAcceptanceTestStep extends AcceptanceTestStep<OrderRequest, OrderResponse> {

	private static final String REQUEST_PATH = "/api/orders";
	private static final String ORDER_STATUS_REQUEST_PATH = "/api/orders/{orderId}/order-status";

	private final MenuAcceptanceTestStep menus = new MenuAcceptanceTestStep();

	public OrderAcceptanceTestStep() {
		super(OrderResponse.class);
	}

	private static void 주문_조리중임(OrderResponse 주문) {
		assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
	}

	public ExtractableResponse<Response> 주문_상태_변경_요청(Long id, OrderStatus orderStatus) {
		return 수정_요청(ORDER_STATUS_REQUEST_PATH, id, new OrderStatusRequest(orderStatus.name()));
	}

	@Override
	protected String getRequestPath() {
		return REQUEST_PATH;
	}

	@Override
	protected ToLongFunction<OrderResponse> idExtractor() {
		return OrderResponse::getId;
	}

	public OrderResponse 주문_등록됨(OrderTableResponse orderTable, ArrayList<MenuResponse> menus) {
		ExtractableResponse<Response> 등록_요청_응답 =
			등록_요청(OrderFixture.주문(orderTable, menus));
		return 등록됨(등록_요청_응답);
	}

	public OrderResponse 등록되어_있음(OrderTableResponse orderTable) {
		MenuResponse menuResponse = menus.등록되어_있음();
		OrderResponse 주문 = super.등록되어_있음(OrderFixture.주문(orderTable, Lists.newArrayList(menuResponse)));

		주문_조리중임(주문);

		return 주문;
	}

	public void 주문_상턔_확인(OrderResponse 주문, OrderStatus orderStatus) {
		assertThat(주문.getOrderStatus()).isEqualTo(orderStatus.name());
	}

	public void 주문_테이블_상태_변경됨(Long orderTableId, boolean empty, List<OrderTableResponse> allOrderTable) {
		OrderTableResponse orderTable = allOrderTable.stream()
															 .filter(it -> it.getId()
																			 .equals(orderTableId))
															 .findAny()
															 .get();
		assertThat(orderTable.getEmpty()).isEqualTo(empty);
	}
}
