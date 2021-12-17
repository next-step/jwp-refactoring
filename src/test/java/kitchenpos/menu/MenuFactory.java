package kitchenpos.menu;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MenuFactory {
  public static MenuGroup ofMenuGroup(Long id, String name) {
    MenuGroup menuGroup = new MenuGroup();
    menuGroup.setId(id);
    menuGroup.setName(name);

    return menuGroup;
  }

  public static MenuGroup ofMenuGroup(String name) {
    return ofMenuGroup(null, name);
  }

  public static Menu ofMenu(Long id, String name, Long menuGroupId, int price) {
    Menu menu = new Menu();
    menu.setId(id);
    menu.setName(name);
    menu.setMenuGroupId(menuGroupId);
    menu.setPrice(BigDecimal.valueOf(price));

    return menu;
  }

  public static Product ofProduct(Long id, String name, int price) {
    Product product = new Product();
    product.setId(id);
    product.setName(name);
    product.setPrice(BigDecimal.valueOf(price));

    return product;
  }

  public static MenuProduct ofMenuProduct(Long seq, Long menuId, Long productId, int quantity) {
    MenuProduct menuProduct = new MenuProduct();
    menuProduct.setSeq(seq);
    menuProduct.setMenuId(menuId);
    menuProduct.setProductId(productId);
    menuProduct.setQuantity(quantity);

    return menuProduct;
  }

  public static List<MenuProduct> ofMenuProductList(List<MenuProduct> list) {
    return new ArrayList<>(list);
  }
}
