package kitchenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Collections;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.*;
import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTest.*;
import static kitchenpos.ordertable.acceptance.OrderTableAcceptanceTest.*;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("주문 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    private MenuResponse 후라이드치킨메뉴;
    private OrderResponse 후라이드치킨주문;

    public static OrderResponse 주문_등록되어_있음(OrderRequest orderRequest) {
        return 주문_등록_요청(orderRequest).as(OrderResponse.class);
    }

    public static ExtractableResponse<Response> 주문_등록_요청(OrderRequest orderRequest) {
        return post("/api/orders", orderRequest);
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return get("/api/orders");
    }

    public static ExtractableResponse<Response> 주문의_주문_상태_변경_요청(Long id, OrderRequest orderRequest) {
        return put("/api/orders/{orderId}/order-status", orderRequest, id);
    }

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        후라이드치킨메뉴 = 메뉴_등록되어_있음(makeMenuRequest());
        후라이드치킨주문 = 주문_등록되어_있음(makeOrderRequest());
    }

    @Test
    @DisplayName("주문을 관리한다.")
    void ManageOrder() {
        // given
        OrderRequest orderRequest = makeOrderRequest();

        // when
        ExtractableResponse<Response> createResponse = 주문_등록_요청(orderRequest);

        // then
        주문_등록됨(createResponse);

        // when
        ExtractableResponse<Response> listResponse = 주문_목록_조회_요청();

        // then
        주문_목록_조회됨(listResponse);
    }

    @Test
    @DisplayName("주문의 주문 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        OrderRequest orderRequest = new OrderRequest(OrderStatus.MEAL.name());

        // when
        ExtractableResponse<Response> response = 주문의_주문_상태_변경_요청(후라이드치킨주문.getId(), orderRequest);

        // then
        주문의_주문_상태_변경됨(response);
    }

    private MenuRequest makeMenuRequest() {
        ProductResponse 후라이드치킨 = 상품_등록되어_있음(new ProductRequest("후라이드치킨", new BigDecimal(16_000)));
        MenuGroupResponse 한마리메뉴그룹 = 메뉴_그룹_등록되어_있음(new MenuGroupRequest("한마리메뉴"));
        return new MenuRequest(
                후라이드치킨.getName(),
                후라이드치킨.getPrice(),
                한마리메뉴그룹.getId(),
                Collections.singletonList(new MenuProductRequest(후라이드치킨.getId(), 1))
        );
    }

    private OrderRequest makeOrderRequest() {
        OrderTableResponse 테이블 = 테이블_등록되어_있음(new OrderTableRequest(2, false));
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(후라이드치킨메뉴.getId(), 2);
        return new OrderRequest(테이블.getId(), OrderStatus.COOKING.name(), Collections.singletonList(orderLineItemRequest));
    }

    private void 주문_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", OrderResponse.class).size()).isPositive();
    }

    private void 주문의_주문_상태_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
