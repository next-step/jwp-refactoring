package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("테이블 관련 인수테스트")
class TableAcceptanceTest extends AcceptanceTest {

    private static final String ORDER_TABLE_PATH = "/api/tables";

    /**
     * Feature: 테이블 기능
     *
     *   Scenario: 테이블 기능을 관리
     *     Given  요청할 주문 테이블을 생성하고
     *     When   테이블 등록 요청하면
     *     Then   주문 테이블이 등록된다.
     *     When   방문 손님 수를 업데이트 요청하면
     *     Then   방문 손님 수가 업데이트 된다
     *     And    손님 수가 0명 미만이면 실패한다.
     *
     *     Given  요청할 빈 테이블을 생성하고
     *     When   테이블 등록 요청하면
     *     Then   빈 테이블이 등록된다.
     *     When   방문 손님 수를 업데이트 요청하면
     *     Then   방문 손님 수 업데이트에 실패한다.
     *     When   빈 테이블 여부를 업데이트 요청하면
     *     Then   빈 테이블 여부가 업데이트 된다.
     *
     *     When   테이블 목록 조회 요청하면
     *     Then   테이블 목록이 조회된다.
     *
     * */
    @DisplayName("테이블 기능을 관리한다.")
    @TestFactory
    Stream<DynamicTest> manageOrderTable() {
        return Stream.of(
                dynamicTest("주문 테이블 기능을 관리한다.", () -> {
                    //given
                    Map<String, Object> params = 요청할_주문_테이블_생성(5);
                    //when
                    ExtractableResponse<Response> response = 테이블_등록_요청(params);

                    //then
                    테이블_등록됨(response);

                    //when
                    ExtractableResponse<Response> updateResponse1 = 방문_손님_수_업데이트_요청(response, 3);
                    ExtractableResponse<Response> updateResponse2 = 방문_손님_수_업데이트_요청(response, -1);

                    //then
                    방문_손님_수_업데이트됨(updateResponse1);
                    방문_손님_수_업데이트_실패됨(updateResponse2);

                }),

                dynamicTest("빈 테이블 관련 기능을 관리한다.", () -> {
                    //given
                    Map<String, Object> params = 요청할_빈_테이블_생성(0);

                    //when
                    ExtractableResponse<Response> response = 테이블_등록_요청(params);

                    //then
                    테이블_등록됨(response);

                    //when
                    ExtractableResponse<Response> updateResponse1 = 방문_손님_수_업데이트_요청(response, 3);

                    //then
                    방문_손님_수_업데이트_실패됨(updateResponse1);

                    //when
                    ExtractableResponse<Response> updateResponse2 = 빈_테이블_여부_업데이트_요청(response, false);

                    //then
                    빈_테이블_여부_업데이트됨(updateResponse2);

                }),

                dynamicTest("주문 테이블 목록을 조회한다.", () -> {
                    //when
                    ExtractableResponse<Response> response = 테이블_목록_조회_요청();

                    //then
                    테이블_목록_조회됨(response, 2, Arrays.asList(3, 0), false, false);
                })

        );

    }

    private ExtractableResponse<Response> 방문_손님_수_업데이트_요청(ExtractableResponse<Response> response, int numberOfGuests) {
        String location = response.header("Location");

        Map<String, Object> params = new HashMap<>();
        params.put("numberOfGuests", numberOfGuests);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(location + "/number-of-guests")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 빈_테이블_여부_업데이트_요청(ExtractableResponse<Response> response, boolean isEmpty) {
        String location = response.header("Location");

        Map<String, Object> params = new HashMap<>();
        params.put("empty", isEmpty);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(location + "/empty")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 테이블_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(ORDER_TABLE_PATH)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 테이블_등록_요청(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(ORDER_TABLE_PATH)
                .then().log().all()
                .extract();
    }

    private Map<String, Object> 요청할_주문_테이블_생성(int numberOfGuests) {
        Map<String, Object> params = new HashMap<>();
        params.put("numberOfGuests", numberOfGuests);
        params.put("empty", false);
        return params;
    }

    private Map<String, Object> 요청할_빈_테이블_생성(int numberOfGuests) {
        Map<String, Object> params = new HashMap<>();
        params.put("numberOfGuests", numberOfGuests);
        params.put("empty", true);
        return params;
    }

    private void 테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 테이블_목록_조회됨(ExtractableResponse<Response> response, int expectedSize,
                            List<Integer> expectedNumberOfGuests,
                            boolean... expectedEmpties) {

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("id", Long.class)).hasSize(expectedSize);
        assertThat(response.body().jsonPath().getList("numberOfGuests", Integer.class))
                .containsExactlyInAnyOrderElementsOf(expectedNumberOfGuests);
        assertThat(response.body().jsonPath().getBoolean("[0].empty")).isEqualTo(expectedEmpties[0]);
        assertThat(response.body().jsonPath().getBoolean("[1].empty")).isEqualTo(expectedEmpties[1]);
    }

    private void 빈_테이블_여부_업데이트됨(ExtractableResponse<Response> updateResponse) {
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 방문_손님_수_업데이트됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 방문_손님_수_업데이트_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
