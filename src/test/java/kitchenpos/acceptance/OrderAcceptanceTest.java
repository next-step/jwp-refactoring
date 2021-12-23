//package kitchenpos.acceptance;
//
//import io.restassured.response.ExtractableResponse;
//import io.restassured.response.Response;
//import kitchenpos.AcceptanceTest;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderLineItem;
//import kitchenpos.domain.OrderStatus;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.fixture.MenuProductFixture;
//import kitchenpos.fixture.OrderLineItemFixture;
//import kitchenpos.menu.domain.MenuProduct;
//import kitchenpos.menu.dto.MenuProductRequest;
//import kitchenpos.menu.dto.MenuResponse;
//import kitchenpos.menugroup.domain.MenuGroup;
//import kitchenpos.product.domain.Product;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import static kitchenpos.acceptance.MenuAcceptanceTest.메뉴_등록되어_있음;
//import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음;
//import static kitchenpos.acceptance.ProductAcceptanceTest.상품_등록되어_있음;
//import static kitchenpos.acceptance.TableAcceptanceTest.주문_테이블_등록되어_있음;
//import static kitchenpos.fixture.RestAssuredFixture.*;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DisplayName("주문 관련 기능")
//class OrderAcceptanceTest extends AcceptanceTest {
//
//    private static final String API_URL = "/api/orders";
//
//    private Product 상품;
//    private MenuProduct 메뉴_상품;
//    private OrderTable 주문_테이블;
//    private MenuGroup 메뉴_그룹;
//    private MenuResponse 메뉴;
//    private OrderLineItem 주문_항목;
//
//    @BeforeEach
//    public void setUp() {
//        super.setUp();
//
//        // given
//        상품 = 상품_등록되어_있음("강정치킨", 17_000);
//        메뉴_상품 = MenuProductFixture.create(null, 상품, 2);
//        메뉴_그룹 = 메뉴_그룹_등록되어_있음("추천_메뉴_그룹");
//        메뉴 = 메뉴_등록되어_있음("강정치킨_두마리_세트_메뉴", 30_000, 메뉴_그룹.getId(), Arrays.asList(MenuProductRequest.of(상품.getId(), 메뉴_상품.getQuantity())));
//        주문_테이블 = 주문_테이블_등록되어_있음(4, false);
//        주문_항목 = OrderLineItemFixture.create(null, null, 메뉴.getId(), 1L);
//    }
//
//    @DisplayName("주문을 관리한다.")
//    @Test
//    void manageOrder() {
//        // given
//        Order order = new Order();
//        order.setOrderTableId(주문_테이블.getId());
//        order.setOrderLineItems(Arrays.asList(주문_항목));
//
//        // when
//        ExtractableResponse<Response> 주문_생성_응답 = 주문_생성_요청(order);
//        // then
//        주문_생성_응답됨(주문_생성_응답);
//
//        // when
//        ExtractableResponse<Response> 주문_목록_조회_응답 = 주문_목록_조회_요청();
//        // then
//        주문_목록_조회됨(주문_목록_조회_응답);
//
//        // given
//        OrderStatus 주문_상태 = OrderStatus.MEAL;
//        // when
//        Long 주문_ID = 주문_ID_조회(주문_생성_응답);
//        ExtractableResponse<Response> 주문_상태_수정_응답 = 주문_상태_수정_요청(주문_ID, 주문_상태);
//        // then
//        주문_상태_수정됨(주문_상태_수정_응답, 주문_상태);
//    }
//
//    private ExtractableResponse<Response> 주문_생성_요청(Order params) {
//        return 생성_요청(API_URL, params);
//    }
//
//    private void 주문_생성_응답됨(ExtractableResponse<Response> response) {
//        생성됨_201_CREATED(response);
//    }
//
//    private ExtractableResponse<Response> 주문_목록_조회_요청() {
//        return 목록_조회_요청(API_URL);
//    }
//
//    private void 주문_목록_조회됨(ExtractableResponse<Response> response) {
//        성공_200_OK(response);
//    }
//
//    private Long 주문_ID_조회(ExtractableResponse<Response> response) {
//        return response.jsonPath().getLong("id");
//    }
//
//    private ExtractableResponse<Response> 주문_상태_수정_요청(Long orderId, OrderStatus orderStatus) {
//        String path = String.format("%s/%s/order-status", API_URL, orderId);
//        Map<String, String> params = new HashMap<>();
//        params.put("orderStatus", orderStatus.name());
//
//        return 수정_요청(path, params);
//    }
//
//    private void 주문_상태_수정됨(ExtractableResponse<Response> response, OrderStatus orderStatus) {
//        Assertions.assertAll(
//                () -> 성공_200_OK(response)
//                , () -> assertThat(response.jsonPath().getString("orderStatus")).isEqualTo(orderStatus.name())
//        );
//    }
//}
