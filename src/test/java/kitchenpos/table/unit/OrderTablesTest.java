package kitchenpos.table.unit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문테이블 목록 관련 단위테스트")
public class OrderTablesTest {
    private OrderTable 빈_테이블_A;
    private OrderTable 빈_테이블_B;
    private OrderTable 채워진_테이블;
    private OrderLineItems 주문상품;
    @BeforeEach
    void setUp() {
        MenuGroup 중식 = MenuGroup.of("중식");
        Product 짜장면 = Product.of("짜장면", BigDecimal.valueOf(6000));
        Product 군만두 = Product.of("군만두", BigDecimal.valueOf(2000));
        MenuProduct 짜장면_1개 = MenuProduct.of(짜장면, 1);
        MenuProduct 군만두_2개 = MenuProduct.of(군만두, 2);
        Menu 짜장세트 = Menu.of("짜장세트", BigDecimal.valueOf(6000), 중식, Arrays.asList(짜장면_1개, 군만두_2개));
        주문상품 = OrderLineItems.of(Arrays.asList(OrderLineItem.of(짜장세트, 1)));
        채워진_테이블 = OrderTable.of(2, false);
        빈_테이블_A = OrderTable.of(2, true);
        빈_테이블_B = OrderTable.of(2, true);
    }


    @DisplayName("주문테이블 목록 전체의 주문이 종료되었는지 확인할 수 있다.")
    @Test
    void isAllFinished() {
        // given
        Order.of(채워진_테이블, 주문상품);
        OrderTables 주문테이블목록 = OrderTables.of(Arrays.asList(채워진_테이블));
        // when - then
        assertFalse(주문테이블목록.isAllFinished());
    }

    @DisplayName("주문테이블 목록 전체가 빈 테이블인지 확인할 수 있다.")
    @Test
    void isAllEmpty() {
        // given
        OrderTables 주문테이블목록 = OrderTables.of(Arrays.asList(빈_테이블_A, 채워진_테이블));
        // when - then
        assertFalse(주문테이블목록.isAllEmpty());
    }

    @DisplayName("주문테이블중 단체지정된 테이블이 있는지 확인할 수 있다.")
    @Test
    void isAnyGrouped() {
        // given
        OrderTables 주문테이블목록 = OrderTables.of(Arrays.asList(빈_테이블_A, 빈_테이블_B));
        TableGroup.of(주문테이블목록);
        // when - then
        assertTrue(주문테이블목록.isAnyGrouped());
    }

}
