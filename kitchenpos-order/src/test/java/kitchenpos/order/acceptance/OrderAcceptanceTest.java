//package kitchenpos.order.acceptance;
//
//import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_상품_생성;
//import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_생성_요청;
//import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
//import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_생성_요청;
//import static kitchenpos.table.acceptance.TableAcceptanceTest.주문_테이블_생성_요청;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import io.restassured.RestAssured;
//import io.restassured.response.ExtractableResponse;
//import io.restassured.response.Response;
//import java.util.Arrays;
//import java.util.List;
//import kitchenpos.AcceptanceTest;
//import kitchenpos.menu.dto.MenuGroupResponse;
//import kitchenpos.menu.dto.MenuProductRequest;
//import kitchenpos.menu.dto.MenuResponse;
//import kitchenpos.order.domain.OrderStatus;
//import kitchenpos.order.dto.OrderLineItemRequest;
//import kitchenpos.order.dto.OrderRequest;
//import kitchenpos.order.dto.OrderResponse;
//import kitchenpos.order.dto.OrderStatusRequest;
//import kitchenpos.product.dto.ProductResponse;
//import kitchenpos.table.dto.OrderTableResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//
//@DisplayName("주문을 관리한다.")
//public class OrderAcceptanceTest extends AcceptanceTest {
//    private ProductResponse 후라이드;
//    private MenuProductRequest 후라이드_원플원;
//    private MenuGroupResponse 추천_메뉴;
//    private MenuResponse 후라이드_세트_메뉴;
//    private OrderTableResponse 일번_주문_테이블;
//    private OrderTableResponse 이번_주문_테이블;
//
//    @BeforeEach
//    void init() {
//        후라이드 = 상품_생성_요청("후라이드", 17_000L).as(ProductResponse.class);
//        후라이드_원플원 = 메뉴_상품_생성(후라이드.getId(), 2);
//
//        추천_메뉴 = 메뉴_그룹_생성_요청("추천메뉴").as(MenuGroupResponse.class);
//        후라이드_세트_메뉴 = 메뉴_생성_요청("후라이드+후라이드", 19_000L, 추천_메뉴.getId(), Arrays.asList(후라이드_원플원)).as(MenuResponse.class);
//
//        일번_주문_테이블 = 주문_테이블_생성_요청(4, false).as(OrderTableResponse.class);
//        이번_주문_테이블 = 주문_테이블_생성_요청(7, false).as(OrderTableResponse.class);
//    }
//
//    @Test
//    @DisplayName("주문을 생성한다.")
//    void createOrder() {
//        // given
//        OrderLineItemRequest 후라이드_아이템 = 주문_아이템_생성(후라이드_세트_메뉴.getId(), 1);
//
//        // when
//        ExtractableResponse<Response> response = 주문_생성_요청(일번_주문_테이블.getId(), Arrays.asList(후라이드_아이템));
//
//        // then
//        주문_생성_요청됨(response);
//    }
//
//    @Test
//    @DisplayName("잘못된 주문 메뉴로 주문을 생성할 경우 - 오류")
//    void createOrderIfNonExistentMenu() {
//        // given
//        OrderLineItemRequest 잘못된_주문_메뉴 = 주문_아이템_생성(-9999L, 1);
//
//        // when
//        ExtractableResponse<Response> response = 주문_생성_요청(일번_주문_테이블.getId(), Arrays.asList(잘못된_주문_메뉴));
//
//        // then
//        주문_생성_실패됨(response);
//    }
//
//    @Test
//    @DisplayName("존재하지 않은 주문 테이블로 주문을 생성할 경우 - 오류")
//    void createOrderIfNonExistentOrderTable() {
//        // given
//        OrderLineItemRequest 후라이드_아이템 = 주문_아이템_생성(후라이드_세트_메뉴.getId(), 1);
//
//        // when
//        ExtractableResponse<Response> response = 주문_생성_요청(-9999L, Arrays.asList(후라이드_아이템));
//
//        // then
//        주문_생성_실패됨(response);
//    }
//
//    @Test
//    @DisplayName("주문 목록을 조회한다.")
//    void findAll() {
//        // given
//        OrderLineItemRequest 후라이드_아이템 = 주문_아이템_생성(후라이드_세트_메뉴.getId(), 1);
//        주문_생성_요청(일번_주문_테이블.getId(), Arrays.asList(후라이드_아이템));
//
//        // when
//        ExtractableResponse<Response> response = 주문_목록_조회_요청();
//
//        // then
//        주문_목록_조회_요청됨(response);
//    }
//
//    @Test
//    @DisplayName("주문의 상태를 변경한다.")
//    void changeOrderStatus() {
//        // given
//        OrderLineItemRequest 후라이드_아이템 = 주문_아이템_생성(후라이드_세트_메뉴.getId(), 1);
//        OrderResponse 주문 = 주문_생성_요청(일번_주문_테이블.getId(), Arrays.asList(후라이드_아이템)).as(OrderResponse.class);
//
//        // when
//        ExtractableResponse<Response> response = 주문_상태_변경_요청(주문.getId(), OrderStatus.MEAL.name());
//
//        // then
//        주문_상태_변경_처리됨(response);
//    }
//
//    public static ExtractableResponse<Response> 주문_생성_요청(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequestList) {
//        OrderRequest orderRequest = new OrderRequest(orderTableId, orderLineItemRequestList);
//
//        return RestAssured.given().log().all()
//            .contentType(MediaType.APPLICATION_JSON_VALUE)
//            .body(orderRequest)
//            .when().post("/api/orders")
//            .then().log().all().extract();
//    }
//
//    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
//        return RestAssured.given().log().all()
//            .when().get("/api/orders")
//            .then().log().all().extract();
//    }
//
//    public static ExtractableResponse<Response> 주문_상태_변경_요청(Long orderId, String orderStatus) {
//        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(orderStatus);
//
//        return RestAssured.given().log().all()
//            .contentType(MediaType.APPLICATION_JSON_VALUE)
//            .body(orderStatusRequest)
//            .when().put("/api/orders/{orderId}/order-status", orderId)
//            .then().log().all().extract();
//    }
//
//    public static void 주문_생성_요청됨(ExtractableResponse response) {
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
//        assertThat(response.header("Location")).isNotBlank();
//    }
//
//    public static void 주문_생성_실패됨(ExtractableResponse response) {
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//    }
//
//    public static void 주문_목록_조회_요청됨(ExtractableResponse response) {
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//    }
//
//    public static void 주문_상태_변경_처리됨(ExtractableResponse response) {
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//    }
//
//    public static OrderLineItemRequest 주문_아이템_생성(Long menuId, long quantity) {
//        return new OrderLineItemRequest(menuId, quantity);
//    }
//}
