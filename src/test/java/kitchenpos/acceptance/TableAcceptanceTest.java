package kitchenpos.acceptance;

import static kitchenpos.acceptance.support.TableAcceptanceSupport.등록한_주문_테이블_검증됨;
import static kitchenpos.acceptance.support.TableAcceptanceSupport.주문_테이블_등록요청;
import static kitchenpos.acceptance.support.TableAcceptanceSupport.주문_테이블_목록_조회됨;
import static kitchenpos.acceptance.support.TableAcceptanceSupport.주문_테이블_목록_조회요청;
import static kitchenpos.acceptance.support.TableAcceptanceSupport.주문_테이블_빈테이블로_변경됨;
import static kitchenpos.acceptance.support.TableAcceptanceSupport.주문_테이블_빈테이블로_변경요청;
import static kitchenpos.acceptance.support.TableAcceptanceSupport.주문_테이블_손님수_변경됨;
import static kitchenpos.acceptance.support.TableAcceptanceSupport.주문_테이블_손님수_변경요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.request.OrderTableRequest;
import kitchenpos.table.domain.response.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문 테이블에 대한 인수 테스트")
class TableAcceptanceTest extends AcceptanceTest {
    private OrderTable 주문_테이블;
    private OrderTable 주문_테이블2;

    private OrderTableRequest 주문_테이블_request;
    private OrderTableRequest 주문_테이블2_request;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블 = OrderTable.of(null, null, 3, true);
        주문_테이블2 = OrderTable.of(null, null, 1, true);

        주문_테이블_request = new OrderTableRequest(
            주문_테이블.getId(),
            주문_테이블.getTableGroupId(),
            주문_테이블.getNumberOfGuests(),
            주문_테이블.isEmpty());

        주문_테이블2_request = new OrderTableRequest(
            주문_테이블2.getId(),
            주문_테이블2.getTableGroupId(),
            주문_테이블2.getNumberOfGuests(),
            주문_테이블2.isEmpty());
    }

    @DisplayName("주문 테이블을 등록한다")
    @Test
    void create_test() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_등록요청(주문_테이블);

        // then
        등록한_주문_테이블_검증됨(response);
    }

    @DisplayName("주문 테이블을 등록한다")
    @Test
    void create_test_copy() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_등록요청_copy(주문_테이블_request);

        // then
        등록한_주문_테이블_검증됨(response);
    }

    public static ExtractableResponse<Response> 주문_테이블_등록요청_copy(OrderTableRequest orderTable) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().post("/api/tables/copy")
            .then().log().all()
            .extract();
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

    @DisplayName("주문 테이블 목록을 조회한다")
    @Test
    void find_test_copy() {
        // given
        주문_테이블_등록요청_copy(주문_테이블_request);
        주문_테이블_등록요청_copy(주문_테이블2_request);

        // when
        ExtractableResponse<Response> response = 주문_테이블_목록_조회요청_copy();

        // then
        주문_테이블_목록_조회됨_copy(response, 2);
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회요청_copy() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/tables/copy")
            .then().log().all()
            .extract();
    }

    public static void 주문_테이블_목록_조회됨_copy(ExtractableResponse<Response> response, int size) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<OrderTableResponse> result = response.jsonPath().getList(".", OrderTableResponse.class);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(size);
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

    @DisplayName("주문 테이블을 빈 테이블로 변경한다")
    @Test
    void change_empty_test_copy() {
        // given
        OrderTableResponse orderTable = 주문_테이블_등록요청_copy(주문_테이블_request).as(OrderTableResponse.class);

        // when
        ExtractableResponse<Response> response = 주문_테이블_빈테이블로_변경요청_copy(orderTable.getId());

        // then
        주문_테이블_빈테이블로_변경됨_copy(response);
    }

    public static ExtractableResponse<Response> 주문_테이블_빈테이블로_변경요청_copy(Long orderTableId) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/tables/{orderTableId}/empty/copy", orderTableId)
            .then().log().all()
            .extract();
    }

    public static void 주문_테이블_빈테이블로_변경됨_copy(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderTableResponse result = response.as(OrderTableResponse.class);
        assertTrue(result.isEmpty());
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

    @DisplayName("주문 테이블의 손님 수를 변경한다")
    @Test
    void change_number_of_guests_test_copy() {
        // given
        주문_테이블_request = new OrderTableRequest(
            null,
            주문_테이블.getTableGroupId(),
            주문_테이블.getNumberOfGuests(),
            false);
        OrderTableResponse orderTable = 주문_테이블_등록요청_copy(주문_테이블_request).as(OrderTableResponse.class);

        int 변경할_손님수 = 5;
        OrderTableRequest 변경할_주문_request = new OrderTableRequest(null, null, 변경할_손님수, true);

        // when
        ExtractableResponse<Response> response = 주문_테이블_손님수_변경요청_copy(orderTable.getId(), 변경할_주문_request);

        // then
        주문_테이블_손님수_변경됨_copy(response, 변경할_손님수);
    }

    public static ExtractableResponse<Response> 주문_테이블_손님수_변경요청_copy(Long orderTableId, OrderTableRequest orderTable) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().put("/api/tables/{orderTableId}/number-of-guests/copy", orderTableId)
            .then().log().all()
            .extract();
    }

    public static void 주문_테이블_손님수_변경됨_copy(ExtractableResponse<Response> response, int numberOfGuests) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderTableResponse result = response.as(OrderTableResponse.class);
        assertThat(result.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    public static OrderTable 주문_테이블_등록됨(OrderTable orderTable) {
        ExtractableResponse<Response> response = 주문_테이블_등록요청(orderTable);
        return response.as(OrderTable.class);
    }
}
