package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문 테이블에 대한 인수 테스트")
class TableAcceptanceTest extends AcceptanceTest {
    private OrderTable 주문_테이블;
    private OrderTable 주문_테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블 = OrderTable.of(null, null, 3, true);
        주문_테이블2 = OrderTable.of(null, null, 1, true);
    }

    @DisplayName("주문 테이블을 등록한다")
    @Test
    void create_test() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_등록요청(주문_테이블);

        // then
        주문_테이블_등록됨(response);
    }

    @DisplayName("주문 테이블 목록을 조회한다")
    @Test
    void find_test() {
        // given
        주문_테이블_등록요청(주문_테이블);
        주문_테이블_등록요청(주문_테이블2);

        // when
        ExtractableResponse<Response> response = 주문_테이블_목록_조회요청();

        // then
        주문_테이블_목록_조회됨(response, 2);
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경한다")
    @Test
    void change_empty_test() {
        // given
        OrderTable orderTable = 주문_테이블_등록요청(주문_테이블).as(OrderTable.class);
        orderTable.setEmpty(false);

        // when
        ExtractableResponse<Response> response = 주문_테이블_빈테이블로_변경요청(orderTable.getId(), 주문_테이블2);

        // then
        주문_테이블_빈테이블로_변경됨(response);
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다")
    @Test
    void change_number_of_guests_test() {
        // given
        int 변경할_손님수 = 5;
        주문_테이블.setEmpty(false);
        주문_테이블2.setNumberOfGuests(변경할_손님수);
        OrderTable orderTable = 주문_테이블_등록요청(주문_테이블).as(OrderTable.class);

        // when
        ExtractableResponse<Response> response = 주문_테이블_손님수_변경요청(orderTable.getId(), 주문_테이블2);

        // then
        주문_테이블_손님수_변경됨(response, 변경할_손님수);
    }

    private ExtractableResponse<Response> 주문_테이블_등록요청(OrderTable orderTable) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().post("/api/tables")
            .then().log().all()
            .extract();
    }

    private void 주문_테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> 주문_테이블_목록_조회요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/tables")
            .then().log().all()
            .extract();
    }

    private void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response, int size) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<OrderTable> result = response.jsonPath().getList(".", OrderTable.class);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(size);
    }

    private ExtractableResponse<Response> 주문_테이블_빈테이블로_변경요청(Long orderTableId, OrderTable orderTable) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().put("/api/tables/{orderTableId}/empty", orderTableId)
            .then().log().all()
            .extract();
    }

    private void 주문_테이블_빈테이블로_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderTable result = response.as(OrderTable.class);
        assertTrue(result.isEmpty());
    }

    private ExtractableResponse<Response> 주문_테이블_손님수_변경요청(Long orderTableId, OrderTable orderTable) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
            .then().log().all()
            .extract();
    }

    private void 주문_테이블_손님수_변경됨(ExtractableResponse<Response> response, int numberOfGuests) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderTable result = response.as(OrderTable.class);
        assertThat(result.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }
}
