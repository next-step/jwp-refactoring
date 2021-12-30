package kitchenpos.order.acceptanceTest;

import static java.util.Arrays.asList;
import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_생성을_요청;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴그룹_생성_요청;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static kitchenpos.table.acceptance.OrderTableAcceptanceTest.주문테이블_생성_요청;
import static kitchenpos.utils.StatusValidation.변경됨;
import static kitchenpos.utils.StatusValidation.생성됨;
import static kitchenpos.utils.StatusValidation.조회됨;
import static kitchenpos.utils.TestFactory.get;
import static kitchenpos.utils.TestFactory.post;
import static kitchenpos.utils.TestFactory.put;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.moduledomain.order.OrderStatus;
import kitchenpos.moduledomain.table.NumberOfGuests;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderLineRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderAcceptanceTest extends AcceptanceTest {

    public static final String ORDERS_BASE_URL = "/orders";

    OrderRequest 주문;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // 주문테이블 생성
        OrderTableResponse 주문테이블 = 주문테이블_생성_요청(
            new OrderTableRequest(new NumberOfGuests(0), false)).as(
            OrderTableResponse.class);

        // 메뉴 생성
        상품_생성_요청(new ProductRequest(1L, "양념치킨", 16000L));
        상품_생성_요청(new ProductRequest(2L, "코카콜라", 2000L));

        메뉴그룹_생성_요청(new MenuGroupRequest("세트메뉴"));
        ;
        메뉴그룹_생성_요청(new MenuGroupRequest("후라이드 그룹"));

        MenuProductRequest 양념치킨_1개 = new MenuProductRequest(1L, 1L);
        MenuProductRequest 코카콜라_1개 = new MenuProductRequest(2L, 1L);

        MenuRequest 양념치킨_콜라세트 = new MenuRequest("양념치킨_콜라세트", 18000L, 1L,
            asList(양념치킨_1개, 코카콜라_1개));
        long id = 메뉴_생성을_요청(양념치킨_콜라세트).jsonPath().getLong("id");

        // 주문 생성
        OrderLineRequest 주문_상품 = new OrderLineRequest(id, 1L);
        주문 = new OrderRequest(주문테이블.getId(), asList(주문_상품));
    }

    @Test
    void 주문_을_생성한다() {
        ExtractableResponse<Response> response = 주문_생성_요청(주문);
        주문_생성됨(response);
    }

    @Test
    void 전체_주문_조회한다() {
        주문_생성_요청(주문);
        ExtractableResponse<Response> response = 주문_전체조회_요청();
        주문_조회됨(response);
    }

    @Test
    void 주문_상태를_변경한다() {
        OrderResponse createdOrder = 주문_생성_요청(주문).as(OrderResponse.class);
        ExtractableResponse<Response> response = 주문_상태_변경_요청(createdOrder.getId(),
            new ChangeOrderStatusRequest(OrderStatus.COMPLETION.name()));
        상태_변경됨(response);
    }

    public static ExtractableResponse<Response> 주문_생성_요청(OrderRequest orderRequest) {
        return post(ORDERS_BASE_URL, orderRequest);
    }

    public static void 주문_생성됨(ExtractableResponse<Response> response) {
        생성됨(response);
    }

    public static ExtractableResponse<Response> 주문_전체조회_요청() {
        return get(ORDERS_BASE_URL);
    }

    public static void 주문_조회됨(ExtractableResponse<Response> response) {
        조회됨(response);
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(Long id,
        ChangeOrderStatusRequest changeOrderStatusRequest) {
        return put(ORDERS_BASE_URL + "/{orderId}/order-status", "orderId", id,
            changeOrderStatusRequest);
    }

    public static void 상태_변경됨(ExtractableResponse<Response> response) {
        변경됨(response);
    }


}
