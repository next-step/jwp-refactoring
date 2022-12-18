package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.acceptance.MenuAcceptanceUtils.메뉴_등록되어_있음;
import static kitchenpos.acceptance.MenuGroupAcceptanceUtils.메뉴_그룹_등록되어_있음;
import static kitchenpos.acceptance.OrderAcceptanceUtils.*;
import static kitchenpos.acceptance.ProductAcceptanceUtils.상품_등록되어_있음;
import static kitchenpos.acceptance.TableAcceptanceUtils.주문_테이블_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 관련 인수 테스트")
class OrderAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse premiumMenu;
    private ProductResponse honeycombo;
    private MenuResponse honeycomboChicken;
    private OrderTableResponse orderTable;
    private OrderTableResponse emptyTable;

    /**
     * Given 메뉴 그룹 등록되어 있음
     * And 상품 등록되어 있음
     * And 메뉴 등록되어 있음
     * And 주문 테이블 등록되어 있음
     * And 비어있는 주문 테이블 등록되어 있음
     */
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        premiumMenu = 메뉴_그룹_등록되어_있음(MenuGroupRequest.from("premiumMenu")).as(MenuGroupResponse.class);

        honeycombo = 상품_등록되어_있음(ProductRequest.of("honeycombo", BigDecimal.valueOf(18000))).as(ProductResponse.class);

        List<MenuProductRequest> 메뉴상품_목록 = Arrays.asList(MenuProductRequest.of(honeycombo.getId(), 2));
        honeycomboChicken = 메뉴_등록되어_있음(MenuRequest.of( "honeycomboChicken", BigDecimal.valueOf(18000), premiumMenu.getId(), 메뉴상품_목록))
                .as(MenuResponse.class);

        orderTable = 주문_테이블_등록되어_있음(OrderTableRequest.of( 2, false)).as(OrderTableResponse.class);
        emptyTable = 주문_테이블_등록되어_있음(OrderTableRequest.of( 2, true)).as(OrderTableResponse.class);

    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        List<OrderLineItemRequest> 주문_항목 = Arrays.asList(OrderLineItemRequest.of(honeycomboChicken.getId(), 2));
        OrderRequest 주문 = OrderRequest.of(orderTable.getId(), 주문_항목);

        ExtractableResponse<Response> response = 주문_생성_요청(주문);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


    @DisplayName("주문 항목이 비어있으면 주문을 생성할 수 없다.")
    @Test
    void createFail() {
        OrderRequest 주문 = OrderRequest.of(orderTable.getId(), Collections.emptyList());

        ExtractableResponse<Response> response = 주문_생성_요청(주문);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @DisplayName("주문 항목이 메뉴에 등록되어 있지 않다면 주문을 생성할 수 없다.")
    @Test
    void createFail2() {
        List<OrderLineItemRequest> 주문_항목 = Arrays.asList(OrderLineItemRequest.of(0L, 2));
        OrderRequest 주문 = OrderRequest.of(orderTable.getId(), 주문_항목);

        ExtractableResponse<Response> response = 주문_생성_요청(주문);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("주문 테이블이 등록되어 있지 않다면 주문을 생성할 수 없다.")
    @Test
    void createFail3() {
        List<OrderLineItemRequest> orderItems = Arrays.asList(OrderLineItemRequest.of(honeycomboChicken.getId(), 2));
        OrderRequest order = OrderRequest.of(0L, orderItems);

        ExtractableResponse<Response> response = 주문_생성_요청(order);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("주문 테이블이 빈 테이블이면 주문을 생성할 수 없다.")
    @Test
    void createFail4() {
        List<OrderLineItemRequest> orderItems = Arrays.asList(OrderLineItemRequest.of(honeycomboChicken.getId(), 2));
        OrderRequest order = OrderRequest.of(emptyTable.getId(), orderItems);

        ExtractableResponse<Response> response = 주문_생성_요청(order);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {

        List<OrderLineItemRequest> orderItems = Arrays.asList(OrderLineItemRequest.of(honeycomboChicken.getId(), 2));
        OrderRequest order = OrderRequest.of(orderTable.getId(), orderItems);

        OrderResponse registeredOrder = 주문_등록되어_있음(order).as(OrderResponse.class);

        ExtractableResponse<Response> response = 주문_목록_조회_요청();


        List<OrderResponse> orders = response.jsonPath().getList(".", OrderResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.get(0)).isEqualTo(registeredOrder),
                () -> assertThat(orders.get(0).getOrderLineItems())
                        .containsExactlyInAnyOrder(registeredOrder.getOrderLineItems().get(0))
        );

    }


    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {

        List<OrderLineItemRequest> orderItems = Arrays.asList(OrderLineItemRequest.of(honeycomboChicken.getId(), 2));
        OrderRequest order = OrderRequest.of(orderTable.getId(), orderItems);

        OrderResponse registeredOrder = 주문_등록되어_있음(order).as(OrderResponse.class);

        String orderStatus = OrderStatus.MEAL.name();
        ExtractableResponse<Response> response = 주문_상태_변경_요청(registeredOrder.getId(), OrderRequest.from(orderStatus));

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(OrderResponse.class).getOrderStatus()).isEqualTo(orderStatus)
        );
    }


    @DisplayName("주문이 없으면 주문의 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusFail() {
        OrderRequest notRegisteredOrder = OrderRequest.from(OrderStatus.MEAL.name());

        ExtractableResponse<Response> response = 주문_상태_변경_요청(0L, notRegisteredOrder);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    @DisplayName("주문 상태가 계산 완료이면 주문의 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusFail2() {
        List<OrderLineItemRequest> orderItems = Arrays.asList(OrderLineItemRequest.of(honeycomboChicken.getId(), 2));
        OrderResponse registeredOrder = 주문_등록되어_있음(OrderRequest.of(orderTable.getId(), orderItems)).as(OrderResponse.class);

        OrderResponse payedOrder = 주문_상태_변경_요청(registeredOrder.getId(), OrderRequest.from(OrderStatus.COMPLETION.name()))
                .as(OrderResponse.class);

        ExtractableResponse<Response> response =
                주문_상태_변경_요청(payedOrder.getId(), OrderRequest.from(OrderStatus.MEAL.name()));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
