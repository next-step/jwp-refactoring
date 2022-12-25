package kitchenpos.order;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import org.springframework.http.MediaType;

public class TableFixture {

    public static final OrderTable 일번테이블 = new OrderTable(1L, 0, false);
    public static final OrderTable 빈테이블 = new OrderTable(2L, 0, true);

    public static ExtractableResponse<Response> 주문_테이블_추가(Long tableGroupId, int numberOfGuest,
        boolean empty) {

        OrderTableRequest orderTable = new OrderTableRequest(tableGroupId, numberOfGuest, empty);
        return 주문_테이블_추가(orderTable);
    }

    public static ExtractableResponse<Response> 주문_테이블_추가(OrderTableRequest orderTable) {
        return RestAssured
            .given().log().all()
            .body(orderTable)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/tables")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회() {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/tables")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_빈_테이블_상태_변경(Long orderTableId,
        boolean empty) {
        OrderTableRequest orderTable = new OrderTableRequest(null, null, 0, empty);

        return RestAssured
            .given().log().all()
            .body(orderTable)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/tables/" + orderTableId + "/empty")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블의_방문한_손님_수_변경(Long orderTableId,
        int numberOfGuest) {
        OrderTableRequest orderTable = new OrderTableRequest(null, null, numberOfGuest, false);

        return RestAssured
            .given().log().all()
            .body(orderTable)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/tables/" + orderTableId + "/number-of-guests")
            .then().log().all()
            .extract();
    }

    public static OrderTableRequest createOrderTableRequest(OrderTable orderTable) {
        return new OrderTableRequest(orderTable.getId(), orderTable.getNumberOfGuests(),
            orderTable.isEmpty());
    }
}
