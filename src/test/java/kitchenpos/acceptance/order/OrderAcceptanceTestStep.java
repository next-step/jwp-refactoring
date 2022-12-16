package kitchenpos.acceptance.order;

import java.util.ArrayList;
import java.util.function.ToLongFunction;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTestStep;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.MenuResponse;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderResponse;
import kitchenpos.ui.dto.OrderStatusRequest;
import kitchenpos.ui.dto.OrderTableResponse;

public class OrderAcceptanceTestStep extends AcceptanceTestStep<OrderRequest, OrderResponse> {

	static final String REQUEST_PATH = "/api/orders";
	static final String ORDER_STATUS_REQUEST_PATH = "/api/orders/{orderId}/order-status";

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
}
