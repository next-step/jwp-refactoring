package kitchenpos.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import kitchenpos.common.Price;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.MenuProducts;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.product.Product;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTables;
import kitchenpos.table.TableGroup;

public class TestDataSet {
    public static final MenuGroup 추천_메뉴_그륩 = new MenuGroup(1L, "추천메뉴");
    public static final MenuGroup 계절_메뉴_그룹 = new MenuGroup(2L, "계절메뉴");
    public static final Product 강정치킨 = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
    public static final Product 양념치킨 = new Product(2L, "양념치킨", BigDecimal.valueOf(15000));
    public static final Product 후라이드 = new Product(3L, "후라이드", BigDecimal.valueOf(10000));
    public static final MenuProduct 후라이드_2개 = new MenuProduct(1L, 후라이드, 2L);
    public static final MenuProduct 양념_2개 = new MenuProduct(2L, 양념치킨, 2L);
    public static final OrderTable 테이블_1번 = new OrderTable(1L, 4, true);
    public static final OrderTable 테이블_2번 = new OrderTable(2L, 2, true);
    public static final OrderTable 테이블_3번_존재 = new OrderTable(3L, 2, false);
    public static final OrderTable 테이블_4번_존재 = new OrderTable(4L, 4, false);
    public static final TableGroup 산악회 = new TableGroup(1L, LocalDateTime.now(),
        new OrderTables(Arrays.asList(테이블_1번, 테이블_2번)));

    public static final Menu 원플원_후라이드 = new Menu(1L, "후라이드+후라이드", Price.of(BigDecimal.valueOf(19000)), 추천_메뉴_그륩,
        MenuProducts.of(Arrays.asList(후라이드_2개)));

    public static final Menu 원플원_양념 = new Menu(2L, "양념+양념", Price.of(19000), 추천_메뉴_그륩,
        MenuProducts.of(Arrays.asList(양념_2개)));

    public static final Order 주문_1번 = new Order(1L, 테이블_3번_존재.getId(),
        Arrays.asList(new OrderLineItem(1L, null, 원플원_후라이드.getId(), 1L),
            new OrderLineItem(1L, null, 원플원_양념.getId(), 1L)));

    public static final Order 주문_2번 = new Order(2L, 테이블_4번_존재.getId(),
        Arrays.asList(new OrderLineItem(원플원_후라이드.getId(), 1L), new OrderLineItem(원플원_양념.getId(), 2L)));
}
