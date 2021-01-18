package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
import kitchenpos.menu.acceptance.MenuAcceptanceTest;
import kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTest;
import kitchenpos.ordertable.acceptance.TableAcceptanceTest;
import kitchenpos.product.acceptance.ProductAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTable orderTable;
    private Menu menu;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        orderTable = TableAcceptanceTest.주문_테이블_등록_되어있음(new OrderTable(3, false)).as(OrderTable.class);

        MenuGroup 추천메뉴 = MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음(new MenuGroup("추천메뉴")).as(MenuGroup.class);

        Product 양념치킨 = ProductAcceptanceTest.상품_등록되어_있음(new Product("양념치킨", BigDecimal.valueOf(16000))).as(Product.class);
        Product 후라이드치킨 = ProductAcceptanceTest.상품_등록되어_있음(new Product("후라이드치킨", BigDecimal.valueOf(15000))).as(Product.class);

        MenuProduct menuProduct_양념치킨 = new MenuProduct(양념치킨.getId(), 1);
        MenuProduct menuProduct_후라이드치킨 = new MenuProduct(후라이드치킨.getId(), 1);

        menu = MenuAcceptanceTest.메뉴_등록_되어있음("양념+후라이드",BigDecimal.valueOf(31000), 추천메뉴, Arrays.asList(menuProduct_양념치킨, menuProduct_후라이드치킨)).as(Menu.class);
    }

    @DisplayName("주문을 관리한다.")
    @Test
    void manageOrder() {
        // given
        Order order = new Order(orderTable.getId(), Arrays.asList(new OrderLineItem(menu.getId(), 1L)));

        // when
        ExtractableResponse<Response> createResponse = 주문_생성_요청(order);

        // then
        주문_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 주문_목록_조회_요청();

        // then
        주문_응답됨(findResponse);
        주문_목록_포함됨(findResponse, Arrays.asList(createResponse));

        order.setOrderStatus("COMPLETION");
        ExtractableResponse<Response> updateResponse = 주문_상태_변경_요청(createResponse, order);

        // then
        주문_응답됨(updateResponse);

        // when
        order.setOrderStatus("MEAL");
        ExtractableResponse<Response> wrongResponse = 주문_상태_변경_요청(createResponse, order);

        // then
        주문_응답_실패됨(wrongResponse);
    }

    public static ExtractableResponse<Response> 주문_생성_요청(Order order) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static void 주문_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static void 주문_응답됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_목록_포함됨(ExtractableResponse<Response> findResponse, List<ExtractableResponse<Response>> createResponses) {
        List<Long> createOrderIds = createResponses.stream()
                .map(create -> Long.parseLong(create.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> findOrderIds = findResponse.jsonPath().getList("id", Long.class);
        assertThat(findOrderIds).containsAll(createOrderIds);
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(ExtractableResponse<Response> createResponse, Order order) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().put(location + "/order-status")
                .then().log().all()
                .extract();
    }

    public static void 주문_응답_실패됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
