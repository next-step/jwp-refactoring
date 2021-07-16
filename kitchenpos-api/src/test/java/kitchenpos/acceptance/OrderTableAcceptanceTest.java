package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.table.OrderTableChangeEmptyRequest;
import kitchenpos.dto.table.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderTableAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 주문;

    @BeforeEach
    void setUpData() {
        // given
        OrderTableRequest request = new OrderTableRequest(5, false);

        // when
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(request);

        // then
        주문 = 주문_테이블_생성_성공(response);
        assertThat(주문).isNotNull()
                      .extracting(OrderTableResponse::getNumberOfGuests)
                      .isEqualTo(5);
    }


    @DisplayName("주문 테이블 생성 통합 테스트")
    @Test
    void createTest() {
        // given
        OrderTableRequest request = new OrderTableRequest(0, true);

        // when
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(request);

        // then
        OrderTableResponse actual = 주문_테이블_생성_성공(response);
        assertThat(actual).isNotNull()
                          .extracting(OrderTableResponse::getNumberOfGuests)
                          .isEqualTo(0);
    }

    @DisplayName("전체 주문 테이블 조회 통합 테스트")
    @Test
    void listTest() {
        // when
        ExtractableResponse<Response> response = 전체_주문_테이블_조회_요청();

        // then
        전체_주문_테이블_조회_성공(response);
    }

    @DisplayName("주문 테이블 빈 상태 변경 통합 테스트")
    @Test
    void changeEmptyTest() {
        // given
        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(false);

        // when
        ExtractableResponse<Response> response = 주문_테이블_상태_변경_요청(주문.getId(), request);

        // then
        OrderTableResponse actual = 주문_테이블_상태_변경_성공(response);
        assertThat(actual).isNotNull()
                          .extracting(OrderTableResponse::isEmpty)
                          .isEqualTo(false);
    }

    @DisplayName("주문 테이블 손님 수 변경 통합 테스트")
    @Test
    void changeNumberOfGuestsTest() {
        // given
        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(10);

        // when
        ExtractableResponse<Response> response = 주문_테이블_손님_수_변경_요청(주문.getId(), request);

        // then
        OrderTableResponse actual = 주문_테이블_손님_수_변경_성공(response);
        assertThat(actual).isNotNull()
                          .extracting(OrderTableResponse::getNumberOfGuests)
                          .isEqualTo(10);
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(final OrderTableRequest request) {
        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/api/tables")
                          .then().log().all().extract();
    }

    public static OrderTableResponse 주문_테이블_생성_성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(OrderTableResponse.class);
    }

    public static ExtractableResponse<Response> 전체_주문_테이블_조회_요청() {
        // when
        return RestAssured.given().log().all()
                          .when().get("/api/tables")
                          .then().log().all().extract();
    }

    public static void 전체_주문_테이블_조회_성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 주문_테이블_상태_변경_요청(final Long id,
                                                                final OrderTableChangeEmptyRequest request) {
        // when
        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().put(String.format("/api/tables/%s/empty", id))
                          .then().log().all().extract();
    }

    public static OrderTableResponse 주문_테이블_상태_변경_성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.as(OrderTableResponse.class);
    }

    public static ExtractableResponse<Response> 주문_테이블_손님_수_변경_요청(final Long id,
                                                                  final OrderTableChangeNumberOfGuestsRequest request) {
        // when
        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().put(String.format("/api/tables/%s/number-of-guests", id))
                          .then().log().all().extract();
    }

    public static OrderTableResponse 주문_테이블_손님_수_변경_성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.as(OrderTableResponse.class);
    }
}
