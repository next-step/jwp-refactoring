package kitchenpos.application.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.acceptance.MenuAcceptanceTest.메뉴_생성_요청;
import static kitchenpos.application.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.application.acceptance.OrderAcceptanceTest.주문_상태_변경_요청;
import static kitchenpos.application.acceptance.OrderAcceptanceTest.주문_생성_요청;
import static kitchenpos.application.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static kitchenpos.application.acceptance.TableGroupAcceptanceTest.단체_지정_생성_요청;
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

    /**
     * Given 주문 테이블 2건을 등록하고 단체 지정을 생성한 후
     * When empty 로 상태를 변경하면
     * Then 오류가 발생한다.
     */
    @DisplayName("단체 지정 정보가 등록된 주문 테이블 상태 변경")
    @Test
    void changeEmptyOfNotEmptyTableGroupId() {
        // given
        OrderTable 주문_테이블_1 = 주문_테이블_생성_요청(1L, 3, true).as(OrderTable.class);
        OrderTable 주문_테이블_2 = 주문_테이블_생성_요청(2L, 2, true).as(OrderTable.class);
        단체_지정_생성_요청(Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // when
        ExtractableResponse<Response> 주문_테이블_빈_테이블로_변경_요청_응답
                = 주문_테이블_빈_테이블로_변경_요청(주문_테이블_1.getId(), 주문_테이블_1);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 주문_테이블_빈_테이블로_변경_요청_응답.statusCode());
    }

    /**
     * Given 주문을 생성한 후 주문의 상태를 MEAL 로 변경하고
     * When 주문에 해당하는 주문 테이블을 빈 테이블로 변경할 경우
     * Then 오류가 발생한다
     */
    @DisplayName("주문의 상태가 MEAL 인 주문 테이블을 빈 테이블로 변경")
    @Test
    void changeMealStatusTableToEmptyStatus() {
        // given
        OrderTable 주문_테이블_1 = 주문_테이블_생성_요청(1L, 3, true).as(OrderTable.class);
        OrderTable 주문_테이블_2 = 주문_테이블_생성_요청(2L,2, true).as(OrderTable.class);
        TableGroup 단체 = 단체_지정_생성_요청(Arrays.asList(주문_테이블_1, 주문_테이블_2)).as(TableGroup.class);

        MenuGroup 메뉴_그룹 = 메뉴_그룹_생성_요청("신메뉴").as(MenuGroup.class);
        Product 상품 = 상품_생성_요청("녹두빈대떡", new BigDecimal(7000)).as(Product.class);
        MenuProduct 메뉴_상품 = new MenuProduct();
        메뉴_상품.setProductId(상품.getId());
        메뉴_상품.setQuantity(1);

        Menu 메뉴 = 메뉴_생성_요청("녹두빈대떡", new BigDecimal(7000), 메뉴_그룹.getId(), Arrays.asList(메뉴_상품)).as(Menu.class);
        OrderLineItem 주문_항목 = new OrderLineItem();
        주문_항목.setMenuId(메뉴.getId());
        주문_항목.setQuantity(1);

        Order 주문 = 주문_생성_요청(주문_테이블_1.getId(), Arrays.asList(주문_항목)).as(Order.class);
        주문_상태_변경_요청(주문.getId(), OrderStatus.MEAL);

        // when
        ExtractableResponse<Response> 주문_테이블_빈_테이블로_변경_요청_응답
                = 주문_테이블_빈_테이블로_변경_요청(주문_테이블_1.getId(), 주문_테이블_1);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 주문_테이블_빈_테이블로_변경_요청_응답.statusCode());
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
