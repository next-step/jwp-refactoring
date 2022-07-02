package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.*;
import static kitchenpos.table.acceptance.TableAcceptanceTest.주문_테이블_가져옴;
import static kitchenpos.table.acceptance.TableAcceptanceTest.주문_테이블_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // when
        ExtractableResponse<Response> 등록된_주문 = 주문_등록되어_있음(주문_생성());

        // then
        주문_생성_검증됨(등록된_주문);
    }

    @DisplayName("[예외] 주문 항목이 없는 주문을 생성한다.")
    @Test
    void createOrder_without_order_list_item() {
        // when
        ExtractableResponse<Response> 등록된_주문 = 주문_등록되어_있음(주문_항목_없는_주문_생성());

        // then
        주문_생성_실패됨(등록된_주문);
    }

    @DisplayName("[예외] 메뉴와 메뉴 항목이 일치하지 않는 주문을 생성한다.")
    @Test
    void createOrder_menu_and_order_list_item_not_matching() {
        // when
        ExtractableResponse<Response> 등록된_주문 = 주문_등록되어_있음(메뉴와_주문_항목_개수_다른_주문_생성());

        // then
        주문_생성_실패됨(등록된_주문);
    }

    @DisplayName("[예외] 주문 테이블 없는 주문을 생성한다.")
    @Test
    void createOrder_without_order_table() {
        // when
        ExtractableResponse<Response> 등록된_주문 = 주문_등록되어_있음(주문_테이블_없는_주문_생성());

        // then
        주문_생성_실패됨(등록된_주문);
    }

    @DisplayName("[예외] 비어 있는 주문 테이블에서 주문을 생성한다.")
    @Test
    void createOrder_with_empty_order_table() {
        // when
        ExtractableResponse<Response> 등록된_주문 = 주문_등록되어_있음(비어_있는_주문_테이블에서_주문_생성());

        // then
        주문_생성_실패됨(등록된_주문);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // given
        ExtractableResponse<Response> 등록된_주문 = 주문_등록되어_있음(주문_생성());

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
        ExtractableResponse<Response> 등록된_주문 = 주문_등록되어_있음(주문_생성());

        // when
        ExtractableResponse<Response> 변경된_주문 = 주문_상태_변경_요청(등록된_주문, OrderStatus.COMPLETION);

        // then
        주문_상태_일치함(변경된_주문, OrderStatus.COMPLETION);
    }

    @DisplayName("[예외] 계산 완료 상태에서 추가로 주문 상태를 변경한다.")
    @Test
    void changeOrderStatus_with_completion_state() {
        // given
        ExtractableResponse<Response> 등록된_주문 = 주문_등록되어_있음(주문_생성());
        ExtractableResponse<Response> COMPLETION_변경된_주문 = 주문_상태_변경_요청(등록된_주문, OrderStatus.COMPLETION);

        // when
        ExtractableResponse<Response> 변경된_주문 = 주문_상태_변경_요청(COMPLETION_변경된_주문, OrderStatus.COMPLETION);

        // then
        주문_상태_변경_실패됨(변경된_주문);
    }

    @DisplayName("[예외] 저장되지 않은 주문의 상태를 변경한다.")
    @Test
    void changeOrderStatus_with_not_saved_order() {
        // when
        ExtractableResponse<Response> 변경된_주문 = 주문_상태_변경_요청(new Order(9999L), OrderStatus.COMPLETION);

        // then
        주문_상태_변경_실패됨(변경된_주문);
    }

    public static OrderRequest 주문_생성() {
        MenuResponse 등록된_메뉴 = 메뉴_가져옴(메뉴_등록되어_있음(테스트_메뉴_생성(MENU_NAME01, MENU_PRICE01)));
        OrderLineItem 생성된_주문_항목 = new OrderLineItem(등록된_메뉴.getId(), 1);
        OrderTable 등록된_주문_테이블 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, false)).toOrderTable();
        return new OrderRequest(등록된_주문_테이블.getId(), Arrays.asList(생성된_주문_항목));
    }

    public static OrderRequest 주문_항목_없는_주문_생성() {
        OrderTable 등록된_주문_테이블 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, false)).toOrderTable();
        return new OrderRequest(등록된_주문_테이블.getId(), Collections.emptyList());
    }

    public static OrderRequest 메뉴와_주문_항목_개수_다른_주문_생성() {
        MenuResponse 등록된_메뉴 = 메뉴_가져옴(메뉴_등록되어_있음(테스트_메뉴_생성(MENU_NAME01, MENU_PRICE01)));
        OrderLineItem 생성된_주문_항목1 = new OrderLineItem(등록된_메뉴.getId(), 1);
        OrderLineItem 생성된_주문_항목2 = new OrderLineItem(등록된_메뉴.getId(), 1);
        OrderTable 등록된_주문_테이블 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, false)).toOrderTable();
        return new OrderRequest(등록된_주문_테이블.getId(), Arrays.asList(생성된_주문_항목1, 생성된_주문_항목2));
    }

    public static OrderRequest 주문_테이블_없는_주문_생성() {
        MenuResponse 등록된_메뉴 = 메뉴_가져옴(메뉴_등록되어_있음(테스트_메뉴_생성(MENU_NAME01, MENU_PRICE01)));
        OrderLineItem 생성된_주문_항목 = new OrderLineItem(등록된_메뉴.getId(), 1);
        return new OrderRequest(null, Arrays.asList(생성된_주문_항목));
    }

    public static OrderRequest 비어_있는_주문_테이블에서_주문_생성() {
        MenuResponse 등록된_메뉴 = 메뉴_가져옴(메뉴_등록되어_있음(테스트_메뉴_생성(MENU_NAME01, MENU_PRICE01)));
        OrderLineItem 생성된_주문_항목 = new OrderLineItem(등록된_메뉴.getId(), 1);
        OrderTable 등록된_주문_테이블 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, true)).toOrderTable();
        return new OrderRequest(등록된_주문_테이블.getId(), Arrays.asList(생성된_주문_항목));
    }

    public static ExtractableResponse<Response> 주문_등록되어_있음(OrderRequest request) {
        return 주문_생성_요청(request);
    }

    public static ExtractableResponse<Response> 주문_생성_요청(OrderRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static void 주문_생성_검증됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 주문_생성_실패됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
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
        List<Long> resultLineIds = response.jsonPath().getList(".", OrderResponse.class).stream()
                .map(OrderResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(ExtractableResponse<Response> response, OrderStatus orderStatus) {
        OrderResponse orderResponse = response.as(OrderResponse.class);
        Order changedOrder = new Order(orderStatus);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changedOrder)
                .when().put("/api/orders/{orderId}/order-status", orderResponse.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(Order order, OrderStatus orderStatus) {
        Order changedOrder = new Order(orderStatus);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changedOrder)
                .when().put("/api/orders/{orderId}/order-status", order.getId())
                .then().log().all()
                .extract();
    }

    public static OrderResponse 주문_가져옴(ExtractableResponse<Response> response) {
        return response.as(OrderResponse.class);
    }

    public void 주문_상태_일치함(ExtractableResponse<Response> response, OrderStatus orderStatus) {
        assertThat(주문_가져옴(response).getOrderStatus()).isEqualTo(orderStatus.name());
    }

    public static void 주문_상태_변경_실패됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
