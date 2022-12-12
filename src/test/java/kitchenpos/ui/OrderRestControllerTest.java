package kitchenpos.ui;

import static kitchenpos.ui.MenuGroupRestControllerTest.메뉴_그룹_생성_요청;
import static kitchenpos.ui.MenuRestControllerTest.메뉴_생성_요청;
import static kitchenpos.ui.ProductRestControllerTest.상품_생성_요청;
import static kitchenpos.ui.TableRestControllerTest.좌석_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;

class OrderRestControllerTest extends BaseTest {
    private final Product 상품 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
    private final OrderTable 좌석 = new OrderTable(1L, 4, false);
    private final List<MenuProduct> 메뉴_항목 = Arrays.asList(new MenuProduct(상품.getId(), 1));
    private final MenuGroup 메뉴_그룹 = new MenuGroup(1L, "한마리메뉴");
    private final List<OrderLineItem> 주문_항목들 =
            Arrays.asList(new OrderLineItem(1L, 1L, 1L, 1));
    private final Menu 메뉴 = new Menu("후라이드치킨", BigDecimal.valueOf(16000), 메뉴_그룹.getId(), 메뉴_항목);
    private Order 주문;
    private final String 조리중 = OrderStatus.COOKING.name();
    private final String 식사중 = OrderStatus.MEAL.name();

    @Test
    void 생성() {
        메뉴_그룹_생성_요청(메뉴_그룹);
        상품_생성_요청(상품);
        메뉴_생성_요청(메뉴);
        Long orderTableId = 좌석_생성_요청(좌석).getBody().getId();

        주문 = new Order(orderTableId, null, LocalDateTime.now(), 주문_항목들);
        ResponseEntity<Order> response = 주문_생성_요청(주문);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 조회() {
        메뉴_그룹_생성_요청(메뉴_그룹);
        상품_생성_요청(상품);
        메뉴_생성_요청(메뉴);
        Long orderTableId = 좌석_생성_요청(좌석).getBody().getId();
        주문 = new Order(orderTableId, null, LocalDateTime.now(), 주문_항목들);
        주문_생성_요청(주문);

        ResponseEntity<List<Order>> response = 조회_요청();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    void 주문_상태_변경() {
        메뉴_그룹_생성_요청(메뉴_그룹);
        상품_생성_요청(상품);
        메뉴_생성_요청(메뉴);
        Long orderTableId = 좌석_생성_요청(좌석).getBody().getId();
        주문 = new Order(orderTableId, 조리중, LocalDateTime.now(), 주문_항목들);
        Long orderId = 주문_생성_요청(주문).getBody().getId();

        주문 = new Order(orderTableId, 식사중, LocalDateTime.now(), 주문_항목들);
        ResponseEntity<Order> response = 상태_변경_요청(orderId, new HttpEntity<>(주문));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getOrderStatus()).isEqualTo(식사중);
    }

    public static ResponseEntity<Order> 주문_생성_요청(Order order) {
        return testRestTemplate.postForEntity(basePath + "/api/orders", order, Order.class);
    }

    private ResponseEntity<List<Order>> 조회_요청() {
        return testRestTemplate.exchange(
                basePath + "/api/orders",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Order>>() {});
    }

    private ResponseEntity<Order> 상태_변경_요청(Long id, HttpEntity<Order> requestEntity) {
        return testRestTemplate.exchange(
                basePath + "/api/orders/" + id + "/order-status",
                HttpMethod.PUT,
                requestEntity,
                new ParameterizedTypeReference<Order>() {});
    }
}
