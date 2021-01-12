package kitchenpos.ordertable;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 테이블 관련 기능")
public class OrderTableAcceptanceTest extends AcceptanceTest {


    @DisplayName("주문 테이블을 관리한다.")
    @Test
    void manage() {
        // when
        Map<String, Object> params = new HashMap<>();
        params.put("numberOfGuests", 0);
        params.put("empty", true);
        ExtractableResponse<Response> createResponse = 주문_테이블_생성_요청(params);

        // then
        주문_테이블_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 주문_테이블_목록_조회_요청();

        // then
        주문_테이블_응답됨(findResponse);
        주문_테이블_목록_포함됨(findResponse, Arrays.asList(createResponse));

        // when
        Map<String, Object> emptyParams = new HashMap<>();
        emptyParams.put("empty", false);
        ExtractableResponse<Response> emptyResponse = 주문_테이블_주문_상태_변경_요청(createResponse, emptyParams);

        // then
        주문_테이블_응답됨(emptyResponse);

        // when
        Map<String, Object> guestParams = new HashMap<>();
        emptyParams.put("numberOfGuests", 4);
        ExtractableResponse<Response> guestResponse = 주문_테이블_손님_수_변경_요청(createResponse, guestParams);

        // then
        주문_테이블_응답됨(guestResponse);
    }

    public static ExtractableResponse<Response> 주문_테이블_등록_되어있음(final int numberOfGuests, final boolean empty) {
        Map<String, Object> params = new HashMap<>();
        params.put("numberOfGuests", numberOfGuests);
        params.put("empty", empty);
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(params);
        주문_테이블_생성됨(response);
        return response;
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(final Map<String, Object> params) {
        return RestAssured
                .given().log().all().body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_주문_상태_변경_요청(final ExtractableResponse<Response> createResponse,
                                                                   final Map<String, Object> params) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all().body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(location + "/empty")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_테이블_손님_수_변경_요청(final ExtractableResponse<Response> createResponse,
                                                            final Map<String, Object> params) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all().body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(location + "/number-of-guests")
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_테이블_응답됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_테이블_목록_포함됨(final ExtractableResponse<Response> response,
                                     final List<ExtractableResponse<Response>> createResponses) {
        List<Long> expectedProductIds = createResponses.stream()
                .map(create -> Long.parseLong(create.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> actualIds = response.jsonPath().getList("id", Long.class);
        assertThat(actualIds).containsAll(expectedProductIds);
    }
}
