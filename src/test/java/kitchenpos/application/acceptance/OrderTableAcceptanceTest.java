package kitchenpos.application.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("주문 테이블 관련 기능")
class OrderTableAcceptanceTest extends BaseAcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * Given 손님의 수와 empty 유무를 입력하여
     * When 주문 테이블을 생성하면
     * Then 생성된 주문 테이블 데이터가 리턴된다
     *      And 단체 지정 ID (tableGroupId) 는 null 로 리턴된다.
     */
    @DisplayName("주문 테이블 생성")
    @Test
    void createOrderTable() {
        // given
        int numberOfGuests = 3;
        boolean empty = false;

        // when
        ExtractableResponse<Response> 주문_테이블_생성_요청_응답 = 주문_테이블_생성_요청(numberOfGuests, empty);

        // then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), 주문_테이블_생성_요청_응답.statusCode()),
                () -> assertNotNull(주문_테이블_생성_요청_응답.jsonPath().get("id")),
                () -> assertEquals(numberOfGuests, (Integer) 주문_테이블_생성_요청_응답.jsonPath().get("numberOfGuests")),
                () -> assertEquals(empty, 주문_테이블_생성_요청_응답.jsonPath().get("empty")),
                () -> assertNull(주문_테이블_생성_요청_응답.jsonPath().get("tableGroupId"))
        );
    }

    /**
     * Given 주문 테이블 1건을 등록한 후
     * When 주문 테이블 목록을 조회하면
     * Then 1건이 조회된다
     */
    @DisplayName("주문 테이블 목록 조회")
    @Test
    void list() {
        // given
        주문_테이블_생성_요청(3, true);

        // when
        ExtractableResponse<Response> 주문_테이블_목록_조회_요청_응답 = 주문_테이블_목록_조회_요청();

        // then
        assertThat(주문_테이블_목록_조회_요청_응답.jsonPath().getList("")).hasSize(1);
    }

    /**
     * Given 주문 테이블 1건을 등록한 후
     * When 주문 테이블을 빈 테이블로 변경하면
     * Then 주문 테이블이 빈 테이블로 변경된다.
     */
    @DisplayName("주문 테이블을 빈 테이블로 변경")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = 주문_테이블_생성_요청(3, false).as(OrderTable.class);
        orderTable.setEmpty(true);

        // when
        ExtractableResponse<Response> 주문_테이블_빈_테이블로_변경_요청_응답
                = 주문_테이블_빈_테이블로_변경_요청(orderTable.getId(), orderTable);

        // then
        assertTrue(주문_테이블_빈_테이블로_변경_요청_응답.as(OrderTable.class).isEmpty());
    }

    /**
     * Given 주문 테이블 1건을 등록한 후
     * When 주문 테이블의 손님 수를 변경하면
     * Then 주문 테이블의 손님 수가 변경된다.
     */
    @DisplayName("주문 테이블의 손님 수를 변경")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = 주문_테이블_생성_요청(3, false).as(OrderTable.class);
        orderTable.setNumberOfGuests(2);

        // when
        ExtractableResponse<Response> 주문_테이블_손님_수_변경_요청_응답
                = 주문_테이블_손님_수_변경_요청(orderTable.getId(), orderTable);

        // then
        assertEquals(2, 주문_테이블_손님_수_변경_요청_응답.as(OrderTable.class).getNumberOfGuests());
    }

    /**
     * Given 주문 테이블 1건을 등록한 후
     * When 주문 테이블의 손님 수를 음수로 변경하면
     * Then 오류가 발생한다.
     */
    @DisplayName("주문 테이블의 손님 수를 음수로 변경")
    @Test
    void changeNumberOfGuestsToNegativeNumber() {
        // given
        OrderTable orderTable = 주문_테이블_생성_요청(3, false).as(OrderTable.class);
        orderTable.setNumberOfGuests(-2);

        // when
        ExtractableResponse<Response> 주문_테이블_손님_수_변경_요청_응답
                = 주문_테이블_손님_수_변경_요청(orderTable.getId(), orderTable);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 주문_테이블_손님_수_변경_요청_응답.statusCode());
    }

    /**
     * Given 주문 테이블 1건을 등록한 후
     * When 생성한 주문 테이블이 아닌 임의의 주문 테이블 ID 를 전송하면
     * Then 오류가 발생한다.
     */
    @DisplayName("주문 테이블의 손님 수를 음수로 변경")
    @Test
    void changeNumberOfGuestsWithInvalidOrderTableId() {
        // given
        OrderTable orderTable = 주문_테이블_생성_요청(3, false).as(OrderTable.class);

        // when
        ExtractableResponse<Response> 주문_테이블_손님_수_변경_요청_응답
                = 주문_테이블_손님_수_변경_요청(100L, orderTable);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 주문_테이블_손님_수_변경_요청_응답.statusCode());
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_빈_테이블로_변경_요청(long orderTableId, OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/empty", orderTableId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_손님_수_변경_요청(long orderTableId, OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
                .then().log().all()
                .extract();
    }
}
