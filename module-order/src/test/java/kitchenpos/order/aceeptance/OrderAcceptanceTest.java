package kitchenpos.order.aceeptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("주문 인수테스트 기능")
class OrderAcceptanceTest extends AcceptanceTest {
    private static final String ORDER_URI = "/api/orders";
    private static final String MENU_URI = "/api/menus";
    private static final String MENU_GROUPS_URI = "/api/menu-groups";
    private static final String PRODUCT_URI = "/api/products";
    private static final String TABLE_URI = "/api/tables";

    @BeforeEach
    void setUp() {
        super.setUp();

        // given
        메뉴_그룹_생성_요청("후라이드세트");
        제품_생성_요청("후라이드", 16_000L);
        final List<MenuProductRequest> 메뉴_제품들 = Arrays.asList(new MenuProductRequest(1L, 2));
        메뉴_생성_요청("반반후라이드", 16_000L, 1L, 메뉴_제품들);
        테이블_주문_번호_생성_요청(3, false);
    }

    /**
     *  Given 메뉴 그룹이 등록되어 있고
     *    And 제품(상품)이 등록되어 있고
     *    And 메뉴가 등록되어 있고
     *    And 주문 테이블이 존재하고
     *  When 주문을 하면
     *  Then 주문내역에서 조회할 수 있다.
     */
    @Test
    @DisplayName("주문을 하면 주문내역에서 조회할 수 있다.")
    void createOrder() {
        // given
        final OrderTableResponse 주문_테이블_결과 = new OrderTableResponse(1L, null, 3, false);
        final Orders order = new Orders.Builder(1L)
                .setId(1L)
                .setOrderStatus(OrderStatus.COOKING)
                .setOrderedTime(LocalDateTime.now())
                .build();
        final MenuGroup menuGroup = new MenuGroup(1L, "후라이드세트");
        final OrderLineItem orderLineItem = new OrderLineItem.Builder(order)
                .setSeq(1L)
                .setMenuId(1L)
                .setQuantity(Quantity.of(2L))
                .builder();
        final OrderResponse 예상된_주문_결과 = new OrderResponse(1L, 1L, OrderStatus.COOKING.name(), null, Arrays.asList(orderLineItem.toOrderLineItemResponse()));

        // when
        final ExtractableResponse<Response> 주문_요청_결과 = 주문_요청(1L, 1L, 1L);
        주문_요청_결과_확인(주문_요청_결과);

        // then
        final ExtractableResponse<Response> 주문_조회_결과 = 주문_조회();
        주문_조회_결과_확인(주문_조회_결과, Arrays.asList(예상된_주문_결과));
    }

    /**
     *  Given 메뉴 그룹이 등록되어 있고
     *    And 제품(상품)이 등록되어 있고
     *    And 메뉴가 등록되어 있고
     *    And 주문 테이블이 존재하고
     *    And 주문을 하고
     *  When 주문 상태을 변경하면
     *  Then 주문 상태가 변경된다.
     */
    @Test
    @DisplayName("주문 상태를 변경하면 주문 상태가 변경된다.")
    void changeOrderStatus() {
        // given
        final OrderStatus 완료_상태 = OrderStatus.COMPLETION;
        주문_요청(1L, 1L, 1L);

        // when
        final ExtractableResponse<Response> 주문_상태_변경_결과 = 주문_상태_변경(1L, 완료_상태);

        // then
        주문_상태_변경_결과_확인(주문_상태_변경_결과, 완료_상태);
    }

    public static ExtractableResponse<Response> 주문_요청(Long 테이블_번호, Long 메뉴_번호, Long 갯수) {
        final OrderLineItemRequest 요청할_메뉴 = new OrderLineItemRequest(메뉴_번호, 갯수);
        return RestAssuredHelper.post(ORDER_URI, new OrderRequest(테이블_번호, Arrays.asList(요청할_메뉴)));
    }

    public static void 주문_요청_결과_확인(ExtractableResponse<Response> 주문_요청_결과) {
        assertThat(주문_요청_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 주문_조회() {
        return RestAssuredHelper.get(ORDER_URI);
    }

    public static void 주문_조회_결과_확인(ExtractableResponse<Response> 주문_조회_결과, List<OrderResponse> 예상된_주문_결과) {
        final List<OrderResponse> 실제_주문 = 주문_조회_결과.body().jsonPath().getList(".", OrderResponse.class);

        assertAll(
                () -> assertThat(주문_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(실제_주문).hasSize(예상된_주문_결과.size()),
                () -> 주문_결과_비교(실제_주문, 예상된_주문_결과)
        );
    }

    private static void 주문_결과_비교(List<OrderResponse> 주문1, List<OrderResponse> 주문2) {
        for (int idx = 0; idx < 주문1.size(); idx++) {
            int innerIdx = idx;
            assertAll(
                    () -> assertThat(주문1.get(innerIdx).getOrderStatus()).isEqualTo(주문2.get(innerIdx).getOrderStatus()),
                    () -> assertThat(주문1.get(innerIdx).getId()).isEqualTo(주문2.get(innerIdx).getId()),
                    () -> assertThat(주문1.get(innerIdx).getOrderTableId()).isEqualTo(주문2.get(innerIdx).getOrderTableId())
            );
        }
    }

    public static ExtractableResponse<Response> 주문_상태_변경(Long 주문_번호, OrderStatus 변경할_상태) {
        final String uri = ORDER_URI + "/{orderId}/order-status";
        return RestAssuredHelper.putContainBody(uri, new OrderStatusRequest(변경할_상태), 주문_번호);
    }

    private void 주문_상태_변경_결과_확인(ExtractableResponse<Response> 주문_상태_변경_결과, OrderStatus 상태) {
        final OrderResponse 변경된_주문 = 주문_상태_변경_결과.body().jsonPath().getObject(".", OrderResponse.class);

        assertAll(
                () -> assertThat(주문_상태_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(변경된_주문.getOrderStatus()).isEqualTo(상태.name())
        );
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(String 메뉴_그룹명) {
        final MenuGroupRequest menuGroupRequest = new MenuGroupRequest(메뉴_그룹명);
        return RestAssuredHelper.post(MENU_GROUPS_URI, menuGroupRequest);
    }

    public static ExtractableResponse<Response> 제품_생성_요청(String 제품명, Long 금액) {
        final ProductRequest 생성할_제품 = new ProductRequest(제품명, 금액);
        return RestAssuredHelper.post(PRODUCT_URI, 생성할_제품);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(String 메뉴명, Long 메뉴_금액, Long 메뉴_그룹_아이디,
                                                         List<MenuProductRequest> 메뉴_제품들) {
        final MenuRequest menuRequest = new MenuRequest(메뉴명, 메뉴_금액, 메뉴_그룹_아이디, 메뉴_제품들);
        return RestAssuredHelper.post(MENU_URI, menuRequest);
    }

    public static ExtractableResponse<Response> 테이블_주문_번호_생성_요청(Integer 손님수, boolean 빈_테이블_유무) {
        final OrderTableRequest 요청할_주문_테이블 = new OrderTableRequest(손님수, 빈_테이블_유무);
        return RestAssuredHelper.post(TABLE_URI, 요청할_주문_테이블);
    }
}
