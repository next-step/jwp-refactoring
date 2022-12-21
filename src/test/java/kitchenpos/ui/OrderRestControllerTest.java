package kitchenpos.ui;

import static kitchenpos.table.ui.TableRestControllerTest.좌석_생성_요청;
import static kitchenpos.menu.ui.MenuGroupRestControllerTest.메뉴_그룹_생성_요청;
import static kitchenpos.menu.ui.MenuRestControllerTest.메뉴_생성_요청;
import static kitchenpos.ui.ProductRestControllerTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.BaseTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class OrderRestControllerTest extends BaseTest {
    private final ProductRequest 후라이드 = new ProductRequest("후라이드", BigDecimal.valueOf(16000));
    private MenuProductRequest 메뉴_항목;
    private final MenuGroup 메뉴_그룹 = new MenuGroup("한마리메뉴");
    private ResponseEntity<MenuResponse> 메뉴;
    private final OrderTable 좌석 = new OrderTable(null, 4, false);
    private OrderRequest 주문;
    private ResponseEntity<OrderResponse> 주문_생성_응답;
    private OrderStatusRequest 주문_상태_변경_요청 = new OrderStatusRequest(OrderStatus.MEAL);

    @BeforeEach
    void beforeEach() {
        ResponseEntity<ProductResponse> productResponse = 상품_생성_요청(후라이드);
        메뉴_항목 = new MenuProductRequest(productResponse.getBody().getId(), 1);
        ResponseEntity<MenuGroup> menuGroupResponse = 메뉴_그룹_생성_요청(메뉴_그룹);
        MenuRequest 후라이드치킨 = new MenuRequest("후라이드치킨", BigDecimal.valueOf(16000), menuGroupResponse.getBody().getId(), Arrays.asList(메뉴_항목));
        ResponseEntity<MenuResponse> 메뉴 = 메뉴_생성_요청(후라이드치킨);
        Long orderTableId = 좌석_생성_요청(좌석).getBody().getId();
        OrderLineItem 주문_항목 = new OrderLineItem(null, null, 메뉴.getBody().getId(), 1);
        주문 = new OrderRequest(orderTableId, Arrays.asList(주문_항목));
        주문_생성_응답 = 주문_생성_요청(주문);
    }

    @Test
    void 생성() {
        ResponseEntity<OrderResponse> response = 주문_생성_요청(주문);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getBody().getOrderTableId()).isEqualTo(1L)
        );
    }

    @Test
    void 조회() {
        ResponseEntity<List<Order>> response = 조회_요청();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    void 주문_상태_변경() {
        ResponseEntity<OrderResponse> response = 상태_변경_요청(주문_생성_응답.getBody().getId(), new HttpEntity<>(주문_상태_변경_요청));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    public static ResponseEntity<OrderResponse> 주문_생성_요청(OrderRequest orderRequest) {
        return testRestTemplate.postForEntity(basePath + "/api/orders", orderRequest, OrderResponse.class);
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
