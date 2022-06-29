package kitchenpos.utils.generator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Product;

public class ProductFixtureGenerator {

    private static String NAME = "뽀빠이 닭강정";
    private static BigDecimal PRICE = BigDecimal.valueOf(23000);
    private static int COUNTER = 0;

    public static Product generateProduct() {
        COUNTER++;
        Product product = new Product();
        product.setName(NAME + COUNTER);
        product.setPrice(PRICE.add(BigDecimal.valueOf(COUNTER)));
        return product;
    }

    public static Product generateProduct(final String name, final BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static List<Product> generateProducts(int count) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            products.add(generateProduct());
        }
        return products;
    }
}
