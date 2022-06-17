package kitchenpos.domain;

import static kitchenpos.domain.MenuAcceptanceTestMethod.*;
import static kitchenpos.domain.MenuGroupAcceptanceTestMethod.*;
import static kitchenpos.domain.OrderAcceptanceTestMethod.*;
import static kitchenpos.domain.ProductAcceptanceTestMethod.*;
import static kitchenpos.domain.TableAcceptanceTestMethod.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.AcceptanceTest;
import kitchenpos.application.fixture.MenuFixtureFactory;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.MenuProductFixtureFactory;
import kitchenpos.application.fixture.OrderFixtureFactory;
import kitchenpos.application.fixture.OrderLineItemFixtureFactory;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 관련 인수테스트")
class OrderAcceptanceTest extends AcceptanceTest {

    private MenuGroup 초밥_메뉴그룹;
    private Product 우아한_초밥_1;
    private Product 우아한_초밥_2;
    private MenuProduct A_우아한_초밥_1;
    private MenuProduct A_우아한_초밥_2;
    private Menu A_메뉴;
    private OrderTable 주문_테이블;
    private Order A_주문;
    private OrderLineItem A_주문항목;

    @BeforeEach
    public void setUp() {
        super.setUp();

        초밥_메뉴그룹 = 메뉴_그룹_등록되어_있음(MenuGroupFixtureFactory.create(1L, "초밥_메뉴그룹")).as(MenuGroup.class);
        우아한_초밥_1 = 상품_등록되어_있음(ProductFixtureFactory.create(1L, "우아한_초밥_1", BigDecimal.valueOf(10_000))).as(Product.class);
        우아한_초밥_2 = 상품_등록되어_있음(ProductFixtureFactory.create(2L, "우아한_초밥_2", BigDecimal.valueOf(20_000))).as(Product.class);

        A_메뉴 = MenuFixtureFactory.create("A", BigDecimal.valueOf(30_000), 초밥_메뉴그룹.getId());
        A_우아한_초밥_1 = MenuProductFixtureFactory.create(1L, A_메뉴.getId(), 우아한_초밥_1.getId(), 1);
        A_우아한_초밥_2 = MenuProductFixtureFactory.create(2L, A_메뉴.getId(), 우아한_초밥_2.getId(), 2);
        A_메뉴.setMenuProducts(Lists.newArrayList(A_우아한_초밥_1, A_우아한_초밥_2));
        A_메뉴 = 메뉴_등록되어_있음(A_메뉴).as(Menu.class);

        주문_테이블 = 테이블_등록되어_있음(OrderTableFixtureFactory.create(1L, false)).as(OrderTable.class);

        A_주문 = OrderFixtureFactory.create(주문_테이블.getId(), OrderStatus.COOKING);
        A_주문항목 = OrderLineItemFixtureFactory.create(A_주문.getId(), A_메뉴.getId(), 1);
        A_주문.setOrderLineItems(Lists.newArrayList(A_주문항목));
    }

    @DisplayName("주문을 할 수 있다.")
    @Test
    void create01() {
        // when
        ExtractableResponse<Response> response = 주문_등록_요청(A_주문);

        // then
        주문_등록됨(response);
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        ExtractableResponse<Response> createdResponse = 주문_등록되어_있음(A_주문);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        주문_목록_응답됨(response);
        주문_목록_포함됨(response, Lists.newArrayList(createdResponse));
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void change02() {
        // given
        Order createdOrder = 주문_등록되어_있음(A_주문).as(Order.class);
        createdOrder.setOrderStatus(OrderStatus.MEAL.name());

        // when
        ExtractableResponse<Response> response = 주문_상태_변경_요청(createdOrder.getId(), createdOrder);

        // then
        주문_상태_변경됨(response, OrderStatus.MEAL.name());
    }
}