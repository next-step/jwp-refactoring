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
import static kitchenpos.application.acceptance.OrderTableAcceptanceTest.주문_테이블_생성_요청;
import static kitchenpos.application.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("단체 지정 관련 기능")
class TableGroupAcceptanceTest extends BaseAcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * Given 주문 테이블을 2건 생성 후
     * When 단체 지정을 생성하면
     * Then 단체가 생성된다.
     */
    @DisplayName("단체 지정 생성")
    @Test
    void createTableGroup() {
        // given
        OrderTable 주문_테이블_1 = 주문_테이블_생성_요청(3, true).as(OrderTable.class);
        OrderTable 주문_테이블_2 = 주문_테이블_생성_요청(2, true).as(OrderTable.class);

        // when
        ExtractableResponse<Response> 단체_지정_생성_요청_응답 = 단체_지정_생성_요청(Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), 단체_지정_생성_요청_응답.statusCode()),
                () -> assertNotNull(단체_지정_생성_요청_응답.jsonPath().get("id")),
                () -> assertThat(단체_지정_생성_요청_응답.jsonPath().getList("orderTables")).hasSize(2)
        );
    }

    /**
     * Given 주문 테이블을 1건 생성 후
     *       And 등록되지 않은 주문 테이블 정보 를 추가하고
     * When 단체 지정을 생성하면
     * Then 오류가 발생한다.
     */
    @DisplayName("등록되지 않은 주문 테이블 정보를 이용하여 생성")
    @Test
    void createTableGroupWithInvalidOrderTable() {
        // given
        OrderTable 주문_테이블_1 = 주문_테이블_생성_요청(3, true).as(OrderTable.class);
        OrderTable 주문_테이블_2 = new OrderTable();
        주문_테이블_2.setNumberOfGuests(2);
        주문_테이블_2.setEmpty(true);

        // when
        ExtractableResponse<Response> 단체_지정_생성_요청_응답 = 단체_지정_생성_요청(Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 단체_지정_생성_요청_응답.statusCode());
    }

    /**
     * Given 비어 있지 않은 주문 테이블을 2건 생성 후
     * When 단체 지정을 생성하면
     * Then 오류가 발생한다.
     */
    @DisplayName("비어 있지 않은 주문 테이블 정보를 이용하여 생성")
    @Test
    void createTableGroupWithNotEmptyOrderTable() {
        // given
        OrderTable 주문_테이블_1 = 주문_테이블_생성_요청(3, false).as(OrderTable.class);
        OrderTable 주문_테이블_2 = 주문_테이블_생성_요청(2, false).as(OrderTable.class);

        // when
        ExtractableResponse<Response> 단체_지정_생성_요청_응답 = 단체_지정_생성_요청(Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 단체_지정_생성_요청_응답.statusCode());
    }

    /**
     * Given 단체ID 가 null 이 아닌 주문 테이블을 2건 생성 후
     * When 단체 지정을 생성하면
     * Then 오류가 발생한다.
     */
    @DisplayName("단체ID 가 null 이 아닌 주문 테이블 정보를 이용하여 생성")
    @Test
    void createTableGroupWithNotEmptyTableGroupId() {
        // given
        OrderTable 주문_테이블_1 = 주문_테이블_생성_요청(1L, 3, false).as(OrderTable.class);
        OrderTable 주문_테이블_2 = 주문_테이블_생성_요청(2L,2, false).as(OrderTable.class);

        // when
        ExtractableResponse<Response> 단체_지정_생성_요청_응답 = 단체_지정_생성_요청(Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 단체_지정_생성_요청_응답.statusCode());
    }

        /**
         * Given 주문 테이블을 1건 생성 후
         * When 단체 지정을 생성하면
         * Then 오류가 발생한다.
         */
    @DisplayName("주문 테이블을 1건 생성 후 단체 지정 생성")
    @Test
    void createTableGroupWithOneOrderTable() {
        // given
        OrderTable 주문_테이블_1 = 주문_테이블_생성_요청(3, true).as(OrderTable.class);

        // when
        ExtractableResponse<Response> 단체_지정_생성_요청_응답 = 단체_지정_생성_요청(Arrays.asList(주문_테이블_1));

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 단체_지정_생성_요청_응답.statusCode());
    }

    /**
     * Given 주문 테이블을 2건 생성 후
     * When 단체 지정을 생성하면
     * Then 생성된 데이터 내 주문 테이블의 빈 테이블 여부가 false 로 지정된다.
     */
    @DisplayName("저장된 데이터와 요청 데이터의 주문 테이블의 개수가 다른 경우")
    @Test
    void createTableGroupWithEmptyFalse() {
        // given
        OrderTable 주문_테이블_1 = 주문_테이블_생성_요청(3, true).as(OrderTable.class);
        OrderTable 주문_테이블_2 = 주문_테이블_생성_요청(2, true).as(OrderTable.class);

        // when
        ExtractableResponse<Response> 단체_지정_생성_요청_응답 = 단체_지정_생성_요청(Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // then
        assertFalse(단체_지정_생성_요청_응답.jsonPath().getBoolean("orderTables.empty"));
    }

    /**
     * Given 단체와 주문을 생성한 후
     * When 단체의 주문 상태가 COMPLETION 인 경우
     * Then 단체가 해제된다.
     */
    @DisplayName("주문 상태가 COMPLETION 인 단체 해제")
    @Test
    void ungroupCompletionStatus() {
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
        주문_상태_변경_요청(주문.getId(), OrderStatus.COMPLETION);

        // when
        ExtractableResponse<Response> 단체_지정_해제_요청_응답 = 단체_지정_해제_요청(단체.getId());

        // then
        assertEquals(HttpStatus.NO_CONTENT.value(), 단체_지정_해제_요청_응답.statusCode());
    }

    /**
     * Given 단체와 주문을 생성한 후
     * When 단체의 주문 상태가 COOKING 인 경우
     * Then 오류가 발생한다
     */
    @DisplayName("주문 상태가 COOKING 인 단체 해제")
    @Test
    void ungroupCookingStatus() {
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
        주문_상태_변경_요청(주문.getId(), OrderStatus.COOKING);

        // when
        ExtractableResponse<Response> 단체_지정_해제_요청_응답 = 단체_지정_해제_요청(단체.getId());

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 단체_지정_해제_요청_응답.statusCode());
    }

    /**
     * Given 단체와 주문을 생성한 후
     * When 단체의 주문 상태가 MEAL 인 경우
     * Then 오류가 발생한다
     */
    @DisplayName("주문 상태가 MEAL 인 단체 해제")
    @Test
    void ungroupMealStatus() {
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
        ExtractableResponse<Response> 단체_지정_해제_요청_응답 = 단체_지정_해제_요청(단체.getId());

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 단체_지정_해제_요청_응답.statusCode());
    }



    public static ExtractableResponse<Response> 단체_지정_생성_요청(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        return RestAssured
                .given().log().all()
                .body(tableGroup)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 단체_지정_해제_요청(long tableGroupId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/table-groups/{tableGroupId}", tableGroupId)
                .then().log().all()
                .extract();
    }
}
