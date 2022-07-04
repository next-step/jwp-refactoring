package kitchenpos.table.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import kitchenpos.common.acceptance.BaseAcceptanceTest;
import kitchenpos.fixture.acceptance.AcceptanceTestOrderTableFixture;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableAcceptanceTest extends BaseAcceptanceTest {
    private AcceptanceTestOrderTableFixture 주문_테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문_테이블 = new AcceptanceTestOrderTableFixture();
    }

    @DisplayName("주문 테이블을 관리한다")
    @Test
    void manageOrderTable() {
        // when
        ExtractableResponse<Response> created1 = 주문_테이블_생성_요청(4, false);
        // then
        주문_테이블_생성됨(created1);

        // when
        ExtractableResponse<Response> created2 = 주문_테이블_생성_요청(0, true);
        // then
        주문_테이블_생성됨(created2);

        // when
        ExtractableResponse<Response> list = 주문_테이블_목록_조회_요청();
        // then
        주문_테이블_목록_조회됨(list);

        // when
        ExtractableResponse<Response> emptyUpdated = 주문_테이블_비었는지_여부_변경_요청(주문_테이블.빈_테이블1, 4, false);
        // then
        주문_테이블_비었는지_여부_변경됨(emptyUpdated);

        // when
        ExtractableResponse<Response> numberOfGuestsUpdated = 주문_테이블_손님_수_변경_요청(주문_테이블.테이블1, 2);
        // then
        주문_테이블_손님_수_변경됨(numberOfGuestsUpdated);
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(final int numberOfGuests, final boolean isEmpty) {
        final Map<String, Object> body = new HashMap<>();
        body.put("numberOfGuests", numberOfGuests);
        body.put("empty", isEmpty);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_비었는지_여부_변경_요청(final OrderTableResponse orderTable,
                                                                  final int numberOfGuests,
                                                                  final boolean isEmpty) {
        final Map<String, Object> body = new HashMap<>();
        body.put("id", orderTable.getId());
        body.put("tableGroupId", orderTable.getTableGroupId());
        body.put("numberOfGuests", numberOfGuests);
        body.put("empty", isEmpty);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().put("/api/tables/{orderTableId}/empty", orderTable.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_손님_수_변경_요청(final OrderTableResponse orderTable,
                                                                     final int numberOfGuests) {
        final Map<String, Object> body = new HashMap<>();
        body.put("id", orderTable.getId());
        body.put("tableGroupId", orderTable.getTableGroupId());
        body.put("numberOfGuests", numberOfGuests);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().put("/api/tables/{orderTableId}/number-of-guests", orderTable.getId())
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_테이블_목록_조회됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_테이블_비었는지_여부_변경됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_테이블_손님_수_변경됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
