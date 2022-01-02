package kitchenpos.fixture;

import static kitchenpos.common.AcceptanceFixture.*;

import io.restassured.response.*;
import kitchenpos.common.*;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.*;

public class OrderTableAcceptanceFixture extends AcceptanceTest {

    public static OrderTable 테이블_4인 = new OrderTable(0, true);
    public static OrderTableRequest 테이블_4인_요청 = OrderTableRequest.of(테이블_4인.getNumberOfGuests(), 테이블_4인.isEmpty());

    public static OrderTable 테이블_6인 = new OrderTable(0, true);
    public static OrderTableRequest 테이블_6인_요청 = OrderTableRequest.of(테이블_6인.getNumberOfGuests(), 테이블_6인.isEmpty());

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTableRequest orderTableRequest) {
        return post("/api/tables", orderTableRequest);
    }

    public static ExtractableResponse<Response> 주문_테이블_방문한_손님_수_변경_요청(OrderTableRequest orderTableRequest, Object... pathParam) {
        return put("/api/tables/{orderTableId}/number-of-guests", orderTableRequest, pathParam);
    }

    public static ExtractableResponse<Response> 주문_테이블_비우기_요청(Object... pathParam) {
        return put("/api/orders/{orderTableId}/empty", pathParam);
    }

    public static ExtractableResponse<Response> 주문_테이블_조회_요청() {
        return get("/api/tables");
    }
}
