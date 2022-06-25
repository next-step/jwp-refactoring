package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.*;
import static kitchenpos.table.acceptance.TableAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> 등록된_주문 = 주문_생성_요청(테스트_주문_생성());

        // then
        주문_생성_검증됨(등록된_주문);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // given
        ExtractableResponse<Response> 등록된_주문 = 주문_생성_요청(테스트_주문_생성());

        // when
        ExtractableResponse<Response> 주문_목록 = 주문_목록_조회_요청();

        // then
        주문_목록_검증됨(주문_목록);
        주문_목록_포함됨(주문_목록, Arrays.asList(등록된_주문));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        ExtractableResponse<Response> 등록된_주문 = 주문_생성_요청(테스트_주문_생성());
        final OrderStatus 주문_완료_상태 = OrderStatus.COMPLETION;

        // when
        ExtractableResponse<Response> 변경된_주문 = 주문_상태_변경_요청(등록된_주문, 주문_완료_상태);

        // then
        assertThat(주문_가져옴(변경된_주문).getOrderStatus()).isEqualTo(주문_완료_상태.name());
    }

    private static Order 테스트_주문_생성() {
        Menu 등록된_메뉴 = 메뉴_가져옴(메뉴_등록되어_있음(테스트_메뉴_생성()));
        OrderLineItem 생성된_주문_항목 = new OrderLineItem(등록된_메뉴.getId(), 1);
        OrderTable 등록된_주문_테이블 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, false));
        return new Order(등록된_주문_테이블.getId(), Arrays.asList(생성된_주문_항목));
    }

    public static ExtractableResponse<Response> 주문_등록되어_있음(Order order) {
        return 주문_생성_요청(order);
    }

    public static ExtractableResponse<Response> 주문_생성_요청(Order order) {
        return RestAssured
                .given().log().all()
                .body(order)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static void 주문_생성_검증됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static void 주문_목록_검증됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> resultLineIds = response.jsonPath().getList(".", Order.class).stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(ExtractableResponse<Response> response, OrderStatus orderStatus) {
        Order responseOrder = response.as(Order.class);
        Order changedOrder = new Order(orderStatus.name());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changedOrder)
                .when().put("/api/orders/{orderId}/order-status", responseOrder.getId())
                .then().log().all()
                .extract();
    }

    public static Order 주문_가져옴(ExtractableResponse<Response> response) {
        return response.as(Order.class);
    }
}
