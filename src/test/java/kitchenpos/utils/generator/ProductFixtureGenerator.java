package kitchenpos.utils.generator;

import static kitchenpos.ui.ProductRestControllerTest.PRODUCT_API_URL_TEMPLATE;
import static kitchenpos.utils.MockMvcUtil.postRequestBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@TestComponent
public class ProductFixtureGenerator {

    private final ProductDao productDao;

    public ProductFixtureGenerator(ProductDao productDao) {
        this.productDao = productDao;
    }

    private static String NAME = "뽀빠이 닭강정";
    private static BigDecimal PRICE = BigDecimal.valueOf(23000);
    private static int COUNTER = 0;

    public static Product generateProduct() {
        COUNTER++;
        Product product = new Product();
        product.setName(NAME + COUNTER);
        product.setPrice(PRICE);
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

    public static Product generateProductMock() {
        Product product = generateProduct();
        product.setId((long) ProductFixtureGenerator.COUNTER);
        return product;
    }

    public Product savedProduct() {
        return productDao.save(generateProduct());
    }

    public List<Product> savedProducts(int count) {
        List<Product> product = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            product.add(productDao.save(generateProduct()));
        }
        return product;
    }

    public static MockHttpServletRequestBuilder 상품_생성_요청() throws Exception {
        return postRequestBuilder(PRODUCT_API_URL_TEMPLATE, generateProduct());
    }

    public static MockHttpServletRequestBuilder 상품_생성_요청(final String name, final BigDecimal price) throws Exception {
        return postRequestBuilder(PRODUCT_API_URL_TEMPLATE, generateProduct(name, price));
    }
}
