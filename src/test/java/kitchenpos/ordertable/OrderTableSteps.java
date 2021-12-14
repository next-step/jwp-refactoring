package kitchenpos.ordertable;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;

import static kitchenpos.AcceptanceTest.*;

public class OrderTableSteps {

    private static final String ORDER_TABLE_URI = "/api/tables";

    public static OrderTableResponse 테이블_등록되어_있음(OrderTableRequest orderTableRequest) {
        return 테이블_등록_요청(orderTableRequest).as(OrderTableResponse.class);
    }

    public static ExtractableResponse<Response> 테이블_등록_요청(OrderTableRequest orderTableRequest) {
        return post(ORDER_TABLE_URI, orderTableRequest);
    }

    public static ExtractableResponse<Response> 테이블_목록_조회_요청() {
        return get(ORDER_TABLE_URI);
    }

    public static ExtractableResponse<Response> 테이블_주문_등록_가능_여부_변경_요청(Long orderTableId, OrderTableRequest orderTableRequest) {
        return put(ORDER_TABLE_URI + "/{orderTableId}/empty", orderTableRequest, orderTableId);
    }

    public static ExtractableResponse<Response> 테이블_방문한_손님_수_변경_요청(long orderTableId, OrderTableRequest orderTableRequest) {
        return put(ORDER_TABLE_URI + "/{orderTableId}/number-of-guests", orderTableRequest, orderTableId);
    }
}
