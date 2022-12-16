package kitchenpos.rest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.resource.UriResource;
import org.springframework.http.MediaType;

public class TableRestAssured {

    public static ExtractableResponse<Response> 주문_테이블_등록됨(int numberOfGuests) {
        return 주문_테이블_등록_요청(numberOfGuests, numberOfGuests < 1);
    }

    public static ExtractableResponse<Response> 주문_테이블_등록_요청(int numberOfGuests, boolean empty) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderTable(numberOfGuests, empty))
                .when().post(UriResource.주문_테이블_API.uri())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(UriResource.주문_테이블_API.uri())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_이용_가능_상태로_변경_요청(Long tableId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderTable(0, true))
                .when().put(UriResource.주문_테이블_API.uri() + "/{orderTableId}/empty", tableId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_이용_불가_상태로_변경_요청(Long tableId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderTable(0, false))
                .when().put(UriResource.주문_테이블_API.uri() + "/{orderTableId}/empty", tableId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_손님수_변경_요청(Long tableId, int numberOfGuests) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderTable(numberOfGuests, true))
                .when().put(UriResource.주문_테이블_API.uri() + "/{orderTableId}/number-of-guests", tableId)
                .then().log().all()
                .extract();
    }
}
