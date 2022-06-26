package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("테이블 관련 인수테스트")
public class TableAcceptanceTest extends AcceptanceTest{

    private static final String TABLE_URL = "/api/tables/";

    private OrderTableResponse 빈테이블;
    private OrderTableResponse 손님_테이블;

    @TestFactory
    Stream<DynamicTest> tableTest() {
        return Stream.of(
                dynamicTest("빈 주문 테이블을 생성 한다.", () -> {
                    ExtractableResponse<Response> 테이블_생성_요청_응답 = 테이블_생성_요청(0, true);
                    주문_테이블_등록됨(테이블_생성_요청_응답);
                    빈테이블 = 테이블_생성_요청_응답.as(OrderTableResponse.class);
                }),
                dynamicTest("손님 있는 주문 테이블을 생성 한다.", () -> {
                    ExtractableResponse<Response> 테이블_생성_요청_응답 = 테이블_생성_요청(5, false);
                    주문_테이블_등록됨(테이블_생성_요청_응답);
                    손님_테이블 = 테이블_생성_요청_응답.as(OrderTableResponse.class);
                }),
                dynamicTest("손님수가 음수인 주문 테이블을 생성 한다.", () -> {
                    ExtractableResponse<Response> 테이블_생성_요청_응답 = 테이블_생성_요청(-1, false);
                    테이블_생성_실패됨(테이블_생성_요청_응답);

                }),
                dynamicTest("빈 주문 테이블의 손님수를 변경 한다.", () -> {
                    ExtractableResponse<Response> 테이블_손님수_변경_요청 = 테이블_손님수_변경_요청(빈테이블.getId(), 5);
                    테이블_손님수_변경됨(테이블_손님수_변경_요청);
                    OrderTableResponse actual = 테이블_손님수_변경_요청.as(OrderTableResponse.class);
                    변경된_테이블_손님수_확인(actual, 5);
                }),
                dynamicTest("손님 있는 주문 테이블을 빈테이블로 변경한다.", () -> {
                    ExtractableResponse<Response> 테이블_손님수_변경_요청 = 테이블_빈테이블로_변경_요청(손님_테이블.getId(), true);
                    테이블_손님수_변경됨(테이블_손님수_변경_요청);
                    OrderTableResponse actual = 테이블_손님수_변경_요청.as(OrderTableResponse.class);
                    빈테이블_확인(actual);
                }),
                dynamicTest("주문 테이블 목록을 조회 한다.", ()->{
                    ExtractableResponse<Response> 테이블_목록_조회_요청 = 테이블_목록_조회_요청();
                    테이블_목록_조회됨(테이블_목록_조회_요청);
                })
                );
    }

    public static ExtractableResponse<Response> 테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get(TABLE_URL)
                .then().log().all()
                .extract();
    }

    public static void 테이블_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 테이블_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 테이블_손님수_변경_요청(Long orderTableId, int numberOfGuests) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(OrderTableRequest.of(numberOfGuests))
                .when().put(TABLE_URL+orderTableId+"/number-of-guests")
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 테이블_빈테이블로_변경_요청(Long orderTableId, boolean empty) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(OrderTableRequest.of(empty))
                .when().put(TABLE_URL+orderTableId+"/empty")
                .then().log().all().
                extract();
    }

    public static void 테이블_손님수_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 변경된_테이블_손님수_확인(OrderTableResponse actual, int expectedNumbersOfGuests) {
        assertThat(actual.getNumberOfGuests()).isEqualTo(expectedNumbersOfGuests);
    }

    public static void 빈테이블_확인(OrderTableResponse actual) {
        assertThat(actual.isEmpty()).isTrue();
    }

    public static ExtractableResponse<Response> 테이블_생성_요청(int numberOfGuests, boolean empty) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(OrderTableRequest.of(numberOfGuests, empty))
                .when().post(TABLE_URL)
                .then().log().all().
                extract();
    }

    public static void 주문_테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
