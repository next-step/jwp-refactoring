package kitchenpos.order.ui;

import static kitchenpos.menu.ui.MenuAcceptanceTestFixture.메뉴_생성_되어있음;
import static kitchenpos.menugroup.ui.MenuGroupAcceptanceTestFixture.메뉴_그룹_생성되어있음;
import static kitchenpos.ordertable.ui.OrderTableAcceptanceTestFixture.주문_테이블_생성_되어있음;
import static kitchenpos.product.ui.ProductAcceptanceTestFixture.상품_생성_되어있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderAcceptanceTestFixture extends AcceptanceTest {

    ProductResponse 떡볶이;
    ProductResponse 튀김;
    ProductResponse 순대;

    public MenuProductRequest 떡튀순_상품_떡볶이;
    public MenuProductRequest 떡튀순_상품_튀김;
    public MenuProductRequest 떡튀순_상품_순대;
    public MenuProductRequest 떡튀순_곱배기_상품_떡볶이;

    public List<MenuProductRequest> 떡튀순_상품_목록;
    public List<MenuProductRequest> 떡튀순_곱배기_상품_목록;
    public MenuGroupResponse 세트;
    public MenuResponse 떡튀순;
    public MenuResponse 떡튀순_곱배기;

    public OrderTable 주문_테이블;
    public OrderLineItemRequest 주문_아이템_1;
    public OrderLineItemRequest 주문_아이템_2;
    public List<OrderLineItemRequest> 주문_아이템_목록;
    public OrderRequest 주문;

    @BeforeEach
    public void setUp() {
        super.setUp();

        세트 = 메뉴_그룹_생성되어있음(new MenuGroupRequest("세트"));

        떡볶이 = 상품_생성_되어있음(new ProductRequest("떡볶이", BigDecimal.valueOf(4500)));
        튀김 = 상품_생성_되어있음(new ProductRequest("튀김", BigDecimal.valueOf(2500)));
        순대 = 상품_생성_되어있음(new ProductRequest("순대", BigDecimal.valueOf(4000)));

        떡튀순_상품_떡볶이 = new MenuProductRequest(떡볶이.getId(), 1);
        떡튀순_상품_튀김 = new MenuProductRequest(튀김.getId(), 1);
        떡튀순_상품_순대 = new MenuProductRequest(순대.getId(), 1);
        떡튀순_곱배기_상품_떡볶이 = new MenuProductRequest(떡볶이.getId(), 2);

        떡튀순_상품_목록 = Arrays.asList(떡튀순_상품_떡볶이, 떡튀순_상품_튀김, 떡튀순_상품_순대);
        떡튀순_곱배기_상품_목록 = Arrays.asList(떡튀순_곱배기_상품_떡볶이, 떡튀순_상품_튀김, 떡튀순_상품_순대);

        떡튀순 = 메뉴_생성_되어있음(new MenuRequest("떡튀순", BigDecimal.valueOf(10000), 세트.getId(), 떡튀순_상품_목록));
        떡튀순_곱배기 = 메뉴_생성_되어있음(new MenuRequest("떡튀순_곱배기", BigDecimal.valueOf(10000), 세트.getId(), 떡튀순_곱배기_상품_목록));

        주문_테이블 = 주문_테이블_생성_되어있음(new OrderTable(null, null, 0, false));

        주문_아이템_1 = new OrderLineItemRequest(떡튀순.getId(), 2);
        주문_아이템_2 = new OrderLineItemRequest(떡튀순_곱배기.getId(), 1);
        주문_아이템_목록 = Arrays.asList(주문_아이템_1, 주문_아이템_2);
        주문 = new OrderRequest(주문_테이블.getId(), OrderStatus.COOKING.name(), 주문_아이템_목록);
    }

    public static ExtractableResponse<Response> 주문_등록_요청(OrderRequest orderRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static OrderResponse 주문_등록_되어있음(OrderRequest orderRequest) {
        return 주문정보(주문_등록_요청(orderRequest));
    }

    public static OrderResponse 주문정보(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("", OrderResponse.class);
    }

    public static void 주문_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 주문_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static List<OrderResponse> 주문_목록(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("", OrderResponse.class);
    }

    public static void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(Long orderId, OrderRequest orderRequest) {
        return RestAssured
                .given().log().all()
                .body(orderRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/orders/" + orderId + "/order-status")
                .then().log().all()
                .extract();
    }

    public static void 주문_상태_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_상태_수정되지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isIn(Arrays.asList(HttpStatus.BAD_REQUEST.value()
                , HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
