package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.product.ProductService;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.ProductDto;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품이 저장된다.")
    @Test
    void create_product() {
        // given
        Product 뿌링클치킨 = Product.of("뿌링클치킨", Price.of(15_000));
        when(productRepository.save(any(Product.class))).thenReturn(뿌링클치킨);

        // when
        ProductDto createdProduct = productService.create(ProductDto.of("뿌링클치킨", BigDecimal.valueOf(15_000)));

        // then
        Assertions.assertThat(createdProduct).isEqualTo(ProductDto.of("뿌링클치킨", BigDecimal.valueOf(15_000)));
    }

    @DisplayName("상품이 조회된다.")
    @Test
    void search_product() {
        // given
        Product 뿌링클치킨 = Product.of("뿌링클치킨", Price.of(15_000));
        Product 치킨무 = Product.of("치킨무", Price.of(1_000));
        Product 코카콜라 = Product.of("코카콜라", Price.of(3_000));

        when(productRepository.findAll()).thenReturn(List.of(뿌링클치킨, 치킨무, 코카콜라));

        // when
        List<ProductDto> searchedProducts = productService.list();

        // then
        Assertions.assertThat(searchedProducts).hasSize(3)
                                                .contains(ProductDto.of("뿌링클치킨", BigDecimal.valueOf(15_000)),
                                                            ProductDto.of("치킨무", BigDecimal.valueOf(1_000)),
                                                            ProductDto.of("코카콜라", BigDecimal.valueOf(3_000)));
    }
}
