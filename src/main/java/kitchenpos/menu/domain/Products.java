package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.common.domian.Price;
import kitchenpos.common.error.InvalidRequestException;
import kitchenpos.product.domain.Product;

public class Products {
    private final List<Product> products;

    public Products(List<Product> products, int requestSize) {
        checkRequestSize(products, requestSize);
        this.products = Collections.unmodifiableList(new ArrayList<>(products));
    }

    private void checkRequestSize(List<Product> products, int requestSize) {
        if (products.size() != requestSize) {
            throw new InvalidRequestException();
        }
    }

    public Price totalPrice(Quantities quantities) {
        return new Price(products.stream()
                .map(product -> {
                    BigDecimal price = product.getPriceAmount();
                    Long quantity = quantities.getQuantity(product.getId());
                    return price.multiply(BigDecimal.valueOf(quantity));
                })
                .collect(Collectors.toList())
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    public List<MenuProduct> toMenuProducts(Menu menu, Quantities quantities) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (Product product : products) {
            menuProducts.add(MenuProduct.of(menu, product, quantities.getByProductId(product.getId())));
        }
        return menuProducts;
    }
}
