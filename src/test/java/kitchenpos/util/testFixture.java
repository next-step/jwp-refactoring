package kitchenpos.util;

import kitchenpos.domain.*;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.OrderTable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.util.testFixture.빈_주문테이블_3_생성;

public class testFixture {

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
        return MenuProduct.of(1L, 1L, productId, 1);
    }

    public static MenuProduct 양념_메뉴_상품_생성(Long productId) {
        return MenuProduct.of(2L, 2L, productId, 1);
    }

    public static Menu 후라이드_치킨_메뉴_생성(Long menuGroupId, List<MenuProduct> menuProducs) {
        return Menu.of(1L, "후라이드치킨", new BigDecimal(16000), menuGroupId, menuProducs);
    }

    public static Menu 양념_치킨_메뉴_생성(Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(1L, "후라이드치킨", new BigDecimal(16000), menuGroupId, menuProducts);
    }

    public static OrderLineItem 주문항목_1_생성() {
        return OrderLineItem.of(1L, 1L, 1L, 1L);
    }

    public static OrderLineItem 주문항목_2_생성() {
        return OrderLineItem.of(2L, 1L, 2L, 1L);
    }

    public static OrderLineItem 주문항목_3_생성() {
        return OrderLineItem.of(3L, 2L, 1L, 1L);
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

    public static OrderTable 주문테이블_3_생성() {
        return OrderTable.of(null, 3, true);
    }

    public static TableGroup 단체지정_1_생성(List<OrderTable> orderTables) {
        return TableGroup.of(orderTables);
    }

    public static TableGroup 단체지정_2_생성(List<OrderTable> orderTables) {
        return TableGroup.of(orderTables);
    }

    public static Order 주문_1_생성(List<OrderLineItem> orderLineItems) {
        return Order.of(1L, 1L, null, null, orderLineItems);
    }

    public static Order 주문_2_생성(List<OrderLineItem> orderLineItems) {
        return Order.of(1L, 1L, null, null, orderLineItems);
    }
}
