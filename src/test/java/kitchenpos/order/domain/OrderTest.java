package kitchenpos.order.domain;

import static java.util.Collections.singletonList;
import static kitchenpos.menu.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenu;
import static kitchenpos.order.domain.OrderLineItemTestFixture.generateOrderLineItem;
import static kitchenpos.order.domain.OrderMenuTestFixture.generateOrderMenu;
import static kitchenpos.order.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.order.domain.OrderTestFixture.generateOrder;
import static kitchenpos.product.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 관련 도메인 테스트")
public class OrderTest {

    private Product 치킨버거;
    private Product 불고기버거;
    private MenuProduct 치킨버거상품;
    private MenuProduct 불고기버거상품;
    private MenuGroup 햄버거단품;
    private Menu 불고기버거단품;
    private Menu 치킨버거단품;
    private OrderMenu 불고기버거단품주문메뉴;
    private OrderMenu 치킨버거단품주문메뉴;
    private OrderLineItem 치킨버거단품_주문_항목;
    private OrderLineItem 불고기버거단품_주문_항목;
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        치킨버거 = generateProduct(2L, "치킨버거", BigDecimal.valueOf(4000L));
        불고기버거 = generateProduct(3L, "불고기버거", BigDecimal.valueOf(3500L));
        불고기버거상품 = generateMenuProduct(불고기버거, 1L);
        치킨버거상품 = generateMenuProduct(치킨버거, 1L);
        햄버거단품 = generateMenuGroup(1L, "햄버거단품");
        치킨버거단품 = generateMenu(2L, "치킨버거단품", BigDecimal.valueOf(4000L), 햄버거단품, singletonList(치킨버거상품));
        불고기버거단품 = generateMenu(1L, "불고기버거단품", BigDecimal.valueOf(3500L), 햄버거단품, singletonList(불고기버거상품));
        치킨버거단품주문메뉴 = generateOrderMenu(치킨버거단품);
        불고기버거단품주문메뉴 = generateOrderMenu(불고기버거단품);
        치킨버거단품_주문_항목 = generateOrderLineItem(치킨버거단품주문메뉴, 2);
        불고기버거단품_주문_항목 = generateOrderLineItem(불고기버거단품주문메뉴, 1);
        주문테이블 = generateOrderTable(4, false);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // when
        Order order = generateOrder(주문테이블,  OrderLineItems.from(Arrays.asList(치킨버거단품_주문_항목, 불고기버거단품_주문_항목)));

        // then
        assertAll(
                () -> assertThat(order.getOrderTable()).isEqualTo(주문테이블),
                () -> assertThat(order.getOrderLineItems().unmodifiableOrderLineItems().stream().map(OrderLineItem::getOrderMenu)).containsExactly(치킨버거단품주문메뉴, 불고기버거단품주문메뉴)
        );
    }

    @DisplayName("주문 테이블이 비어있으면 주문 생성 시 오류가 발생한다.")
    @Test
    void createOrderThrowErrorWhenOrderTableIsEmpty() {
        // given
        OrderTable emptyOrderTable = generateOrderTable(4, true);

        // when & then
        assertThatThrownBy(
                () -> generateOrder(emptyOrderTable, OrderLineItems.from(Arrays.asList(치킨버거단품_주문_항목, 불고기버거단품_주문_항목))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.주문_테이블은_비어있으면_안됨.getErrorMessage());
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        Order order = generateOrder(주문테이블,  OrderLineItems.from(Arrays.asList(치킨버거단품_주문_항목, 불고기버거단품_주문_항목)));

        // when
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("이미 완료된 주문은 상태 변경 시 오류가 발생한다.")
    @Test
    void changeOrderStatusThrowErrorWhenAlreadyCompletion() {
        // given
        Order order = generateOrder(주문테이블,  OrderLineItems.from(Arrays.asList(치킨버거단품_주문_항목, 불고기버거단품_주문_항목)));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.이미_완료된_주문.getErrorMessage());
    }

    @DisplayName("완료되지 않은 주문이면 오류가 발생한다.")
    @Test
    void ifNotCompletionOrderThrowError() {
        // given
        Order order = generateOrder(주문테이블,  OrderLineItems.from(Arrays.asList(치킨버거단품_주문_항목, 불고기버거단품_주문_항목)));

        // when & then
        assertThatThrownBy(order::validateIfNotCompletionOrder)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.완료되지_않은_주문.getErrorMessage());
    }
}
