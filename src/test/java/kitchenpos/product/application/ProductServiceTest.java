package kitchenpos.product.application;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static kitchenpos.product.ProductGenerator.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품 생성 시 0원 미만이면 예외가 발생해야 한다")
    @Test
    void createProductByMinusAmountTest() {
        // given
        ProductCreateRequest 상품_생성_요청 = 상품_생성_요청("상품", -1);

        // then
        상품_생성_실패됨(() -> productService.create(상품_생성_요청));
    }

    @DisplayName("상품 생성 시 정상 생성되어야 한다")
    @Test
    void createProductTest() {
        // given
        ProductCreateRequest 상품_생성_요청 = 상품_생성_요청("상품", 1_000);

        // when
        Product 상품_생성_결과 = productService.create(상품_생성_요청);

        // then
        상품_생성_성공됨(상품_생성_결과, 상품_생성_요청);
    }

    @DisplayName("상품 리스트 조회 시 정상 조회되어야 한다")
    @Test
    void findProductsTest() {
        // given
        List<Long> 포함_되어야_할_아이디들 = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            포함_되어야_할_아이디들.add(
                    productService.create(상품_생성_요청("상품", 1_000)).getId()
            );
        }

        // when
        List<Product> 상품_조회_결과 = productService.list();

        // then
        상품_목록_정상_조회됨(상품_조회_결과, 포함_되어야_할_아이디들);
    }

    void 상품_생성_실패됨(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }

    void 상품_생성_성공됨(Product product, ProductCreateRequest request) {
        assertThat(product.getName()).isEqualTo(request.getName());
        assertThat(product.getPrice()).isEqualTo(new Price(request.getPrice()));
    }

    void 상품_목록_정상_조회됨(List<Product> products, List<Long> containIds) {
        assertThat(products.size()).isGreaterThanOrEqualTo(containIds.size());
        assertThat(products.stream().mapToLong(Product::getId)).containsAll(containIds);
    }
}
