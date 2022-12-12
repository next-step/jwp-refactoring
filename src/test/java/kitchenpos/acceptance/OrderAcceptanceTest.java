package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.*;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.singletonList;
import static kitchenpos.acceptance.MenuAcceptanceStep.등록된_메뉴;
import static kitchenpos.acceptance.MenuGroupAcceptanceStep.등록된_메뉴_그룹;
import static kitchenpos.acceptance.OrderAcceptanceStep.*;
import static kitchenpos.acceptance.OrderTableAcceptanceStep.등록된_주문_테이블;
import static kitchenpos.acceptance.ProductAcceptanceStep.등록된_상품;
import static kitchenpos.fixture.MenuGroupTestFixture.createMenuGroup;
import static kitchenpos.fixture.MenuProductTestFixture.*;
import static kitchenpos.fixture.MenuTestFixture.*;
import static kitchenpos.fixture.OrderLineItemTestFixture.createOrderLineItem;
import static kitchenpos.fixture.OrderTableTestFixture.createOrderTable;
import static kitchenpos.fixture.OrderTestFixture.createOrder;
import static kitchenpos.fixture.ProductTestFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 관련 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    private Product 짜장면;
    private Product 짬뽕;
    private Product 단무지;
    private Product 탕수육;
    private MenuGroup 중국집_1인_메뉴_세트;
    private MenuProduct 짜장면메뉴상품;
    private MenuProduct 짬뽕메뉴상품;
    private MenuProduct 단무지메뉴상품;
    private MenuProduct 탕수육메뉴상품;
    private MenuResponse 짜장면_탕수육_1인_메뉴_세트;
    private MenuResponse 짬뽕_탕수육_1인_메뉴_세트;
    private OrderTableResponse 주문테이블1;
    private OrderTableResponse 주문테이블2;
    private OrderLineItem 짜장면_탕수육_1인_메뉴_세트주문;
    private OrderLineItem 짬뽕_탕수육_1인_메뉴_세트주문;
    private OrderRequest 주문1;
    private OrderRequest 주문2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        중국집_1인_메뉴_세트 = 등록된_메뉴_그룹(createMenuGroup("중국집_1인_메뉴_세트")).as(MenuGroup.class);
        짜장면 = 등록된_상품(createProduct("짜장면", BigDecimal.valueOf(8000L))).as(Product.class);
        탕수육 = 등록된_상품(createProduct("탕수육", BigDecimal.valueOf(12000L))).as(Product.class);
        짬뽕 = 등록된_상품(createProduct("짬뽕", BigDecimal.valueOf(9000L))).as(Product.class);
        단무지 = 등록된_상품(createProduct("단무지", BigDecimal.valueOf(0L))).as(Product.class);
        짜장면메뉴상품 = 짜장면메뉴상품();
        탕수육메뉴상품 = 탕수육메뉴상품();
        짬뽕메뉴상품 = 짬뽕메뉴상품();
        단무지메뉴상품 = 단무지메뉴상품();
        짜장면_탕수육_1인_메뉴_세트 = 등록된_메뉴(createMenu("짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L), 중국집_1인_메뉴_세트.getId(), Arrays.asList(짜장면메뉴상품, 탕수육메뉴상품, 단무지메뉴상품))).as(MenuResponse.class);
        짬뽕_탕수육_1인_메뉴_세트 = 등록된_메뉴(createMenu("짬뽕_탕수육_1인_메뉴_세트", BigDecimal.valueOf(21000L), 중국집_1인_메뉴_세트.getId(), Arrays.asList(짬뽕메뉴상품, 탕수육메뉴상품, 단무지메뉴상품))).as(MenuResponse.class);
        주문테이블1 = 등록된_주문_테이블(createOrderTable(null, 10, false)).as(OrderTableResponse.class);
        주문테이블2 = 등록된_주문_테이블(createOrderTable(null, 20, false)).as(OrderTableResponse.class);
        짜장면_탕수육_1인_메뉴_세트주문 = createOrderLineItem(1L, null, 짜장면_탕수육_1인_메뉴_세트.getId(), 1);
        짬뽕_탕수육_1인_메뉴_세트주문 = createOrderLineItem(2L, null, 짬뽕_탕수육_1인_메뉴_세트.getId(), 1);
        주문1 = createOrder(주문테이블1.getId(), null, null, Arrays.asList(짜장면_탕수육_1인_메뉴_세트주문, 짬뽕_탕수육_1인_메뉴_세트주문));
        주문2 = createOrder(주문테이블2.getId(), null, null, singletonList(짜장면_탕수육_1인_메뉴_세트주문));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> response = 주문_생성_요청(주문1);

        // then
        주문_생성됨(response);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // given
        ExtractableResponse<Response> createResponse1 = 등록된_주문(주문1);
        ExtractableResponse<Response> createResponse2 = 등록된_주문(주문2);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        주문_목록_응답됨(response);
        주문_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderResponse orderResponse = 등록된_주문(주문1).as(OrderResponse.class);
        Order order = Order.of(
                OrderTable.of(1L, null, 10, false),
                Collections.singletonList(짜장면_탕수육_1인_메뉴_세트주문)
        );
        order.changeOrderStatus(OrderStatus.COMPLETION.name());

        // when
        ExtractableResponse<Response> response = 주문_상태_변경_요청(orderResponse.getId(), order);

        // then
        주문_상태_변경됨(response, order.getOrderStatus());
    }

    private static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }
}
