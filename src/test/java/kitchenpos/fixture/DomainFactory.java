package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

public class DomainFactory {
    public static Product createProduct(Long id, String name, long price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }

    public static Product createEmptyPriceProduct(Long id, String name) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);

        return product;
    }

    public static Menu createMenu(Long id, String name, long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    public static Menu createEmptyPriceMenu(Long id, String name, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(null);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    public static MenuProduct createMenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    public static MenuGroup createMenuGroup(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);

        return menuGroup;
    }

    public static OrderTable createOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }
}
