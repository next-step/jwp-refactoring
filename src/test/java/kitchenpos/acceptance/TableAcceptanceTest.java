package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTest.응답코드_확인;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableAcceptanceTest extends BaseAcceptanceTest{

    @DisplayName("주문 테이블을 관리한다")
    @Test
    public void manageOrderTable() {
        //주문  생성
        //given
        OrderTableRequest 주문테이블 = OrderTableRequest.of( 1, true);
        //when
        ExtractableResponse<Response> 주문테이블_생성_요청 = 주문테이블_생성_요청(주문테이블);
        //then
        응답코드_확인(주문테이블_생성_요청, HttpStatus.CREATED);

        //주문 테이블 목록 조회
        //when
        ExtractableResponse<Response> 주문테이블_목록조회_요청 = 주문테이블_목록조회_요청();
        //then
        응답코드_확인(주문테이블_목록조회_요청, HttpStatus.OK);
        주문테이블_조회됨(주문테이블_목록조회_요청, 주문테이블_생성_요청.as(OrderTableResponse.class).getId());

        //주문 테이블 빈 테이블 수정
        //given
        OrderTableRequest 수정테이블 =OrderTableRequest.of( 3, false);
        //when
        ExtractableResponse<Response> 주문테이블_빈테이블_수정_요청 = 주문테이블_빈테이블_수정_요청(주문테이블_생성_요청.as(OrderTableResponse.class).getId(), 수정테이블);
        //then
        응답코드_확인(주문테이블_빈테이블_수정_요청, HttpStatus.OK);
        빈테이블_변경됨(주문테이블_빈테이블_수정_요청, 수정테이블.isEmpty());

        //주문 테이블 손님 수 변경
        //given
        //when
        ExtractableResponse<Response> 주문테이블_손님수_변경_요청 = 주문테이블_손님수_변경_요청(주문테이블_생성_요청.as(OrderTableResponse.class).getId(), 수정테이블);
        //then
        응답코드_확인(주문테이블_손님수_변경_요청, HttpStatus.OK);
        손님수_변경됨(주문테이블_손님수_변경_요청, 수정테이블.getNumberOfGuests());
    }

    public static ExtractableResponse<Response> 주문테이블_생성_요청(OrderTableRequest orderTableRequest) {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTableRequest)
            .when().post("/api/tables")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 주문테이블_목록조회_요청() {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/tables")
            .then().log().all()
            .extract();
    }


    public static ExtractableResponse<Response> 주문테이블_빈테이블_수정_요청(Long orderTableId, OrderTableRequest orderTableRequest) {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTableRequest)
            .when().put("/api/tables/{orderTableId}/empty", orderTableId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 주문테이블_손님수_변경_요청(Long orderTableId, OrderTableRequest orderTableRequest) {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTableRequest)
            .when().put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
            .then().log().all()
            .extract();
    }

    public static void 주문테이블_조회됨(final ExtractableResponse<Response> response, Long id) {
        assertThat(response.jsonPath().getList(".", OrderTableResponse.class).stream()
            .anyMatch(orderTable -> orderTable.getId().equals(id))).isTrue();
    }

    public static void 빈테이블_변경됨(final ExtractableResponse<Response> response, boolean isEmpty) {
        assertThat(response.as(OrderTableResponse.class).isEmpty()).isEqualTo(isEmpty);
    }

    public static void 손님수_변경됨(final ExtractableResponse<Response> response, int numberOfGuests) {
        assertThat(response.as(OrderTableResponse.class).getNumberOfGuests()).isEqualTo(numberOfGuests);
    }
}
