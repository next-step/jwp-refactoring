package kitchenpos.utils;

import java.math.BigDecimal;
import java.util.Arrays;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

public class UnitTestData {
    public static MenuGroup 추천메뉴;
    public static MenuGroup 베스트메뉴;
    public static MenuGroup 세트메뉴;

    public static MenuProduct 행복세트_치킨;
    public static MenuProduct 행복세트_피자;
    public static MenuProduct 행복세트_떡볶이;
    public static MenuProduct 치쏘세트_치킨;
    public static MenuProduct 치쏘세트_소주;
    public static MenuProduct 피맥세트_피자;
    public static MenuProduct 피맥세트_맥주;

    public static Menu 행복세트;
    public static Menu 치쏘세트;
    public static Menu 피맥세트;

    public static OrderLineItem 테이블1_행복세트;
    public static OrderLineItem 테이블1_치쏘세트;
    public static OrderLineItem 테이블2_행복세트;
    public static OrderLineItem 테이블2_피맥세트;
    public static OrderLineItem 테이블3_치쏘세트;
    public static OrderLineItem 테이블4_행복세트;
    public static OrderLineItem 테이블4_치쏘세트;
    public static OrderLineItem 테이블4_피맥세트;

    public static OrderTable 테이블1번_USING;
    public static OrderTable 테이블2번_USING;
    public static OrderTable 테이블3번_EMPTY;
    public static OrderTable 테이블4번_EMPTY;
    public static OrderTable 테이블5번_EMPTY;
    public static OrderTable 테이블6번_EMPTY;

    public static Order 주문1_결제완료;
    public static Order 주문2_식사중;

    public static Product 치킨;
    public static Product 피자;
    public static Product 떡볶이;
    public static Product 소주;
    public static Product 맥주;

    public static TableGroup 테이블_그룹;
    public static TableGroup 테이블_그룹_삭제_불가;

    public static void reset() {
        추천메뉴 = new MenuGroup(1L, "추천메뉴");
        베스트메뉴 = new MenuGroup(2L, "베스트메뉴");
        세트메뉴 = new MenuGroup(3L, "세트메뉴");

        치킨 = new Product(1L, "치킨", BigDecimal.valueOf(7000));
        피자 = new Product(2L, "피자", BigDecimal.valueOf(9900));
        떡볶이 = new Product(3L, "떡볶이", BigDecimal.valueOf(1000));
        소주 = new Product(4L, "소주", BigDecimal.valueOf(1000));
        맥주 = new Product(5L, "맥주", BigDecimal.valueOf(2000));

        행복세트_치킨 = new MenuProduct(1L, 1L, 치킨.getId(), 1);
        행복세트_피자 = new MenuProduct(2L, 1L, 피자.getId(), 1);
        행복세트_떡볶이 = new MenuProduct(3L, 1L, 떡볶이.getId(), 1);
        치쏘세트_치킨 = new MenuProduct(4L, 2L, 치킨.getId(), 1);
        치쏘세트_소주 = new MenuProduct(5L, 2L, 소주.getId(), 1);
        피맥세트_피자 = new MenuProduct(6L, 3L, 피자.getId(), 1);
        피맥세트_맥주 = new MenuProduct(7L, 3L, 맥주.getId(), 1);

        행복세트 = new Menu(1L, "행복세트", BigDecimal.valueOf(17900), 추천메뉴.getId(), Arrays.asList(행복세트_치킨, 행복세트_피자, 행복세트_떡볶이));
        치쏘세트 = new Menu(2L, "치쏘세트", BigDecimal.valueOf(8000), 세트메뉴.getId(), Arrays.asList(치쏘세트_치킨, 치쏘세트_소주));
        피맥세트 = new Menu(3L, "피맥세트", BigDecimal.valueOf(11900), 세트메뉴.getId(), Arrays.asList(피맥세트_피자, 피맥세트_맥주));

        테이블1_행복세트 = new OrderLineItem(1L, 1L, 행복세트.getId(), 1);
        테이블1_치쏘세트 = new OrderLineItem(2L, 1L, 치쏘세트.getId(), 2);
        테이블2_행복세트 = new OrderLineItem(3L, 2L, 행복세트.getId(), 1);
        테이블2_피맥세트 = new OrderLineItem(4L, 2L, 피맥세트.getId(), 2);
        테이블3_치쏘세트 = new OrderLineItem(5L, 3L, 치쏘세트.getId(), 1);
        테이블4_행복세트 = new OrderLineItem(6L, 3L, 행복세트.getId(), 1);
        테이블4_치쏘세트 = new OrderLineItem(7L, 3L, 치쏘세트.getId(), 1);
        테이블4_피맥세트 = new OrderLineItem(8L, 3L, 피맥세트.getId(), 1);

        테이블1번_USING = new OrderTable(1L, null, 4, false);
        테이블2번_USING = new OrderTable(2L, null, 4, false);
        테이블3번_EMPTY = new OrderTable(3L, null, 6, true);
        테이블4번_EMPTY = new OrderTable(4L, null, 2, true);
        테이블5번_EMPTY = new OrderTable(5L, 1L, 2, true);
        테이블6번_EMPTY = new OrderTable(6L, 1L, 2, true);

        테이블_그룹 = new TableGroup(1L, Arrays.asList(테이블5번_EMPTY, 테이블6번_EMPTY));
        테이블_그룹_삭제_불가 = new TableGroup(1L, Arrays.asList(테이블1번_USING, 테이블6번_EMPTY));

        주문1_결제완료 = new Order(1L, 테이블1번_USING.getId(), OrderStatus.COMPLETION.name(), Arrays.asList(테이블1_행복세트, 테이블1_치쏘세트));
        주문2_식사중 = new Order(2L, 테이블2번_USING.getId(), OrderStatus.MEAL.name(), Arrays.asList(테이블2_행복세트, 테이블2_피맥세트));
    }
}
