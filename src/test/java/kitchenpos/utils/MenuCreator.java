package kitchenpos.utils;

import kitchenpos.common.domian.Price;
import kitchenpos.common.domian.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Products;
import kitchenpos.menu.domain.ProductsQuantities;
import kitchenpos.menu.domain.Quantities;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuCreator {
    private MenuCreator() {}

    public static Menu of(String menuGroupName, String productName, int productPrice, Long quantity, String menuName, int requestAmount) {
        Long productId = 1L;

        MenuGroup menuGroup = new MenuGroup(menuGroupName);
        Product product = new Product(productId, productName, new Price(productPrice));

        List<Product> products = Arrays.asList(product);
        Map<Long, Quantity> quantities = new HashMap<Long, Quantity>() {{ put(productId, new Quantity(quantity)); }};

        Price requestPrice = new Price(BigDecimal.valueOf(requestAmount * quantity));
        ProductsQuantities productsQuantities = new ProductsQuantities(new Products(products, products.size()), new Quantities(quantities, quantities.size()), requestPrice);

        return Menu.of(menuGroup, menuName, productsQuantities);
    }
}
