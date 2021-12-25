package kitchenpos.order.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.*;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.*;
import static kitchenpos.menu.acceptance.ProductAcceptanceTest.*;
import static kitchenpos.ordertable.acceptance.TableAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;

@DisplayName("주문 관련 기능")
class OrderAcceptanceTest extends AcceptanceTest {

    private OrderRequest orderRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // menu
        ProductResponse 후라이드 = 상품_생성_요청(new ProductRequest("후라이드", new BigDecimal(16000)))
            .as(ProductResponse.class);
        MenuGroupResponse 두마리메뉴 = 메뉴_그룹_생성_요청("두마리 메뉴").as(MenuGroupResponse.class);
        MenuRequest menuRequest = new MenuRequest("후라이드+후라이드", new BigDecimal(19000), 두마리메뉴.getId(),
            Collections.singletonList(new MenuProductRequest(후라이드.getId(), 2)));

        MenuResponse menuResponse = 메뉴_생성_요청(menuRequest).as(MenuResponse.class);

        // order table
        OrderTableRequest orderTableRequest = new OrderTableRequest(0, false);
        OrderTableResponse orderTableResponse = 주문_테이블_생성_요청(orderTableRequest).as(OrderTableResponse.class);

        orderRequest = new OrderRequest(orderTableResponse.getId(), Collections.singletonList(
            new OrderLineItemRequest(menuResponse.getId(), 2)
        ));
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void createOrder() {
        // when
        ExtractableResponse<Response> response = 주문_생성_요청(orderRequest);

        // then
        주문_생성됨(response);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void findAllOrder() {
        // given
        List<OrderResponse> orderResponses = Collections.singletonList(
            주문_생성_요청(orderRequest).as(OrderResponse.class));

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        주문_목록_조회됨(response, orderResponses);
    }

    @DisplayName("주문 상태를 갱신한다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderResponse orderResponse = 주문_생성_요청(orderRequest).as(OrderResponse.class);

        // when
        ExtractableResponse<Response> response = 주문_상태_갱신_요청(orderResponse.getId(),
            new OrderStatusRequest(OrderStatus.MEAL));

        주문_상태_갱신됨(response);
    }

    private ExtractableResponse<Response> 주문_생성_요청(OrderRequest orderRequest) {
        return RestAssured
            .given().log().all()
            .body(orderRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/api/orders")
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/api/orders")
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 주문_상태_갱신_요청(Long orderId, OrderStatusRequest orderStatusRequest) {
        return RestAssured
            .given().log().all()
            .body(orderStatusRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put("/api/orders/{orderId}/order-status", orderId)
            .then().log().all().extract();
    }

    private void 주문_생성됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 주문_목록_조회됨(ExtractableResponse<Response> response, List<OrderResponse> responses) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(new TypeRef<List<OrderResponse>>() {
        }))
            .containsExactlyElementsOf(responses);
    }

    private void 주문_상태_갱신됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
