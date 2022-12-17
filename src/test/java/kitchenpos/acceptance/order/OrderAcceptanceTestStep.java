package kitchenpos.acceptance.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.function.ToLongFunction;

import org.assertj.core.util.Lists;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTestStep;
import kitchenpos.acceptance.menu.MenuAcceptanceTestStep;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.MenuResponse;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderResponse;
import kitchenpos.ui.dto.OrderStatusRequest;
import kitchenpos.ui.dto.OrderTableResponse;

public class OrderAcceptanceTestStep extends AcceptanceTestStep<OrderRequest, OrderResponse> {

	private static final String REQUEST_PATH = "/api/orders";
	private static final String ORDER_STATUS_REQUEST_PATH = "/api/orders/{orderId}/order-status";

	private MenuAcceptanceTestStep menus = new MenuAcceptanceTestStep();

	public OrderAcceptanceTestStep() {
		super(OrderResponse.class);
	}

	public ExtractableResponse<Response> 주문_상태_변경_요청(Long id, OrderStatus 식사중) {
		return 수정_요청(ORDER_STATUS_REQUEST_PATH, id, new OrderStatusRequest(식사중.name()));
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

	private static void 주문_조리중임(OrderResponse 주문) {
		assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
	}
}
