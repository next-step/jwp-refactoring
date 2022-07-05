package kitchenpos.ui.acceptance;

import static kitchenpos.ui.acceptance.MenuAcceptanceTest.메뉴_생성_요청;
import static kitchenpos.ui.acceptance.MenuGroupAcceptanceTest.메뉴그룹_생성_요청;
import static kitchenpos.ui.acceptance.TableAcceptanceTest.테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.menus.menu.dto.MenuProductRequest;
import kitchenpos.menus.menu.dto.MenuResponse;
import kitchenpos.menus.menugroup.dto.MenuGroupResponse;
import kitchenpos.orders.order.domain.OrderStatus;
import kitchenpos.orders.order.dto.ChangeOrderStatusRequest;
import kitchenpos.orders.order.dto.OrderLineItemRequest;
import kitchenpos.orders.order.dto.OrderRequest;
import kitchenpos.orders.order.dto.OrderResponse;
import kitchenpos.orders.table.dto.OrderTableResponse;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문 관련 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    private static final String ORDER_URL = "/api/orders/";
    private ProductResponse 짬뽕;
    private MenuGroupResponse 중식;
    private MenuResponse 중식_메뉴;
    private OrderTableResponse 테이블;
    private OrderResponse 주문;

    @TestFactory
    Stream<DynamicTest> orderTest() {
        return Stream.of(
                dynamicTest("상품, 메뉴그룹, 메뉴, 테이블 준비", () -> {
                    짬뽕 = ProductAcceptanceTest.상품_생성_요청("짬뽕", BigDecimal.valueOf(10000L)).as(ProductResponse.class);
                    중식 = 메뉴그룹_생성_요청("중식").as(MenuGroupResponse.class);
                    테이블 = 테이블_생성_요청(3, false).as(OrderTableResponse.class);

                    중식_메뉴 = 메뉴_생성_요청("중식_메뉴", 1000L, 중식.getId(),
                            Arrays.asList(MenuProductRequest.of(짬뽕.getId(), 1L))).as(MenuResponse.class);
                }),
                dynamicTest("주문 생성", () -> {
                    ExtractableResponse<Response> 주문_생성_요청 = 주문_생성_하기(테이블.getId(), 중식_메뉴.getId(), 1L);
                    주문_생성됨(주문_생성_요청);
                    주문 = 주문_생성_요청.as(OrderResponse.class);
                }),
                dynamicTest("주문 조회", () -> {
                    ExtractableResponse<Response> 주문_조회_요청 = 주문_조회_요청();
                    주문_조회됨(주문_조회_요청);
                }),
                dynamicTest("주문 상태 수정(식사)", () -> {
                    ExtractableResponse<Response> 주문_상태_수정_요청 = 주문_상태_수정_요청(주문.getId(), OrderStatus.MEAL);
                    주문_수정됨(주문_상태_수정_요청);
                }),
                dynamicTest("시스템에 없는 주문 상태 수정", () -> {
                    ExtractableResponse<Response> 주문_상태_수정_요청 = 주문_상태_수정_요청(1000L, OrderStatus.MEAL);
                    주문_수정_실패(주문_상태_수정_요청);
                })
        );
    }

    public static ExtractableResponse<Response> 주문_생성_하기(Long tableId, Long menuId, Long quantity) {
        return 주문_생성_요청(tableId,
                Arrays.asList(OrderLineItemRequest.of(menuId, quantity)));
    }

    public static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_수정_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 주문_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 주문_생성_요청(Long
                                                                 orderTableId,
                                                         List<OrderLineItemRequest> orderLineItems) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(OrderRequest.of(orderTableId, orderLineItems))
                .when().post(ORDER_URL)
                .then().log().all().
                extract();
    }


    public static ExtractableResponse<Response> 주문_상태_수정_요청(Long
                                                                    orderTableId,
                                                            OrderStatus status) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(ChangeOrderStatusRequest.of(orderTableId, status.name()))
                .when().put(ORDER_URL + orderTableId + "/order-status")
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 주문_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get(ORDER_URL)
                .then().log().all()
                .extract();
    }


}
