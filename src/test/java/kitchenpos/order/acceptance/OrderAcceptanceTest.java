package kitchenpos.order.acceptance;

import static kitchenpos.menu.acceptance.MenuGroupTestFixture.메뉴_그룹_생성_요청함;
import static kitchenpos.menu.acceptance.MenuTestFixture.메뉴_생성_요청함;
import static kitchenpos.order.acceptance.OrderTestFixture.주문_상태_변경됨;
import static kitchenpos.order.acceptance.OrderTestFixture.주문_상태변경_요청함;
import static kitchenpos.order.acceptance.OrderTestFixture.주문_생성_요청함;
import static kitchenpos.order.acceptance.OrderTestFixture.주문_생성됨;
import static kitchenpos.order.acceptance.OrderTestFixture.주문_요청_응답됨;
import static kitchenpos.order.acceptance.OrderTestFixture.주문_조회_요청함;
import static kitchenpos.order.acceptance.OrderTestFixture.주문_조회_포함됨;
import static kitchenpos.product.acceptance.ProductTestFixture.상품_생성_요청함;
import static kitchenpos.table.acceptance.TableTestFixture.주문_테이블_생성_요청함;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderRequest 주문요청;

    @BeforeEach
    public void setUp() {
        super.setUp();
        OrderTable 주문테이블 = 주문_테이블_생성_요청함(new OrderTable(null, null, 3, false)).as(OrderTable.class);
        MenuGroupResponse 메뉴분류세트 = 메뉴_그룹_생성_요청함("메뉴분류세트").as(MenuGroupResponse.class);
        ProductResponse 후라이드 = 상품_생성_요청함("후라이드", BigDecimal.valueOf(15000)).as(ProductResponse.class);
        ProductResponse 콜라 = 상품_생성_요청함("콜라", BigDecimal.valueOf(1000)).as(ProductResponse.class);
        MenuProductRequest 훌라이드메뉴상품요청 = new MenuProductRequest(후라이드.getId(), 1L);
        MenuProductRequest 콜라메뉴상품요청 = new MenuProductRequest(콜라.getId(), 1L);
        MenuResponse 후라이드세트 = 메뉴_생성_요청함(new MenuRequest("후라이드세트", BigDecimal.valueOf(16000), 메뉴분류세트.getId(),
                Arrays.asList(훌라이드메뉴상품요청, 콜라메뉴상품요청))).as(MenuResponse.class);
        OrderLineItemRequest 주문항목요청 = new OrderLineItemRequest(후라이드세트.getId(), 1L);
        주문요청 = new OrderRequest(주문테이블.getId(), null, Collections.singletonList(주문항목요청));
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        //when
        ExtractableResponse<Response> response = 주문_생성_요청함(주문요청);
        //then
        주문_생성됨(response);
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void list() {
        //given
        ExtractableResponse<Response> 주문생성_response = 주문_생성_요청함(주문요청);
        //when
        ExtractableResponse<Response> response = 주문_조회_요청함();
        //then
        주문_요청_응답됨(response);
        주문_조회_포함됨(response, Collections.singletonList(주문생성_response));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeStatus() {
        //given
        OrderStatus 식사중 = OrderStatus.MEAL;
        OrderResponse 주문 = 주문_생성_요청함(주문요청).as(OrderResponse.class);
        OrderRequest 상태변경요청 = new OrderRequest(null, 식사중, null);
        //when
        ExtractableResponse<Response> response = 주문_상태변경_요청함(주문.getId(), 상태변경요청);
        //then
        주문_요청_응답됨(response);
        주문_상태_변경됨(response, 식사중);
    }
}
