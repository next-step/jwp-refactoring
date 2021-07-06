package kitchenpos.fixture;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.Menus;
import kitchenpos.domain.menuproduct.MenuProduct;

import java.util.Arrays;
import java.util.Collections;

public class MenuFixture {
    public static final Menus EMPTY_MENUS = new Menus(Collections.emptyList());
    private static ProductFixture productFixture;
    private static MenuGroup 치킨 = new MenuGroup(new Name("치킨"));

    public static Menu 양념치킨_콜라_1000원_1개;
    public static Menu 양념치킨_콜라_1000원_2개;
    public static Menu 후라이드치킨_콜라_2000원_1개;
    public static Menu 후라이드치킨_콜라_2000원_2개;

    public static void cleanUp() {
        양념치킨_콜라_1000원_1개 = new Menu(1L,new Name("양념치킨_콜라_1000원_1개"), new Price(1000), 치킨,
                Arrays.asList(
                        new MenuProduct(productFixture.양념치킨_1000원, new Quantity(1)),
                        new MenuProduct(productFixture.콜라_100원, new Quantity(1))
                )
        );

        양념치킨_콜라_1000원_2개 = new Menu(2L, new Name("양념치킨_콜라_1000원_1개"), new Price(2000), 치킨,
                Arrays.asList(
                        new MenuProduct(productFixture.양념치킨_1000원, new Quantity(2)),
                        new MenuProduct(productFixture.콜라_100원, new Quantity(2))
                )
        );

        후라이드치킨_콜라_2000원_1개 = new Menu(3L, new Name("양념치킨_콜라_1000원_1개"), new Price(2000), 치킨,
                Arrays.asList(
                        new MenuProduct(productFixture.후라이드치킨_2000원, new Quantity(1)),
                        new MenuProduct(productFixture.콜라_100원, new Quantity(1))
                )
        );

        후라이드치킨_콜라_2000원_2개 = new Menu(4L, new Name("양념치킨_콜라_1000원_1개"), new Price(4000), 치킨,
                Arrays.asList(
                        new MenuProduct(productFixture.후라이드치킨_2000원, new Quantity(2)),
                        new MenuProduct(productFixture.콜라_100원, new Quantity(2))
                )
        );
    }
}
