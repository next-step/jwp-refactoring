package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @DisplayName("상품을 등록한다.")
    @Test
    void createTest(){

        Product product = new Product("후라이드치킨", BigDecimal.valueOf(18000));

        Product expectedProduct = mock(Product.class);
        when(expectedProduct.getId()).thenReturn(1L);
        when(expectedProduct.getName()).thenReturn("후라이드치킨");

        when(productRepository.save(product)).thenReturn(expectedProduct);

        ProductService productService = new ProductService(productRepository);
        Product savedProduct = productService.create(ProductRequest.from(product));

        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(product.getName());

    }

    @DisplayName("상품의 목록을 조회한다.")
    @Test
    void list() {
        // given
        Product expectedProduct = mock(Product.class);
        when(productRepository.findAll()).thenReturn(Arrays.asList(expectedProduct));
        ProductService productService = new ProductService(productRepository);

        //when
        List<Product> products = productService.list();

        //then
        assertThat(products).contains(expectedProduct);
    }

}
