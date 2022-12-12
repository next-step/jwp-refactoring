package kitchenpos.table;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import org.springframework.http.MediaType;

public class TableFixture {

    public static ExtractableResponse<Response> 주문_테이블_추가(Long tableGroupId, int numberOfGuest,
        boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuest);
        orderTable.setEmpty(empty);

        return RestAssured
            .given().log().all()
            .body(orderTable)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/tables")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_빈_테이블_상태_변경(Long orderTableId,
        boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);

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
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuest);

        return RestAssured
            .given().log().all()
            .body(orderTable)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/tables/" + orderTableId + "/empty")
            .then().log().all()
            .extract();
    }
}
