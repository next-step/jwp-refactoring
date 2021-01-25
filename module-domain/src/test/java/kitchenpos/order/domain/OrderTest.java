package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.utils.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static kitchenpos.utils.TestFixture.메뉴상품_양념;
import static kitchenpos.utils.TestFixture.메뉴상품_후라이드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OrderTest {

    private OrderTable 그룹이_지정되지_않은_비어있지_않은_테이블;
    private Menu 메뉴_후라이드;
    private Menu 메뉴_양념;
    private OrderLineItem orderLineItem_후라이드;
    private OrderLineItem orderLineItem_양념;

    @BeforeEach
    void setUp() {
        MenuGroup 메뉴그룹_한마리메뉴 = new MenuGroup(2L, "한마리메뉴");
        그룹이_지정되지_않은_비어있지_않은_테이블 = new OrderTable(3L, null, 0, false);

        메뉴_후라이드 = new Menu.Builder()
                .id(1L)
                .name("후라이드")
                .price(BigDecimal.valueOf(16000))
                .menuGroup(메뉴그룹_한마리메뉴)
                .menuProducts(Arrays.asList(TestFixture.메뉴상품_후라이드))
                .build();

        메뉴_양념 = new Menu.Builder()
                .id(2L)
                .name("양념")
                .price(BigDecimal.valueOf(16000))
                .menuGroup(메뉴그룹_한마리메뉴)
                .menuProducts(Arrays.asList(TestFixture.메뉴상품_양념))
                .build();

        orderLineItem_후라이드 = new OrderLineItem(메뉴_후라이드.getId(), 1);
        orderLineItem_양념 = new OrderLineItem(메뉴_양념.getId(), 1);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // when
        Order order = new Order.Builder()
                .id(1L)
                .orderTable(그룹이_지정되지_않은_비어있지_않은_테이블)
                .orderLineItems(Arrays.asList(orderLineItem_후라이드, orderLineItem_양념))
                .orderedTime(LocalDateTime.of(2021, 1, 20, 03, 30))
                .build();

        // then
        assertThat(order).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("하나 이상의 주문 항목을 가져야 한다.")
    @Test
    void requireOrderLineItem() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            Order order = new Order.Builder()
                    .id(1L)
                    .orderTable(그룹이_지정되지_않은_비어있지_않은_테이블)
                    .orderLineItems(new ArrayList<>())
                    .orderedTime(LocalDateTime.of(2021, 1, 20, 03, 30))
                    .build();
        }).withMessageMatching("주문은 1개 이상의 메뉴가 포함되어 있어야 합니다.");
    }

    @DisplayName("주문 테이블이 등록 불가 상태인 경우 생성할 수 없다.")
    @Test
    void emptyOrderTable() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            Order order = new Order.Builder()
                    .id(1L)
                    .orderTable(그룹이_지정되지_않은_비어있지_않은_테이블)
                    .orderLineItems(new ArrayList<>())
                    .orderedTime(LocalDateTime.of(2021, 1, 20, 03, 30))
                    .build();
        }).withMessageMatching("주문은 1개 이상의 메뉴가 포함되어 있어야 합니다.");
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void changeStatus() {
        // given
        Order order = new Order.Builder()
                .id(1L)
                .orderTable(그룹이_지정되지_않은_비어있지_않은_테이블)
                .orderLineItems(Arrays.asList(orderLineItem_후라이드, orderLineItem_양념))
                .orderedTime(LocalDateTime.of(2021, 1, 20, 03, 30))
                .build();

        // when
        order.updateOrderStatus(OrderStatus.COMPLETION.name());

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("주문의 상태가 이미 완료된 경우 변경할 수 없다.")
    @Test
    void notChangeStatus() {
        // given
        OrderTable 그룹_지정된_테이블 = new OrderTable(11L,5, false);
        Order order = new Order.Builder()
                .id(2L)
                .orderTable(그룹_지정된_테이블)
                .orderLineItems(Arrays.asList(orderLineItem_후라이드, orderLineItem_양념))
                .orderedTime(LocalDateTime.of(2021, 1, 20, 03, 30))
                .orderStatus(OrderStatus.COMPLETION)
                .build();

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            order.updateOrderStatus(OrderStatus.MEAL.name());
        }).withMessageMatching("주문 완료 시 상태를 변경할 수 없습니다.");

    }
}
