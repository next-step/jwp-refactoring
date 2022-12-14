package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.Money;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.fixture.ProductFixture;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @Test
    @DisplayName("상품목록 등록")
    void testCreateProduct() {
        Product product = new Product("스파게티", Money.wons(100));

        when(productRepository.save(any()))
                .thenReturn(product);

        Product createdProduct = productService.create(product);

        assertThat(product).isEqualTo(createdProduct);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("상품목록 목록")
    void testGetProductList() {
        // given
        List<Product> expectedProducts = Lists.newArrayList(ProductFixture.상품목록(3));
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // when
        List<Product> actualProducts = productService.list();

        // then
        assertThat(actualProducts).containsExactlyInAnyOrderElementsOf(expectedProducts);
        verify(productRepository, times(1)).findAll();
    }


}
