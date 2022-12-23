package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.table.domain.OrderTableFixture.주문테이블;
import static kitchenpos.menu.domain.MenuFixture.메뉴;
import static kitchenpos.order.domain.OrderFixture.주문;
import static kitchenpos.order.domain.OrderLineItemFixture.주문라인아이템;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Order 클래스 테스트")
public class OrderLineItemsTest {

    private OrderTable 테이블1;

    private Order 주문;

    private Menu 풀코스;
    private Menu 오일2인세트;

    private OrderLineItem 풀코스_주문;
    private OrderLineItem 오일2인세트_주문;

    @BeforeEach
    void setup() {
        테이블1 = 주문테이블(1L, null, 6, false);

        주문 = 주문(1L, OrderStatus.COOKING.name(), 테이블1);

        오일2인세트 = 메뉴(1L, "오일2인세트", 34000, null);
        풀코스 = 메뉴(2L, "풀코스", 62000, null);

        풀코스_주문 = 주문라인아이템(1L, 주문, 풀코스, 1);
        오일2인세트_주문 = 주문라인아이템(2L, 주문, 오일2인세트, 1);
    }

    @DisplayName("OrderLineItem 리스트를 추가한다")
    @Test
    void OrderLineItem_리스트_추가_테스트() {
        // given
        List<OrderLineItem> orderLineItemList = Arrays.asList(풀코스_주문, 오일2인세트_주문);

        // when
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.addList(orderLineItemList);

        // then
        assertThat(orderLineItems.getOrderLineItems()).containsExactly(풀코스_주문, 오일2인세트_주문);
    }

    @DisplayName("빈 리스트를 추가한다")
    @Test
    void 빈_리스트_추가_테스트() {
        // given
        OrderLineItems orderLineItems = new OrderLineItems();

        // when & then
        assertThatThrownBy(
                () -> orderLineItems.addList(Arrays.asList())
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
