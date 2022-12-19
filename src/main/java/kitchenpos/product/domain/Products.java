package kitchenpos.product.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Products {

    List<Product> products = new ArrayList<>();

    public Products() {}

    public Products(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Stream<Product> stream() {
        return products.stream();
    }
}
