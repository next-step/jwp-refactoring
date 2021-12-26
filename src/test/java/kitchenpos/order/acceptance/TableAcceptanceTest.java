package kitchenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.acceptance.RestAssuredApi;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.dto.TableRequest;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.order.dto.OrderStatusRequest.completion;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 테이블 인수 테스트")
class TableAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    @DisplayName("주문 테이블 정상 시나리오")
    void normalScenario() {
        String createdUri1 = 주문_테이블_등록됨(주문_테이블_등록_요청(TableRequest.from(2)));
        String createdUri2 = 주문_테이블_등록됨(주문_테이블_등록_요청(TableRequest.from(4)));

        주문_상태_변경됨(주문_상태_변경_요청(createdUri1, completion()));
        주문_테이블_비움(createdUri1);

        테이블_손님수_변경(createdUri2, TableRequest.from(3));

        ExtractableResponse<Response> response = 주문_테이블_목록_조회_요청();
        주문_테이블_목록_조회됨(response);
        주문_테이블_손님수_일치됨(response, Arrays.asList(0, 3));
        주문_테이블_상태_일치됨(response, Arrays.asList(true, false));
    }

    @Test
    @DisplayName("주문 테이블 예외 시나리오")
    void exceptionScenario() {
        ExtractableResponse<Response> response1 = 주문_테이블_등록_요청(TableRequest.from(2));
        ExtractableResponse<Response> response2 = 주문_테이블_등록_요청(TableRequest.from(4));
        String 테이블1 = 주문_테이블_등록됨(response1);
        String 테이블2 = 주문_테이블_등록됨(response2);

        주문_상태_변경됨(주문_상태_변경_요청(테이블1, completion()));
        주문_테이블_비움(테이블1);
        테이블_변경_실패됨(테이블_손님수_변경(테이블1, TableRequest.from(3)));

        테이블_변경_실패됨(테이블_손님수_변경(테이블2, TableRequest.from(-1)));
    }

    public static ExtractableResponse<Response> 주문_테이블_등록_요청(TableRequest request) {
        return RestAssuredApi.post("/api/tables", request);
    }

    private ExtractableResponse<Response> 주문_테이블_비움(String uri) {
        return RestAssuredApi.put(uri + "/empty");
    }

    private ExtractableResponse<Response> 테이블_손님수_변경(String uri, TableRequest request) {
        return RestAssuredApi.put(uri + "/number-of-guests", request);
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(String uri, OrderStatusRequest request) {
        return RestAssuredApi.put(uri + "/order-status", request);
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssuredApi.get("/api/tables");
    }

    private String 주문_테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.header("Location");
    }

    private void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 주문_테이블_손님수_일치됨(ExtractableResponse<Response> response, List<Integer> excepted) {
        assertThat(response.jsonPath().getList("numberOfGuests", Integer.class))
                .isEqualTo(excepted);
    }

    private void 주문_테이블_상태_일치됨(ExtractableResponse<Response> response, List<Boolean> excepted) {
        assertThat(response.jsonPath().getList("tableState"))
                .isEqualTo(excepted);
    }

    public static void 테이블_변경_실패됨(ExtractableResponse<Response> response) {
        AssertionsForClassTypes.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 주문_상태_변경됨(ExtractableResponse<Response> response) {
        AssertionsForClassTypes.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
