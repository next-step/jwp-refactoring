package kitchenpos.acceptance.ordertable;

import java.util.function.ToLongFunction;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTestStep;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.OrderTableResponse;

public class OrderTableAcceptanceTestStep extends AcceptanceTestStep<OrderTableRequest, OrderTableResponse> {

	private static final String TABLES_REQUEST_PATH = "/api/tables";
	private static final String EMPTY_REQUEST_PATH = "/api/tables/{orderTableId}/empty";
	private static final String NUMBER_OF_GUESTS_REQUEST_PATH = "/api/tables/{orderTableId}/number-of-guests";

	public OrderTableAcceptanceTestStep() {
		super(OrderTableResponse.class);
	}

	@Override
	protected String getRequestPath() {
		return TABLES_REQUEST_PATH;
	}

	@Override
	protected ToLongFunction<OrderTableResponse> idExtractor() {
		return OrderTableResponse::getId;
	}

	public ExtractableResponse<Response> 빈_주문_테이블_수정_요청(Long id, OrderTableRequest request) {
		return super.수정_요청(EMPTY_REQUEST_PATH, id, request);
	}

	public ExtractableResponse<Response> 손님_수_수정_요청(Long id, OrderTableRequest request) {
		return super.수정_요청(NUMBER_OF_GUESTS_REQUEST_PATH, id, request);
	}
}
