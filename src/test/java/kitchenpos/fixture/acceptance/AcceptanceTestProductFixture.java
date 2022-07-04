package kitchenpos.fixture.acceptance;

import java.math.BigDecimal;
import kitchenpos.product.acceptance.ProductAcceptanceTest;
import kitchenpos.product.domain.Product;

public class AcceptanceTestProductFixture {
    public final Product 삼겹살;
    public final Product 목살;
    public final Product 김치찌개;
    public final Product 공깃밥;

    public AcceptanceTestProductFixture() {
        this.삼겹살 = ProductAcceptanceTest.상품_생성_요청("삼겹살", BigDecimal.valueOf(14000L)).as(Product.class);
        this.목살 = ProductAcceptanceTest.상품_생성_요청("목살", BigDecimal.valueOf(15000L)).as(Product.class);
        this.김치찌개 = ProductAcceptanceTest.상품_생성_요청("김치찌개", BigDecimal.valueOf(8000L)).as(Product.class);
        this.공깃밥 = ProductAcceptanceTest.상품_생성_요청("공깃밥", BigDecimal.valueOf(1000L)).as(Product.class);
    }
}
