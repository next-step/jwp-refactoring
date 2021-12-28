package kitchenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.RestAssuredFixture;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.fixture.OrderLineItemFixture;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.table.acceptance.TableAcceptanceTest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/orders";

    private OrderTableResponse 주문_테이블;
    private OrderLineItem 주문_항목;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        Long menuId = 메뉴_등록되어_있음();
        주문_테이블 = TableAcceptanceTest.주문_테이블_등록되어_있음(4, false);
        주문_항목 = OrderLineItemFixture.create(null, null, menuId, 1L);
    }

    @DisplayName("주문을 관리한다.")
    @Test
    void manageOrder() {
        // given
        OrderRequest orderRequest = OrderRequest.of(
                주문_테이블.getId(), Arrays.asList(OrderLineItemRequest.of(주문_항목.getMenuId(), 주문_항목.getQuantity().getQuantity())));

        // when
        ExtractableResponse<Response> 주문_생성_응답 = 주문_생성_요청(orderRequest);
        // then
        주문_생성_응답됨(주문_생성_응답);

        // when
        ExtractableResponse<Response> 주문_목록_조회_응답 = 주문_목록_조회_요청();
        // then
        주문_목록_조회됨(주문_목록_조회_응답);

        // given
        OrderStatus 주문_상태 = OrderStatus.MEAL;
        // when
        Long 주문_ID = 주문_ID_조회(주문_생성_응답);
        ExtractableResponse<Response> 주문_상태_수정_응답 = 주문_상태_수정_요청(주문_ID, 주문_상태);
        // then
        주문_상태_수정됨(주문_상태_수정_응답, 주문_상태);
    }

    private ExtractableResponse<Response> 주문_생성_요청(OrderRequest params) {
        return RestAssuredFixture.생성_요청(API_URL, params);
    }

    private void 주문_생성_응답됨(ExtractableResponse<Response> response) {
        RestAssuredFixture.생성됨_201_CREATED(response);
    }

    private ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssuredFixture.목록_조회_요청(API_URL);
    }

    private void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        RestAssuredFixture.성공_200_OK(response);
    }

    private Long 주문_ID_조회(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> 주문_상태_수정_요청(Long orderId, OrderStatus orderStatus) {
        String path = String.format("%s/%s/order-status", API_URL, orderId);
        Map<String, String> params = new HashMap<>();
        params.put("orderStatus", orderStatus.name());

        return RestAssuredFixture.수정_요청(path, params);
    }

    private void 주문_상태_수정됨(ExtractableResponse<Response> response, OrderStatus orderStatus) {
        assertAll(
                () -> RestAssuredFixture.성공_200_OK(response)
                , () -> assertThat(response.jsonPath().getString("orderStatus")).isEqualTo(orderStatus.name())
        );
    }

    private Long 메뉴_등록되어_있음() {
        상품_생성_요청();
        메뉴_그룹_생성_요청();
        return 메뉴_생성_요청().jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> 상품_생성_요청() {
        ProductRequest params = ProductRequest.of("강정치킨", BigDecimal.valueOf(17_000));

        return RestAssuredFixture.생성_요청("/api/products", params);
    }

    private ExtractableResponse<Response> 메뉴_그룹_생성_요청() {
        MenuGroup params = MenuGroup.of("추천_메뉴_그룹");

        return RestAssuredFixture.생성_요청("/api/menu-groups", params);
    }

    private ExtractableResponse<Response> 메뉴_생성_요청() {
        MenuRequest params = MenuRequest.of(
                "강정치킨_두마리_세트_메뉴"
                , BigDecimal.valueOf(30_000)
                , 1L
                , Arrays.asList(MenuProductRequest.of(1L, 2L)));

        return RestAssuredFixture.생성_요청("/api/menus", params);
    }
}
