package kitchenpos.tablegroup.domain;

import kitchenpos.common.exception.OrderStatusNotCompletedException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuId;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("테이블 그룹 도메인 테스트")
class TableGroupTest {
    private Product 짜장면_상품;
    private OrderLineItem 짜장면_주문1;
    private OrderLineItem 짜장면_주문2;
    private Order 주문;
    private Menu 짜장면;
    private MenuId 짜장면_ID;
    private MenuProduct 짜장면_하나;
    private MenuProduct 짜장면_두개;

    private TableGroup 단체테이블1번;
    private OrderTable 주문테이블1번;
    private OrderTable 주문테이블2번;

    @BeforeEach
    void setUp() {

        짜장면_상품 = new Product("짜장면", new BigDecimal(1000));
        짜장면_하나 = new MenuProduct(1L, new Menu(), 짜장면_상품, 1);
        짜장면_두개 = new MenuProduct(2L, new Menu(), 짜장면_상품, 2);
        짜장면 = new Menu("짜장면", 10000, new MenuGroup(), Lists.newArrayList(짜장면_하나, 짜장면_두개));
        짜장면_ID = new MenuId(짜장면.getId());

        짜장면_주문1 = new OrderLineItem(주문, 짜장면_ID, 10);
        짜장면_주문2 = new OrderLineItem(주문, 짜장면_ID, 3);

        주문테이블1번 = new OrderTable(null, 0);

        주문테이블2번 = new OrderTable(null, 0);
        단체테이블1번 = new TableGroup(1L, Lists.newArrayList(주문테이블1번, 주문테이블2번));
    }

    @DisplayName("테이블 그룹을 생성할 수 있다.")
    @Test
    void createTableGroupTest() {
        assertAll(
                () -> assertThat(단체테이블1번.getOrderTables().get(0)).isEqualTo(주문테이블1번)
        );
    }

    @DisplayName("테이블 그룹은 주문 상태가 cooking or meal 이 아니어야 한다.")
    @Test
    void ungroupTableGroupEmptyOrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            // given
            단체테이블1번 = new TableGroup(1L, Arrays.asList(주문테이블1번, 주문테이블2번));

            주문테이블1번.changeNumberOfGuests(3);

            주문 = new Order(주문테이블1번, Arrays.asList(짜장면_주문1, 짜장면_주문2));
            주문.changeOrderStatus(OrderStatus.COOKING);

            주문테이블1번.addOrder(주문);

            // when
            단체테이블1번.ungroup();

            // then
        }).isInstanceOf(OrderStatusNotCompletedException.class);
    }
}