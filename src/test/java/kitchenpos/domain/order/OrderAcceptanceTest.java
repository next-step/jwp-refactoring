package kitchenpos.domain.order;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    private Menu menu;

    private OrderTable orderTable;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("추천메뉴"));
        menu = menuDao.save(((new Menu("후라이드+후라이드", BigDecimal.valueOf(17000), menuGroup.getId()))));
        orderTable = orderTableDao.save(new OrderTable());
    }

    @Test
    @DisplayName("주문을 할 수 있다.")
    void createOrder() {
        // given
        final Order order = new Order(orderTable.getId(), Arrays.asList(new OrderLineItem(menu.getId(), 1L)));

        // when
        final ExtractableResponse<Response> 주문_요청_응답 = 주문_요청(order);

        // then
        주문_완료_됨(주문_요청_응답);
    }

    private void 주문_완료_됨(final ExtractableResponse<Response> response) {
        final Order 완료된_주문 = response.as(Order.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(완료된_주문.getId()).isNotNull(),
                () -> assertThat(완료된_주문.getOrderTableId()).isEqualTo(orderTable.getId()),
                () -> assertThat(완료된_주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(완료된_주문.getOrderLineItems().size()).isOne()
        );
    }

    public ExtractableResponse<Response> 주문_요청(final Order order) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        // given
        final Order order = 주문(new Order(orderTable.getId(), Arrays.asList(new OrderLineItem(menu.getId(), 1L))));

        // when
        final ExtractableResponse<Response> 주문_상태_변경_요청_응답 = 주문_상태_변경_요청(order);

        // then
        주문_상태_변경_됨(주문_상태_변경_요청_응답);
    }

    private void 주문_상태_변경_됨(final ExtractableResponse<Response> response) {
        final Order 변경된_주문_상태 = response.as(Order.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(변경된_주문_상태.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name())
        );
    }

    public ExtractableResponse<Response> 주문_상태_변경_요청(final Order order) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new Order(OrderStatus.COMPLETION.name()))
                .when().put("/api/orders/{orderId}/order-status", order.getId())
                .then().log().all()
                .extract();
    }

    private Order 주문(final Order order) {
        return 주문_요청(order).as(Order.class);
    }
}
