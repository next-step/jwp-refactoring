package kitchenpos.product.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Products {

    List<Product> products = new ArrayList<>();

    public Products() {}

    public Stream<Product> stream() {
        return products.stream();
    }
}
