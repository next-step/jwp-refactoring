package kitchenpos.utils.generator;

import static kitchenpos.ui.ProductRestControllerTest.PRODUCT_API_URL_TEMPLATE;
import static kitchenpos.utils.MockMvcUtil.postRequestBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.product.CreateProductRequest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@TestComponent
public class ProductFixtureGenerator {

    private static String NAME = "뽀빠이 닭강정";
    private static BigDecimal PRICE = BigDecimal.valueOf(23_000);
    private static int COUNTER = 0;

    public static Product 상품_생성() {
        COUNTER++;
        return Product.of(NAME + COUNTER, PRICE);
    }

    public static Product 상품_생성(final String name, final int price) {
        return Product.of(name, new BigDecimal(price));
    }

    public static CreateProductRequest 상품_생성_요청_객체(final String name, final int price) {
        return new CreateProductRequest(name, new BigDecimal(price));
    }

    public static List<Product> 상품_목록_생성(int count) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            products.add(상품_생성());
        }
        return products;
    }

    public static MockHttpServletRequestBuilder 상품_생성_요청() throws Exception {
        return postRequestBuilder(PRODUCT_API_URL_TEMPLATE, 상품_생성());
    }

    public static MockHttpServletRequestBuilder 상품_생성_요청(final String name, final int price) throws Exception {
        return postRequestBuilder(PRODUCT_API_URL_TEMPLATE, 상품_생성(name, price));
    }
}
