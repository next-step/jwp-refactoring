package kitchenpos.menu.domain.fixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.domain.table.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.utils.AcceptanceTest;

import static kitchenpos.utils.AcceptanceFixture.*;

public class OrderTableDomainFixture extends AcceptanceTest {

    public static OrderTable 양식_테이블 = new OrderTable(0, true);
    public static OrderTableRequest 양식_테이블_요청 = OrderTableRequest.of(양식_테이블.getNumberOfGuests(), 양식_테이블.isEmpty());

    public static OrderTable 한식_테이블 = new OrderTable(0, true);
    public static OrderTableRequest 한식_테이블_요청 = OrderTableRequest.of(한식_테이블.getNumberOfGuests(), 한식_테이블.isEmpty());

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTableRequest orderTableRequest) {
        return post("/api/tables", orderTableRequest);
    }

    public static ExtractableResponse<Response> 주문_테이블_방문_손님_변경_요청(OrderTableRequest orderTableRequest, Object... pathParam) {
        return put("/api/tables/{orderTableId}/number-of-guests", orderTableRequest, pathParam);
    }

    public static ExtractableResponse<Response> 주문_테이블_비우기_요청(Object... pathParam) {
        return put("/api/tables/{orderTableId}/empty", pathParam);
    }

    public static ExtractableResponse<Response> 주문_테이블_조회_요청() {
        return get("/api/tables");
    }
}
