package kitchenpos.acceptance;


import static kitchenpos.acceptance.step.OrderAcceptanceStep.*;
import static kitchenpos.acceptance.step.MenuAcceptanceStep.메뉴_등록됨;
import static kitchenpos.acceptance.step.MenuGroupAcceptanceStep.메뉴그룹_등록됨;
import static kitchenpos.acceptance.step.TableAcceptanceStep.주문테이블_생성됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 관리 기능")
class OrderAcceptanceTest extends AcceptanceTest {

    private MenuGroup 치킨류;
    private Menu 메뉴_치킨;
    private OrderLineItem 메뉴_항목;
    private OrderTable 주문테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        치킨류 = 메뉴그룹_등록됨(new MenuGroup("치킨류"));
        메뉴_치킨 = 메뉴_등록됨(Menu.of("치킨메뉴", 16000, 치킨류.getId(),
            Collections.singletonList(MenuProduct.of(1L, 1L))));
        메뉴_항목 = OrderLineItem.of(1L, 메뉴_치킨.getId(), 1L);
        주문테이블 = 주문테이블_생성됨(new OrderTable());
    }


    @Test
    @DisplayName("주문관리 한다.")
    void 주문관리_기능() {
        // given
        Order 주문 = Order.of(주문테이블.getId(), Collections.singletonList(메뉴_항목));

        // when
        ExtractableResponse<Response> 주문_등록_결과 = 주문_등록_요청(주문);
        // then
        Order 등록된_주문 = 주문_등록_검증(주문_등록_결과, 주문);

        // when
        ExtractableResponse<Response> 주문_목록조회_결과 = 주문_목록조회_요청();
        // then
        주문_목록조회_검증(주문_목록조회_결과, 등록된_주문);

        // when
        등록된_주문.setOrderStatus(OrderStatus.MEAL.name());
        ExtractableResponse<Response> 주문_상태변경_결과 = 주문_상태변경_요청(등록된_주문.getId(), 등록된_주문);
        // then
        주문_상태변경_검증(주문_상태변경_결과, OrderStatus.MEAL.name());
    }
}
