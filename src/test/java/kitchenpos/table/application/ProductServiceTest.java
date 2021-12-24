package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("이름과 가격으로 상품을 생성할 수 있다")
    @Test
    void 상품_생성() {
        // given
        Product product = Product.of("양념치킨", 17000);
        Product savedProduct = new Product(1L, "양념치킨", 17000);

        given(productRepository.save(any()))
            .willReturn(savedProduct);

        // when
        ProductResponse result = productService.create(product);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("양념치킨");
        assertThat(result.getPrice()).isEqualTo(17000);
    }

    @DisplayName("가격이 음수인 경우 예외 발생")
    @Test
    void 상품_생성_예외1() {
        // given
        Product product = Product.of("양념치킨", -1000);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> productService.create(product)
        );
    }

    @DisplayName("상품 목록 조회")
    @Test
    void 상품_목록_조회() {
        // given
        Product savedProduct1 = new Product(1L, "양념치킨", 17000);
        Product savedProduct2 = new Product(2L, "후라이드치킨", 16000);
        Product savedProduct3 = new Product(3L, "간장치킨", 17000);

        given(productRepository.findAll())
            .willReturn(Lists.newArrayList(savedProduct1, savedProduct2, savedProduct3));

        // when
        List<ProductResponse> result = productService.list();

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("양념치킨");
        assertThat(result.get(0).getPrice()).isEqualTo(17000);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getName()).isEqualTo("후라이드치킨");
        assertThat(result.get(1).getPrice()).isEqualTo(16000);
        assertThat(result.get(2).getId()).isEqualTo(3L);
        assertThat(result.get(2).getName()).isEqualTo("간장치킨");
        assertThat(result.get(2).getPrice()).isEqualTo(17000);
    }
}