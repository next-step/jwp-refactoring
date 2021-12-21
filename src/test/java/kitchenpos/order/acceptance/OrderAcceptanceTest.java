package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.menu.MenuFactory;
import kitchenpos.menu.acceptance.MenuAcceptanceTest;
import kitchenpos.order.OrderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
    Menu 치킨세트 = 메뉴_생성됨();
    Order 첫번째_주문 = 주문_메뉴_등록됨(치킨세트);

    // when
    ExtractableResponse<Response> response = 주문_생성_요청(첫번째_주문);

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
    Menu 치킨세트 = 메뉴_생성됨();
    Order 첫번째_주문 = 주문_메뉴_등록됨(치킨세트);
    첫번째_주문 = 주문_생성_요청(첫번째_주문).body().as(Order.class);

    // when
    첫번째_주문.setOrderStatus(OrderStatus.COMPLETION.name());
    ExtractableResponse<Response> response = 주문_상태_변경_요청(첫번째_주문);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  private ExtractableResponse<Response> 주문_상태_변경_요청(Order order) {
    Map<String, Object> pathParams = new HashMap<>();
    pathParams.put("orderId", order.getId());
    return ofRequest(Method.PUT, "/api/orders/{orderId}/order-status", pathParams, order);
  }

  private ExtractableResponse<Response> 주문_생성_요청(Order order) {
    return ofRequest(Method.POST, "/api/orders", order);
  }

  private ExtractableResponse<Response> 주문_목록_조회_요청() {
    return ofRequest(Method.GET, "/api/orders");
  }

  private Order 주문_메뉴_등록됨(Menu 치킨세트) {
    OrderTable 첫번째_주문_테이블 = TableAcceptanceTest.주문_테이블_생성됨(OrderFactory.ofOrderTable(false, 10));

    Order 첫번째_주문 = OrderFactory.ofOrder(첫번째_주문_테이블.getId());
    첫번째_주문.setOrderLineItems(Collections.singletonList(OrderFactory.ofOrderLineItem(치킨세트.getId(), 35000L)));
    return 첫번째_주문;
  }

  private Menu 메뉴_생성됨() {
    Menu 치킨세트 = MenuFactory.ofMenu("치킨세트", 1L, 35000);
    치킨세트.setMenuProducts(Collections.singletonList(MenuFactory.ofMenuProduct(1L, null, 1L, 35000)));
    치킨세트 = MenuAcceptanceTest.메뉴_생성됨(치킨세트);
    return 치킨세트;
  }
}
