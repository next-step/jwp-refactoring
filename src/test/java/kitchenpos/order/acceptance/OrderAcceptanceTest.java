package kitchenpos.order.acceptance;//package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.acceptance.MenuAcceptanceTest;
import kitchenpos.menu.acceptance.MenuGroupAcceptanceTest;
import kitchenpos.menu.dto.CreateMenuRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.UpdateOrderStatusRequest;
import kitchenpos.product.acceptance.ProductAcceptanceTest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.acceptance.TableAcceptanceTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderAcceptanceTest extends AcceptanceTest {
    private static final String ORDER_URL = "/api/orders";

    private OrderTable 주문테이블;
    private MenuResponse 불고기_새우버거_메뉴;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        주문테이블 = TableAcceptanceTest.테이블_추가_되어_있음(OrderTable.of(0, true));
        final MenuGroupResponse 메뉴_그룹 = MenuGroupAcceptanceTest.메뉴_그룹_추가_되어_있음(MenuGroupAcceptanceTest.햄버거_메뉴);
        final ProductResponse 불고기버거 = ProductAcceptanceTest.제품_추가_되어_있음(ProductAcceptanceTest.불고기버거);
        final ProductResponse 새우버거 = ProductAcceptanceTest.제품_추가_되어_있음(ProductAcceptanceTest.새우버거);

        final List<MenuProductRequest> menuProductRequests = Arrays.asList(new MenuProductRequest(불고기버거.getId(), 5),
                new MenuProductRequest(새우버거.getId(), 1));

        final CreateMenuRequest menuRequest = new CreateMenuRequest("불고기버거 + 새우버거", BigDecimal.valueOf(8_000),
                메뉴_그룹.getId(), menuProductRequests);
        불고기_새우버거_메뉴 = MenuAcceptanceTest.메뉴가_추가_되어_있음(menuRequest);
    }

    /**
     * given 테이블에 손님이 setting 되어 있음
     * 메뉴가 등록 되어 있음
     * when 주문 요청을 함
     * then 주문 요청이 됨
     * <p>
     * when 전체 주문 요청 조회를 함
     * then 주무한 요청이 조회 됨
     * <p>
     * when 요청
     * then 요청한 주문이 변경 됨
     */
    @Test
    @DisplayName("주문 관리 테스트")
    void order() {
        // given
        final OrderRequest 주문 = new OrderRequest(주문테이블.getId(), Arrays.asList(new OrderLineItemRequest(불고기_새우버거_메뉴.getId(), 1)));
        // when
        final ExtractableResponse<Response> 주문_요청 = 주문_요청(주문);
        // then
        final OrderResponse 주문_요청_됨 = 주문_요청_됨(주문_요청);

        // when
        ExtractableResponse<Response> 주문_전체_조회 = 주문_전체_조회();
        // then
        final List<OrderResponse> 주문_조회_됨 = 주문_조회_됨(주문_전체_조회);
        assertThat(주문_조회_됨.stream().anyMatch(it -> it.getId() == 주문_요청_됨.getId())).isTrue();

        // then
        final ExtractableResponse<Response> 주문_상태_변경_요청 = 주문_상태_변경_요청(주문_요청_됨.getId(), new UpdateOrderStatusRequest(OrderStatus.COMPLETION));
        // when
        final OrderResponse 주문_상태_변경_됨 = 주문_상태_변경_됨(주문_상태_변경_요청);
        assertThat(주문_상태_변경_됨.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    public static ExtractableResponse<Response> 주문_요청(final OrderRequest order) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post(ORDER_URL)
                .then().log().all()
                .extract();
    }

    public static OrderResponse 주문_요청_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(OrderResponse.class);
    }

    public static OrderResponse 주문_요청_되어_있음(final OrderRequest order) {
        return 주문_요청_됨(주문_요청(order));
    }

    public static ExtractableResponse<Response> 주문_전체_조회() {
        return RestAssured.given()
                .when().get(ORDER_URL)
                .then()
                .extract();
    }

    public static List<OrderResponse> 주문_조회_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getList(".", OrderResponse.class);
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(final Long orderId, final UpdateOrderStatusRequest order) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().put(ORDER_URL + "/{orderId}/order-status", orderId)
                .then().log().all()
                .extract();
    }

    public static OrderResponse 주문_상태_변경_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.as(OrderResponse.class);
    }
}
