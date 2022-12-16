package kitchenpos.ui;

import static kitchenpos.ui.MenuGroupRestControllerTest.메뉴_그룹_생성_요청;
import static kitchenpos.ui.MenuRestControllerTest.메뉴_생성_요청;
import static kitchenpos.ui.ProductRestControllerTest.상품_생성_요청;
import static kitchenpos.ui.TableRestControllerTest.좌석_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.dto.response.OrderResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class OrderRestControllerTest extends BaseTest {
    private final Product 상품 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
    private final OrderTable 좌석 = new OrderTable(null, 4, false);
    private final MenuProductRequest 메뉴_항목 = new MenuProductRequest(상품.getId(), 1);
    private final MenuGroup 메뉴_그룹 = new MenuGroup(1L, "한마리메뉴");
    private final MenuRequest 메뉴 =
            new MenuRequest("후라이드치킨", BigDecimal.valueOf(16000), 메뉴_그룹.getId(), Arrays.asList(메뉴_항목));
    private Order 주문;
    private OrderStatusRequest 주문_상태_변경_요청 = new OrderStatusRequest(OrderStatus.MEAL);
    private final List<OrderLineItem> 주문_항목들 =
            Arrays.asList(new OrderLineItem(1L, 주문, 1L, 1));
    private ResponseEntity<OrderResponse> 주문_생성_응답;

    @BeforeEach
    void beforeEach() {
        메뉴_그룹_생성_요청(메뉴_그룹);
        상품_생성_요청(상품);
        메뉴_생성_요청(메뉴);
        Long orderTableId = 좌석_생성_요청(좌석).getBody().getId();
        주문 = new Order(orderTableId, 주문_항목들);
        주문_생성_응답 = 주문_생성_요청(주문);
    }

    @Test
    void 생성() {
        ResponseEntity<OrderResponse> response = 주문_생성_요청(주문);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 조회() {
        ResponseEntity<List<Order>> response = 조회_요청();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    void 주문_상태_변경() {
        ResponseEntity<OrderResponse> response =
                상태_변경_요청(주문_생성_응답.getBody().getId(), new HttpEntity<>(주문_상태_변경_요청));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    public static ResponseEntity<OrderResponse> 주문_생성_요청(Order order) {
        return testRestTemplate.postForEntity(basePath + "/api/orders", order, OrderResponse.class);
    }

    private ResponseEntity<List<Order>> 조회_요청() {
        return testRestTemplate.exchange(
                basePath + "/api/orders",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Order>>() {});
    }

    private ResponseEntity<OrderResponse> 상태_변경_요청(Long id, HttpEntity<OrderStatusRequest> requestEntity) {
        return testRestTemplate.exchange(
                basePath + "/api/orders/" + id + "/order-status",
                HttpMethod.PUT,
                requestEntity,
                new ParameterizedTypeReference<OrderResponse>() {});

    }
}
