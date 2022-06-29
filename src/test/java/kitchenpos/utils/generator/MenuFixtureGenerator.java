package kitchenpos.utils.generator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuFixtureGenerator {

    private static String NAME = "맵단 스페샬";
    private static BigDecimal PRICE = BigDecimal.valueOf(33000);
    private static int COUNTER = 0;

    public static Menu generateMenu(
        final MenuGroup menuGroup,
        final List<Product> products
    ) {
        Menu menu = new Menu();
        menu.setName(NAME + COUNTER);
        menu.setPrice(PRICE);
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(generateMenuProduct(products));
        return menu;
    }

    private static List<MenuProduct> generateMenuProduct(List<Product> products) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        int lastMenuGroupSeq = 7;
        for (Product product : products) {
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(product.getId());
            menuProduct.setQuantity(1);
            lastMenuGroupSeq++;
            menuProduct.setSeq(Long.valueOf(lastMenuGroupSeq));

            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }
}
