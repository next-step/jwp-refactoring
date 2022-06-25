package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTest.메뉴_등록_요청;
import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴그룹_등록_요청;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품_등록_요청;
import static kitchenpos.acceptance.TableAcceptanceTest.주문테이블_등록_요청;
import static kitchenpos.fixture.DomainFactory.createMenu;
import static kitchenpos.fixture.DomainFactory.createMenuGroup;
import static kitchenpos.fixture.DomainFactory.createMenuProduct;
import static kitchenpos.fixture.DomainFactory.createOrder;
import static kitchenpos.fixture.DomainFactory.createOrderLineItem;
import static kitchenpos.fixture.DomainFactory.createOrderTable;
import static kitchenpos.fixture.DomainFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
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
    private Order 주문;

    @BeforeEach
    public void setUp() {
        super.setUp();

        OrderTable 주문테이블 = 주문테이블_등록_요청(createOrderTable(1L, null, 5, false)).as(OrderTable.class);

        MenuGroup 빅맥세트 = 메뉴그룹_등록_요청(createMenuGroup(1L, "빅맥세트")).as(MenuGroup.class);
        Product 토마토 = 상품_등록_요청(createProduct(1L, "토마토", 1000)).as(Product.class);
        Product 양상추 = 상품_등록_요청(createProduct(2L, "양상추", 500)).as(Product.class);
        Menu 빅맥버거 = 메뉴_등록_요청(createMenu(1L, "빅맥버거", 3000, 빅맥세트.getId(),
                Arrays.asList(createMenuProduct(1L, 1L, 토마토.getId(), 1),
                        createMenuProduct(2L, 1L, 양상추.getId(), 4)))).as(Menu.class);

        주문 = createOrder(주문테이블.getId(), null, null, Arrays.asList(createOrderLineItem(1L, 1L, 빅맥버거.getId(), 1)));
    }

    /**
     * Feature: 주문들 관리한다.
     * <p>
     * Scenario: 주문 관리
     * <p>
     * When: 주문 등록 요청
     * <p>
     * Then: 주문이 등록됨
     * <p>
     * When: 주문 목록 조회 요청
     * <p>
     * Then: 주문 목록이 조회됨
     * <p>
     * When: 주문 상태 변경 요청
     * <p>
     * Then: 주문 상태가 변경됨
     */
    @Test
    void 주문_관리() {
        ExtractableResponse<Response> response;
        // when 주문테이블 등록 요청
        response = 주문_등록_요청(주문);
        // then 주문테이블 등록됨
        주문_등록됨(response);
        주문 = response.as(Order.class);
        
        // when 주문테이블 목록 조회 요청
        response = 주문_목록_조회();
        // then 주문테이블 목록이 조회됨
        주문_목록_조회됨(response);
        // then 주문테이블 목록이 조회됨
        주문_목록_포함됨(response, Arrays.asList(주문));

        // when 주문 테이블을 빈 테이블로 변경
        주문.setOrderStatus(OrderStatus.COMPLETION.name());
        response = 주문상태_변경_요청(주문);
        // then 주문 테이블이 빈 테이블로 변경됨
        주문상태_변경됨(response, 주문);
    }

    public static ExtractableResponse<Response> 주문_등록_요청(Order order) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_목록_조회() {
        return RestAssured
                .given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문상태_변경_요청(Order order) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().put("/api/orders/{orderId}/order-status", order.getId())
                .then().log().all()
                .extract();
    }

    public static void 주문_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_목록_포함됨(ExtractableResponse<Response> response, List<Order> expectedOrders) {
        List<Long> resultOrderIds = response.jsonPath().getList(".", Order.class).stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        List<Long> expectedOrderIds = expectedOrders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        assertThat(resultOrderIds).containsAll(expectedOrderIds);
    }

    public static void 주문상태_변경됨(ExtractableResponse<Response> response, Order order) {
        assertThat(response.as(Order.class).getOrderStatus()).isEqualTo(order.getOrderStatus());
    }
}
