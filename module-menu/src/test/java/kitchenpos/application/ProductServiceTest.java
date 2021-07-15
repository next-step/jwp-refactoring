package kitchenpos.application;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    ProductRequest 후라이드치킨요청;
    Product 후라이드치킨;
    Product 양념치킨;

    ProductService productService;

    @BeforeEach
    void setUp() {
        후라이드치킨요청 = new ProductRequest("후라이드치킨", BigDecimal.valueOf(18000L));
        후라이드치킨 = new Product(1L, "후라이드치킨", BigDecimal.valueOf(18000L));
        양념치킨 = new Product(2L, "양념치킨", BigDecimal.valueOf(19000L));

        productService = new ProductService(productRepository);
    }

    @DisplayName("상품을 등록")
    @Test
    void 상품을_등록() {
        //given
        when(productRepository.save(any())).thenReturn(후라이드치킨);

        //when
        ProductResponse savedProduct = productService.create(후라이드치킨요청);

        //then
        assertThat(savedProduct.getId()).isEqualTo(후라이드치킨.getId());
    }

    @DisplayName("상품_목록을_불러옴")
    @Test
    void 상품_목록을_부러옴() {
        //given
        when(productRepository.findAll()).thenReturn(Arrays.asList(후라이드치킨, 양념치킨));

        //when
        List<ProductResponse> list = productService.list();

        //then
        assertThat(list).hasSize(2);
        assertThat(list.stream().map(ProductResponse::getName))
                .containsExactly(후라이드치킨.getName(), 양념치킨.getName());
    }
}
