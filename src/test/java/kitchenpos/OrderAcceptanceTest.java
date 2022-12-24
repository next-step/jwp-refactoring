package kitchenpos;

import static kitchenpos.MenuAcceptanceTest.메뉴_생성_요청;
import static kitchenpos.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.ProductAcceptanceTest.상품_생성_요청;
import static kitchenpos.TableAcceptanceTest.테이블_생성_요청;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTable 빈_테이블;
    private OrderTable 테이블;
    private Menu 페페로니피자;
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        테이블 = 테이블_생성_요청(false, 5).as(OrderTable.class);
        빈_테이블 = 테이블_생성_요청(true, 0).as(OrderTable.class);
        페페로니피자 = 메뉴_등록_요청().as(Menu.class);
    }

    /**
     * Feature: 주문 관련 기능
     *  Background:
     *      given: 테이블을 생성한다
     *      and: 빈테이블을 생성한다
     *      and: 메뉴를 생성한다
     * <p> 
     *  Scenario: 주문 추가 조회
     *      when: 주문을 생성한다.
     *      then: 주문이 생성된다.     
     *      when: 주문 항목 없이 주문을 생성한다
     *      then: 주문 생성이 실패됨
     *      when: 존재하지 않는 메뉴가 포함된 주문 항목으로 주문을 생성한다.
     *      then: 주문 생성이 실패됨
     *      when: 빈 테이블에 주문을 생성한다.
     *      then: 주문 생성이 실패됨
     *      when: 주문 목록을 조회한다
     *      then: 주문 목록이 조회됨
     *      when: 주문의 상태를 조리에서 식사로 변경한다
     *      then: 주문 상태가 변경됨
     *      when: 존재하지 않는 주문의 상태를 변경한다
     *      then: 주문 상태 변경이 실패됨
     */

    @DisplayName("주문 추가 조회")
    @Test
    void scenarioOrder() {
        // when
        ExtractableResponse<Response> 주문생성 = 주문_생성_요청(테이블, 페페로니피자);
        // then
        주문_생성됨(주문생성);
        주문_생성시_조리상태_확인(주문생성);
        Order 주문 = 주문생성.as(Order.class);

        // when
        ExtractableResponse<Response> 메뉴없이주문 = 주문_생성_요청(테이블);
        // then
        주문_생성_실패됨(메뉴없이주문);

        // given
        Menu 없는메뉴 = new Menu();
        없는메뉴.setId(Long.MAX_VALUE);
        // when
        ExtractableResponse<Response> 없는메뉴주문 = 주문_생성_요청(테이블, 없는메뉴);
        // then
        주문_생성_실패됨(없는메뉴주문);

        // when
        ExtractableResponse<Response> 빈테이블주문 = 주문_생성_요청(빈_테이블, 페페로니피자);
        // then
        주문_생성_실패됨(빈테이블주문);

        // when
        ExtractableResponse<Response> 조회 = 주문_목록_조회_요청();
        // then
        주문_목록_주문에_주문_항목이_포함됨(조회, 페페로니피자);

        // when
        ExtractableResponse<Response> 식사상태변경 = 주문_상태_변경_요청(주문, OrderStatus.MEAL);
        // then
        주문_상태_변경됨(식사상태변경);

        // given
        Order 없는주문 = new Order();
        없는주문.setId(Long.MAX_VALUE);
        // when
        ExtractableResponse<Response> 없는상태변경 = 주문_상태_변경_요청(없는주문, OrderStatus.MEAL);
        // then
        주문_상태_변경_실패됨(없는상태변경);
    }

    public static ExtractableResponse<Response> 주문_생성_요청(OrderTable orderTable, Menu... menus) {
        Map<String, Object> request = new HashMap<>();
        request.put("orderTableId", orderTable.getId());
        request.put("orderLineItems", toOrderLoneItems(menus));
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    private static List<OrderLineItem> toOrderLoneItems(Menu[] menus) {
        return Arrays.stream(menus)
                .map(m -> {
                    OrderLineItem orderLineItem = new OrderLineItem();
                    orderLineItem.setMenuId(m.getId());
                    orderLineItem.setQuantity(1L);
                    return orderLineItem;
                })
                .collect(Collectors.toList());
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(Order order, OrderStatus status) {
        Map<String, Object> request = new HashMap<>();
        request.put("orderStatus", status.name());
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/orders/{orderId}/order-status", order.getId())
                .then().log().all()
                .extract();
    }

    public static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_생성시_조리상태_확인(ExtractableResponse<Response> response) {
        Order order = response.as(Order.class);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    public static void 주문_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 주문_상태_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_상태_변경_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 주문_목록_주문에_주문_항목이_포함됨(ExtractableResponse<Response> response, Menu... menus) {
        List<Long> menuIds = response.jsonPath().getList(".", Order.class)
                .stream()
                .flatMap(o -> o.getOrderLineItems().stream())
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        List<Long> expectedIds = Arrays.stream(menus)
                .map(Menu::getId)
                .collect(Collectors.toList());
        assertThat(menuIds).containsExactlyElementsOf(expectedIds);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청() {
        MenuGroup 신메뉴 = 메뉴_그룹_생성_요청("신메뉴").as(MenuGroup.class);
        Product 파닭치킨 = 상품_생성_요청("파닭치킨", BigDecimal.valueOf(15_000L)).as(Product.class);

        return 메뉴_생성_요청("파닭치킨",
                BigDecimal.valueOf(15_000L),
                신메뉴.getId(), 파닭치킨);
    }
}
