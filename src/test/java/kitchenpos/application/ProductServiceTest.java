package kitchenpos.application;

import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.application.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    void 상품의_가격이_올바르지_않으면_등록할_수_없다() {
        // when & then
        assertThatThrownBy(() ->
                productService.create(new Product("치킨", BigDecimal.valueOf(-1)))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    void 상품을_등록한다() {
        // given
        Product product = new Product("치킨", BigDecimal.valueOf(10000));
        given(productRepository.save(product)).willReturn(createProduct());

        // when
        Product result = productService.create(product);

        // then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void 상품_목록을_조회한다() {
        // given
        given(productRepository.findAll()).willReturn(createProducts());

        // when
        List<Product> result = productService.list();

        // then
        assertThat(result).hasSize(2);
    }

    private Product createProduct() {
        return new Product(1L, "치킨", BigDecimal.valueOf(10000));
    }

    private Product createProduct2() {
        return new Product(2L, "피자", BigDecimal.valueOf(15000));
    }

    private List<Product> createProducts() {
        return Arrays.asList(createProduct(), createProduct2());
    }
}
