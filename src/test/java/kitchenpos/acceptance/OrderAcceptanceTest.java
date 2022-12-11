package kitchenpos.acceptance;



import static kitchenpos.acceptance.MenuAcceptanceTest.짜장_탕수_메뉴_생성;
import static kitchenpos.acceptance.TableAcceptanceTest.빈_테이블_생성;
import static kitchenpos.acceptance.TableAcceptanceTest.채워진_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {
    private Menu 짜장_탕수_세트;
    private OrderTable 채워진_테이블_A;
    private OrderTable 채워진_테이블_B;
    private OrderTable 빈_테이블;

    @DisplayName("주문 기능 통합 테스트")
    @TestFactory
    Stream<DynamicNode> order() {
        짜장_탕수_세트 = 짜장_탕수_메뉴_생성();
        채워진_테이블_A = 채워진_테이블_생성();
        채워진_테이블_B = 채워진_테이블_생성();
        빈_테이블 = 빈_테이블_생성();

        return Stream.of(
                dynamicTest("주문을 생성한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 주문_생성_요청(채워진_테이블_A, 짜장_탕수_세트);
                    // then
                    주문_정상_생성됨(response);
                }),
                dynamicTest("등록되지 않은 메뉴로는 주문을 생성할 수 없다.", () -> {
                    // given
                    Menu 미등록_메뉴 = new Menu();
                    미등록_메뉴.setId(Long.MAX_VALUE);
                    // when
                    ExtractableResponse<Response> response = 주문_생성_요청(채워진_테이블_A, 미등록_메뉴);
                    // then
                    요청_실패됨(response);
                }),
                dynamicTest("빈테이블은 주문을 생성 할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 주문_생성_요청(빈_테이블, 짜장_탕수_세트);
                    // then
                    요청_실패됨(response);
                }),
                dynamicTest("주문 목록을 조회한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 주문_목록_조회_요청();
                    // then
                    주문_목록_정상_조회됨(response, 짜장_탕수_세트.getId());
                }),
                dynamicTest("주문 상태를 변경한다.", () -> {
                    // given
                    Order 생성된_주문 = 주문_생성됨(채워진_테이블_A, 짜장_탕수_세트);
                    생성된_주문.setOrderStatus(OrderStatus.MEAL.name());
                    // when
                    ExtractableResponse<Response> response = 주문상태_변경_요청(생성된_주문.getId(), 생성된_주문);
                    // then
                    주문_정상_변경됨(response, OrderStatus.MEAL);
                }),
                dynamicTest("주문상태가 현재 종료되었으면 상태를 변경할 수 없다.", () -> {
                    // given
                    Order 생성된_주문 = 주문_생성됨(채워진_테이블_B, 짜장_탕수_세트);
                    생성된_주문.setOrderStatus(OrderStatus.COMPLETION.name());
                    주문상태_변경_요청(생성된_주문.getId(), 생성된_주문);
                    // when
                    ExtractableResponse<Response> response = 주문상태_변경_요청(생성된_주문.getId(), 생성된_주문);
                    // then
                    요청_실패됨(response);
                })
        );
    }
    public static Order 주문_생성됨(OrderTable orderTable, Menu... menus){
        return 주문_생성_요청(orderTable, menus).as(Order.class);
    }

    public static ExtractableResponse<Response> 주문_생성_요청(OrderTable orderTable, Menu... menus) {
        Order order = new Order(null, orderTable.getId(), toOrderLoneItems(menus));
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }
    public static List<OrderLineItem> toOrderLoneItems(Menu[] menus) {
        return Arrays.stream(menus)
                .map(m -> {
                    OrderLineItem orderLineItem = new OrderLineItem();
                    orderLineItem.setMenuId(m.getId());
                    orderLineItem.setQuantity(1L);
                    return orderLineItem;
                })
                .collect(Collectors.toList());
    }

    private ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문상태_변경_요청(Long id, Order order) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().put("/api/orders/{orderId}/order-status", id)
                .then().log().all()
                .extract();
    }

    private void 주문_정상_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
    private void 요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 주문_목록_정상_조회됨(ExtractableResponse<Response> response, Long... 메뉴아이디목록) {
        List<Long> 조회_결과_목록 = response.jsonPath()
                .getList(".", Order.class)
                .stream()
                .map(it->it.getOrderLineItems())
                .flatMap(Collection::stream)
                .map(it->it.getMenuId())
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(조회_결과_목록).containsAll(Arrays.asList(메뉴아이디목록))
        );
    }
    private void 주문_정상_변경됨(ExtractableResponse<Response> response, OrderStatus 주문상태) {

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(Order.class).getOrderStatus()).isEqualTo(주문상태.name())
        );
    }

}
