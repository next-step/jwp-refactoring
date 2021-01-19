package kitchenpos.ordertable;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 테이블 기능")
public class OrderTableAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("시나리오1: 주문 테이블을 등록하고 관리할 수 있다.")
    public void scenarioTest() throws Exception {
        // when 주문 테이블 등록 요청
        ExtractableResponse<Response> 주문_테이블_등록 = 주문_테이블_등록_요청(0, true);
        // then 주문 테이블 등록됨
        주문_테이블_등록됨(주문_테이블_등록);

        // when 주문 테이블 목록 조회 요청
        ExtractableResponse<Response> 주문_테이블_목록_조회 = 주문_테이블_목록_조회_요청();
        // then 등록한 주문 테이블이 반영된 주문 테이블 목록이 조회됨
        주문_테이블_목록_조회됨(주문_테이블_목록_조회);

        // when 주문 테이블 상태 변경 요청
        ExtractableResponse<Response> 주문_테이블_상태_변경 = 주문_테이블_상태_변경_요청(주문_테이블_등록, false);
        // then 주문 테이블 상태 변경됨
        주문_테이블_상태_변경됨(주문_테이블_상태_변경);

        // when 주문 테이블 방문한 손님 수 입력 요청
        ExtractableResponse<Response> 주문_테이블_방문한_손님수_입력 = 주문_테이블_방문한_손님수_입력_요청(주문_테이블_등록, 4);
        // then 주문 테이블 방문한 손님 수 입력됨
        주문_테이블_방문한_손님수_입력됨(주문_테이블_방문한_손님수_입력);
    }

    public static ExtractableResponse<Response> 주문_테이블_등록_요청(int numberOfGuests, boolean empty) {
        OrderTableRequest orderTableRequest = new OrderTableRequest(numberOfGuests, empty);

        // when
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", OrderTableResponse.class)
                .stream()
                .map(OrderTableResponse::getNumberOfGuests)
                .anyMatch(it -> it.equals(0)))
                .isTrue();
    }

    public static ExtractableResponse<Response> 주문_테이블_상태_변경_요청(ExtractableResponse<Response> response, boolean empty) {
        OrderTableRequest orderTableRequest = new OrderTableRequest(0, empty);
        String location = response.header("Location");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().put(location + "/empty")
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_상태_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 주문_테이블_방문한_손님수_입력_요청(ExtractableResponse<Response> response, int numberOfGuests) {
        OrderTableRequest orderTableRequest = new OrderTableRequest(numberOfGuests, true);
        String location = response.header("Location");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().put(location + "/number-of-guests")
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_방문한_손님수_입력됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
