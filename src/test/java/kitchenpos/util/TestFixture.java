package kitchenpos.util;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.OrderTable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TestFixture {

    public static Product 후라이드_상품_생성() {
        return Product.of((long)1, "후라이드", new BigDecimal(16000));
    }

    public static Product 양념치킨_상품_생성() {
        return Product.of((long)2, "양념치킨", new BigDecimal(16000));
    }

    public static MenuGroup 한마리_메뉴_그룹_생성() {
        return MenuGroup.of("한마리메뉴");
    }

    public static MenuGroup 두마리_메뉴_그룹_생성() {
        return MenuGroup.of("두마리메뉴");
    }

    public static MenuProduct 후라이드_메뉴_상품_생성(Long productId) {
        return MenuProduct.of(후라이드_상품_생성(), 1);
    }

    public static MenuProduct 양념_메뉴_상품_생성(Long productId) {
        return MenuProduct.of(양념치킨_상품_생성(), 2);
    }

    public static Menu 후라이드_치킨_메뉴_생성(Long menuGroupId, List<MenuProduct> menuProducs) {
        return Menu.of("후라이드치킨", new BigDecimal(16000), 한마리_메뉴_그룹_생성(), menuProducs);
    }

    public static Menu 양념_치킨_메뉴_생성(Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of("양념치킨", new BigDecimal(16000), 한마리_메뉴_그룹_생성(), menuProducts);
    }

    public static OrderTable 주문테이블_1_생성() {
        return OrderTable.of(단체지정_1_생성(Arrays.asList(빈_주문테이블_1_생성(), 빈_주문테이블_2_생성())), 1, false);
    }

    public static OrderTable 주문테이블_2_생성() {
        return OrderTable.of(단체지정_2_생성(Arrays.asList(빈_주문테이블_3_생성())), 1, false);
    }

    public static OrderTable 빈_주문테이블_1_생성() {
        return OrderTable.of(1L, null, 2, true);
    }

    public static OrderTable 빈_주문테이블_2_생성() {
        return OrderTable.of(2L, null, 3, true);
    }

    public static OrderTable 빈_주문테이블_3_생성() {
        return OrderTable.of(3L, null, 3, true);
    }

    public static TableGroup 단체지정_1_생성(List<OrderTable> orderTables) {
        return TableGroup.of(orderTables);
    }

    public static TableGroup 단체지정_2_생성(List<OrderTable> orderTables) {
        return TableGroup.of(orderTables);
    }
}
