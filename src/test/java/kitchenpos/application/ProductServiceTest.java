package kitchenpos.application;

import static kitchenpos.domain.ProductTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("제품 서비스")
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Test
    @DisplayName("제품을 생성한다")
    void create() {
        // given
        Product 깐풍치킨 = new Product(100L, "깐풍치킨", Price.valueOf(17000));
        when(productRepository.save(깐풍치킨)).thenReturn(깐풍치킨);

        // when
        Product savedProduct = productService.create(깐풍치킨);

        // then
        assertThat(savedProduct.getId()).isEqualTo(깐풍치킨.getId());
    }

    @Test
    @DisplayName("제품 목록을 가져온다")
    void list() {
        // given
        List<Product> products = Arrays.asList(후라이드, 양념치킨, 반반치킨);
        when(productRepository.findAll()).thenReturn(products);

        // when
        List<Product> allProducts = productService.list();
        assertThat(allProducts.size()).isEqualTo(3);
        assertThat(allProducts).containsExactly(후라이드, 양념치킨, 반반치킨);
    }
}
