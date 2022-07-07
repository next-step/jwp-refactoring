package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderTableAcceptanceFactory {


    public static ExtractableResponse<Response> 주문테이블_등록_요청(boolean isEmpty, int 손님의수) {
        OrderTableRequest orderTable = new OrderTableRequest(손님의수, isEmpty);

        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when()
                .post("/api/tables/")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블그룹에속한_주문테이블_등록_요청(boolean isEmpty, int 손님의수) {
        OrderTableRequest orderTable = new OrderTableRequest(손님의수, isEmpty);
//        orderTable.setTableGroupId(1L);

        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when()
                .post("/api/tables/")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문테이블_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/tables/")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문테이블_빈테이블로_변경요청(Long orderTableId, OrderTableRequest 주문테이블) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(주문테이블)
                .when()
                .put("/api/tables/{orderTableId}/empty", orderTableId)
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse<Response> 테이블_손님수_변경_요청(Long 주문테이블번호, int 인원수) {
        OrderTableRequest 변경요청테이블 = new OrderTableRequest(인원수);
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(변경요청테이블)
                .when()
                .put("/api/tables/{orderTableId}/number-of-guests", 주문테이블번호)
                .then().log().all()
                .extract();
    }


    public static void 주문테이블_등록성공(ExtractableResponse<Response> 주문테이블등록_결과) {
        assertThat(주문테이블등록_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문테이블_조회성공(ExtractableResponse<Response> 주문테이블조회_결과) {
        assertThat(주문테이블조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문테이블_빈테이블_변경성공(ExtractableResponse<Response> 주문테이블_빈테이블로_변경_결과) {
        assertThat(주문테이블_빈테이블로_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문테이블_빈테이블_변경실패(ExtractableResponse<Response> 주문테이블_빈테이블로_변경_결과) {
        assertThat(주문테이블_빈테이블로_변경_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 주문테이블_인원수_변경성공(ExtractableResponse<Response> 주문테이블_인원수변경_결과, Integer 변경요청_인원수) {
        OrderTable orderTable = 주문테이블_인원수변경_결과.as(OrderTable.class);
        assertAll(
                () -> assertThat(주문테이블_인원수변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(변경요청_인원수)
        );
    }

    public static void 주문테이블_인원수_변경실패(ExtractableResponse<Response> 주문테이블_인원수변경_결과) {
        assertThat(주문테이블_인원수변경_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


}
