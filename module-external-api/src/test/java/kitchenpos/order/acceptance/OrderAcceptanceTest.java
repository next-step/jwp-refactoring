package kitchenpos.order.acceptance;

import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.acceptance.MenuAcceptanceTest;
import kitchenpos.menu.acceptance.MenuGroupAcceptanceTest;
import kitchenpos.product.acceptance.ProductAcceptanceTest;
import kitchenpos.menu.dto.*;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.*;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.acceptance.TableAcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("주문을 생성 한다.")
    @Test
    void createOrder() {
        // given
        OrderRequest orderRequest = 주문_생성_정보_입력됨();

        // when
        ExtractableResponse<Response> response = 주문_생성_요청(orderRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("주문 목록을 조회 한다.")
    @Test
    void findAllOrders() {
        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderResponse orderResponse = 주문_생성되어_있음();
        OrderRequest orderRequest = new OrderRequest(null, OrderStatus.COMPLETION, null);

        // when
        ExtractableResponse<Response> response = 주문_상태_변경_요청(orderResponse.getId(), orderRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private MenuResponse 메뉴_생성되어_있음() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("튀김류");
        MenuGroupResponse menuGroupResponse = MenuGroupAcceptanceTest.메뉴_그룹_생성됨(menuGroupRequest);

        ProductRequest productRequest = new ProductRequest("후라이드", BigDecimal.valueOf(10000));
        ProductResponse productResponse = ProductAcceptanceTest.상품_생성됨(productRequest);

        MenuProductRequest menuProductRequest = new MenuProductRequest(productResponse.getId(), 2L);

        MenuRequest menuRequest = new MenuRequest("치킨세트", BigDecimal.valueOf(10000), menuGroupResponse.getId(), Collections.singletonList(menuProductRequest));

        return MenuAcceptanceTest.메뉴_생성됨(menuRequest);
    }

    private OrderResponse 주문_생성되어_있음() {
        OrderRequest orderRequest = 주문_생성_정보_입력됨();

        return 주문_생성_요청(orderRequest).as(OrderResponse.class);
    }

    private OrderRequest 주문_생성_정보_입력됨() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(10, false);
        OrderTableResponse orderTableResponse = TableAcceptanceTest.주문_테이블_생성됨(orderTableRequest);

        MenuResponse menuResponse = 메뉴_생성되어_있음();

        OrderLineItemRequest firstOrderLine = new OrderLineItemRequest(menuResponse.getId(), 2L);
        OrderLineItemRequest secondOrderLine = new OrderLineItemRequest(menuResponse.getId(), 1L);

        return new OrderRequest(orderTableResponse.getId(), null, Arrays.asList(firstOrderLine, secondOrderLine));
    }

    private ExtractableResponse<Response> 주문_상태_변경_요청(Long orderId, OrderRequest orderRequest) {
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("orderId", orderId);
        return ofRequest(Method.PUT, "/api/orders/{orderId}/order-status", pathParams, orderRequest);
    }

    private ExtractableResponse<Response> 주문_생성_요청(OrderRequest orderRequest) {
        return ofRequest(Method.POST, "/api/orders", orderRequest);
    }

    private ExtractableResponse<Response> 주문_목록_조회_요청() {
        return ofRequest(Method.GET, "/api/orders");
    }
}
