package kichenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kichenpos.menu.ui.dto.*;
import kichenpos.order.domain.OrderStatus;
import kichenpos.order.ui.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

import static kichenpos.order.acceptance.TableAcceptanceTest.주문_테이블;
import static kichenpos.order.acceptance.TableAcceptanceTest.주문_테이블_등록됨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("주문 관리")
public class OrderAcceptanceTest extends AcceptanceTest {
    private OrderCreateRequest 주문;
    private OrderUpdateRequest 식사중;

    @BeforeEach
    public void setUp() {
        super.setUp();

        Long 주문_테이블_id = 주문_테이블_등록됨(주문_테이블).getId();
        MenuCreateRequest 메뉴_요청_생성 = 메뉴_요청_생성();
        Long 메뉴_id = 메뉴_등록됨(메뉴_요청_생성).getId();

        주문 = new OrderCreateRequest(주문_테이블_id, Collections.singletonList(new OrderLineItemCreateRequest(메뉴_id, 메뉴_요청_생성.getName(), 메뉴_요청_생성.getPrice(), 2)));
        식사중 = new OrderUpdateRequest(OrderStatus.MEAL.name());
    }

    @TestFactory
    Stream<DynamicTest> 주문_관리_시나리오() {
        return Stream.of(
                dynamicTest("주문을 등록한다.", this::주문을_등록한다),
                dynamicTest("주문 상태를 변경한다", this::주문_상태를_변경한다),
                dynamicTest("주문 목록을 조회한다", this::주문_목록을_조회한다)
        );
    }

    private void 주문을_등록한다() {
        // when
        ExtractableResponse<Response> 주문_등록_응답 = 주문_등록_요청(주문);

        // then
        주문_등록됨(주문_등록_응답);
    }

    private void 주문_상태를_변경한다() {
        // when
        ExtractableResponse<Response> 주문_상태_변경_응답 = 주문_상태_변경_요청(식사중);

        // then
        주문_상태_변경됨(주문_상태_변경_응답);
    }

    private void 주문_목록을_조회한다() {
        // when
        ExtractableResponse<Response> 주문_목록_조회_응답 = 주문_목록_조회_요청();

        // then
        주문_목록_조회됨(주문_목록_조회_응답);
    }

    public static ExtractableResponse<Response> 주문_등록_요청(OrderCreateRequest request) {
        return post("/api/orders", request);
    }

    public static void 주문_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(OrderUpdateRequest request) {
        return put("/api/orders/1/order-status", request);
    }

    public static void 주문_상태_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(OrderUpdateResponse.class).getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());

    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return get("/api/orders");
    }

    public static void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(ArrayList.class)).hasSize(1);
    }

    public static MenuCreateRequest 메뉴_요청_생성() {
        MenuGroupCreateRequest 세마리메뉴 = new MenuGroupCreateRequest("세마리메뉴");
        Long 세마리메뉴_id = 메뉴_그룹_등록됨(세마리메뉴).getId();
        ProductCreateRequest 파닭 = new ProductCreateRequest("파닭", BigDecimal.valueOf(10_000));
        Long 파닭_id = 상품_등록됨(파닭).getId();

        MenuProductCreateRequest 메뉴상품 = new MenuProductCreateRequest(파닭_id, 2L);

        return new MenuCreateRequest("파닭", BigDecimal.valueOf(18_000), 세마리메뉴_id, Collections.singletonList(메뉴상품));
    }

    public static MenuGroupCreateResponse 메뉴_그룹_등록됨(MenuGroupCreateRequest request) {
        return 메뉴_그룹_등록_요청(request).as(MenuGroupCreateResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(MenuGroupCreateRequest request) {
        return AcceptanceTest.post("/api/menu-groups", request);
    }

    public static ProductCreateResponse 상품_등록됨(ProductCreateRequest request) {
        return 상품_등록_요청(request).as(ProductCreateResponse.class);
    }

    public static ExtractableResponse<Response> 상품_등록_요청(ProductCreateRequest request) {
        return AcceptanceTest.post("/api/products", request);
    }

    public static MenuCreateResponse 메뉴_등록됨(MenuCreateRequest request) {
        return 메뉴_등록_요청(request).as(MenuCreateResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(MenuCreateRequest request) {
        return AcceptanceTest.post("/api/menus", request);
    }
}
