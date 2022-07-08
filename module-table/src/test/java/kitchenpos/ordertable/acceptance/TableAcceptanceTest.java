package kitchenpos.ordertable.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.utils.RestAssuredMethods.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 관련 기능 인수테스트")
public class TableAcceptanceTest extends AcceptanceTest {
    private OrderTableRequest 테이블1_orderTableRequest;
    private OrderTableRequest 테이블2_orderTableRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();

        테이블1_orderTableRequest = OrderTableRequest.of(3, false);
        테이블2_orderTableRequest = OrderTableRequest.of(4, false);
    }

    /**
     * Feature: 테이블 관련 기능
     *
     *   Scenario: 테이블을 관리
     *     When 테이블1(not empty) 등록 요청
     *     Then 테이블1 등록됨
     *     When 테이블2(not empty) 등록 요청
     *     Then 테이블2 등록됨
     *     When 메뉴 조회 요청
     *     Then 테이블1, 테이블2 조회됨
     *     When 테이블1 '비어있음' 업데이트 (not empty → empty)
     *     Then 테이블1 업데이트됨
     *     When 테이블1 '비어있음' 업데이트 (empty → not empty)
     *     Then 테이블1 업데이트됨
     *     When 테이블2 손님 수 업데이트 (4명 → 5명)
     *     Then 테이블2 업데이트됨
     */
    @DisplayName("테이블을 관리한다")
    @Test
    void 테이블_관리_정상_시나리오() {
        ExtractableResponse<Response> 테이블1_등록 = 테이블_등록_요청(테이블1_orderTableRequest);
        테이블_등록됨(테이블1_등록);

        ExtractableResponse<Response> 테이블2_등록 = 테이블_등록_요청(테이블2_orderTableRequest);
        테이블_등록됨(테이블2_등록);

        ExtractableResponse<Response> 테이블_목록_조회 = 테이블_목록_조회_요청();
        테이블_목록_응답됨(테이블_목록_조회);
        테이블_목록_포함됨(테이블_목록_조회, Arrays.asList(테이블1_등록, 테이블2_등록));

        OrderTableRequest request_EMPTY = OrderTableRequest.of(0, true);
        ExtractableResponse<Response> 테이블_업데이트_to_EMPTY = 테이블_비어있음_수정_요청(테이블1_등록, request_EMPTY);
        테이블_수정됨(테이블_업데이트_to_EMPTY);

        OrderTableRequest request_NOTEMPTY = OrderTableRequest.of(1, false);
        ExtractableResponse<Response> 테이블_업데이트_to_NOTEMPTY = 테이블_비어있음_수정_요청(테이블1_등록, request_NOTEMPTY);
        테이블_수정됨(테이블_업데이트_to_NOTEMPTY);

        OrderTableRequest request_guest = OrderTableRequest.of(5, false);
        ExtractableResponse<Response> 테이블_업데이트_손님수 = 테이블_손님수_수정_요청(테이블2_등록, request_guest);
        테이블_수정됨(테이블_업데이트_손님수);
    }

    public static ExtractableResponse<Response> 테이블_등록_요청(OrderTableRequest params) {
        return post("/api/tables", params);
    }

    public static ExtractableResponse<Response> 테이블_목록_조회_요청() {
        return get("/api/tables");
    }

    public static ExtractableResponse<Response> 테이블_비어있음_수정_요청(ExtractableResponse<Response> response, OrderTableRequest params) {
        String uri = response.header("Location") + "/empty";
        return put(uri, params);
    }

    public static ExtractableResponse<Response> 테이블_손님수_수정_요청(ExtractableResponse<Response> response, OrderTableRequest params) {
        String uri = response.header("Location") + "/number-of-guests";
        return put(uri, params);
    }

    public static void 테이블_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 테이블_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 테이블_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedOrderTableIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultOrderTableIds = response.jsonPath().getList(".", OrderTableResponse.class).stream()
                .map(OrderTableResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultOrderTableIds).containsAll(expectedOrderTableIds);
    }

    public static void 테이블_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 테이블_등록되어_있음(int numberOfGuests, boolean empty) {
        return 테이블_등록_요청(OrderTableRequest.of(numberOfGuests, empty));
    }
}
