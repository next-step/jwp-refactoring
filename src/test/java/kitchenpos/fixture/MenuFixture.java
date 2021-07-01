package kitchenpos.fixture;

import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menuproduct.MenuProduct;

import java.util.Arrays;

public class MenuFixture {
    private static ProductFixture productFixture;
    private static MenuGroup 치킨 = new MenuGroup("치킨");

    public static Menu 양념치킨_콜라_1000원_1개;
    public static Menu 양념치킨_콜라_1000원_2개;
    public static Menu 후라이드치킨_콜라_2000원_1개;
    public static Menu 후라이드치킨_콜라_2000원_2개;

    public static void cleanUp() {
        양념치킨_콜라_1000원_1개 = new Menu(1L,"양념치킨_콜라_1000원_1개", new Price(1000), 치킨,
                Arrays.asList(
                        new MenuProduct(productFixture.양념치킨_1000원, 1),
                        new MenuProduct(productFixture.콜라_100원, 1)
                )
        );

        양념치킨_콜라_1000원_2개 = new Menu(2L, "양념치킨_콜라_1000원_1개", new Price(2000), 치킨,
                Arrays.asList(
                        new MenuProduct(productFixture.양념치킨_1000원, 2),
                        new MenuProduct(productFixture.콜라_100원, 2)
                )
        );

        후라이드치킨_콜라_2000원_1개 = new Menu(3L, "양념치킨_콜라_1000원_1개", new Price(2000), 치킨,
                Arrays.asList(
                        new MenuProduct(productFixture.후라이드치킨_2000원, 1),
                        new MenuProduct(productFixture.콜라_100원, 1)
                )
        );

        후라이드치킨_콜라_2000원_2개 = new Menu(4L, "양념치킨_콜라_1000원_1개", new Price(4000), 치킨,
                Arrays.asList(
                        new MenuProduct(productFixture.후라이드치킨_2000원, 2),
                        new MenuProduct(productFixture.콜라_100원, 2)
                )
        );
    }
}
