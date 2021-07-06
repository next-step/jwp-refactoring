package kitchenpos.fixture;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuCreate;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.Menus;
import kitchenpos.domain.menuproduct.MenuAmountCreateValidator;
import kitchenpos.domain.menuproduct.MenuProduct;
import kitchenpos.domain.menuproduct.MenuProductCreate;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.Products;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.ProductFixture.*;

public class MenuFixture {
    public static final Menus EMPTY_MENUS = new Menus(Collections.emptyList());
    private static MenuGroup 치킨 = new MenuGroup(1L, new Name("치킨"));

    public static Menu 양념치킨_콜라_1000원_1개;
    public static List<MenuProduct> 양념치킨_콜라_1000원_1개_MenuProduct;
    public static Menu 양념치킨_콜라_1000원_2개;
    public static List<MenuProduct> 양념치킨_콜라_1000원_2개_MenuProduct;
    public static Menu 후라이드치킨_콜라_2000원_1개;
    public static List<MenuProduct> 후라이드치킨_콜라_2000원_1개_MenuProduct;
    public static Menu 후라이드치킨_콜라_2000원_2개;
    public static List<MenuProduct> 후라이드치킨_콜라_2000원_2개_MenuProduct;

    public static void cleanUp() throws Exception {
        양념치킨_콜라_1000원_1개 = createMenu(1L, new Name("양념치킨_콜라_1000원_1개"), new Price(1000),
                Arrays.asList(
                        new MenuProductCreate(1L, 양념치킨_1000원.getId(), 1),
                        new MenuProductCreate(1L, 콜라_100원.getId(), 1)
                ),
                Arrays.asList(
                        양념치킨_1000원,
                        콜라_100원
                ), 치킨);

        양념치킨_콜라_1000원_2개 = createMenu(2L, new Name("양념치킨_콜라_1000원_2개"), new Price(2000),
                Arrays.asList(
                        new MenuProductCreate(2L, 양념치킨_1000원.getId(), 2),
                        new MenuProductCreate(2L, 콜라_100원.getId(), 2)
                ),
                Arrays.asList(
                        양념치킨_1000원,
                        콜라_100원
                ), 치킨);

        후라이드치킨_콜라_2000원_1개 = createMenu(3L, new Name("후라이드치킨_콜라_2000원_1개"), new Price(2000),
                Arrays.asList(
                        new MenuProductCreate(3L, 후라이드치킨_2000원.getId(), 1),
                        new MenuProductCreate(3L, 콜라_100원.getId(), 1)
                ),
                Arrays.asList(
                        후라이드치킨_2000원,
                        콜라_100원
                ), 치킨);

        후라이드치킨_콜라_2000원_2개 = createMenu(4L, new Name("후라이드치킨_콜라_2000원_2개"), new Price(4000),
                Arrays.asList(
                        new MenuProductCreate(4L, 후라이드치킨_2000원.getId(), 2),
                        new MenuProductCreate(4L, 콜라_100원.getId(), 2)
                ),
                Arrays.asList(
                        후라이드치킨_2000원,
                        콜라_100원
                ), 치킨);


        양념치킨_콜라_1000원_1개_MenuProduct = Arrays.asList(
                new MenuProduct(양념치킨_콜라_1000원_1개, 양념치킨_1000원, new Quantity(1)),
                new MenuProduct(양념치킨_콜라_1000원_1개, 콜라_100원, new Quantity(1))
        );
        양념치킨_콜라_1000원_2개_MenuProduct = Arrays.asList(
                new MenuProduct(양념치킨_콜라_1000원_2개, 양념치킨_1000원, new Quantity(2)),
                new MenuProduct(양념치킨_콜라_1000원_2개, 콜라_100원, new Quantity(2))
        );
        후라이드치킨_콜라_2000원_1개_MenuProduct = Arrays.asList(
                new MenuProduct(후라이드치킨_콜라_2000원_1개, 후라이드치킨_2000원, new Quantity(1)),
                new MenuProduct(후라이드치킨_콜라_2000원_1개, 콜라_100원, new Quantity(1))
        );
        후라이드치킨_콜라_2000원_2개_MenuProduct = Arrays.asList(
                new MenuProduct(후라이드치킨_콜라_2000원_2개, 후라이드치킨_2000원, new Quantity(2)),
                new MenuProduct(후라이드치킨_콜라_2000원_2개, 콜라_100원, new Quantity(2))
        );
    }

    private static Menu createMenu(Long menuId, Name menuName, Price menuPrice,
                                   List<MenuProductCreate> menuProductsCreate, List<Product> products,
                                   MenuGroup group) throws Exception {
        MenuCreate menuCreate = new MenuCreate(menuName, menuPrice, group.getId(), menuProductsCreate);

        return Menu.create(menuId, menuCreate, group, new Products(products), new MenuAmountCreateValidator());
    }
}
