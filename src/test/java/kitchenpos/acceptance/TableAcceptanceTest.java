package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문 테이블 관련 기능")
public class TableAcceptanceTest extends AcceptanceTest {

    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블_1 = 주문_테이블_등록되어_있음(2, true).as(OrderTable.class);
        주문_테이블_2 = 주문_테이블_등록되어_있음(4, false).as(OrderTable.class);
    }

    @Test
    @DisplayName("주문 테이블을 등록할 수 있다.")
    void create() {
        // given
        int 손님_수 = 2;
        boolean empty = true;

        // when
        ExtractableResponse<Response> response = 주문_테이블_등록_요청(손님_수, empty);

        // then
        주문_테이블_등록됨(response);
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    void list() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_목록_조회_요청();

        // then
        주문_테이블_목록_조회됨(response);
    }

    @Test
    @DisplayName("주문 테이블을 비울 수 있다.")
    void changeEmpty() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_비었는지_여부_변경_요청(주문_테이블_1.getId(), false);

        // then
        주문_테이블_비었는지_여부_변경됨(response);
    }

    @Test
    @DisplayName("주문 테이블에 방문한 손님 수를 변경할 수 있다.")
    void changeNumberOfGuests() {
        // when
        주문_테이블_비었는지_여부_변경_요청(주문_테이블_1.getId(), false);
        ExtractableResponse<Response> response = 주문_테이블_방문한_손님_수_변경_요청(주문_테이블_1.getId(), 3);

        // then
        주문_테이블_방문한_손님_수_변경됨(response);
    }

    public static ExtractableResponse<Response> 주문_테이블_등록되어_있음(int numberOfGuests, boolean empty) {
        return 주문_테이블_등록_요청(numberOfGuests, empty);
    }

    public static ExtractableResponse<Response> 주문_테이블_등록_요청(int numberOfGuests, boolean empty) {
        Map<String, Object> params = new HashMap<>();
        params.put("numberOfGuests", numberOfGuests);
        params.put("empty", empty);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 주문_테이블_비었는지_여부_변경_요청(Long orderTableId, boolean empty) {
        Map<String, Object> params = new HashMap<>();
        params.put("empty", empty);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/empty", orderTableId)
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_비었는지_여부_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    public static ExtractableResponse<Response> 주문_테이블_방문한_손님_수_변경_요청(Long orderTableId, int numberOfGuests) {
        Map<String, Object> params = new HashMap<>();
        params.put("numberOfGuests", numberOfGuests);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_방문한_손님_수_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
