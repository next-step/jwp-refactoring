package kitchenpos.product;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @DisplayName("상품을 등록한다.")
    @Test
    void registerProduct() {

        //given
        ProductRequest productRequest = new ProductRequest("후라이드", new BigDecimal("16000"));
        Product product = productRequest.toEntity();
        when(productRepository.save(any())).thenReturn(product);

        //when
        ProductResponse savedProduct = productService.create(productRequest);

        //then
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
    }

    @DisplayName("상품 리스트를 조회한다.")
    @Test
    void getProducts() {

        //given
        List<Product> products = new ArrayList<>();
        Product 후라이드 = Product.create("후라이드", new BigDecimal("16000"));
        ReflectionTestUtils.setField(후라이드, "id", 1L);

        Product 치즈버거 = Product.create("치즈버거", new BigDecimal("8000"));
        ReflectionTestUtils.setField(치즈버거, "id", 2L);

        products.add(후라이드);
        products.add(치즈버거);

        when(productRepository.findAll()).thenReturn(products);

        //when
        List<ProductResponse> findProducts = productService.list();

        //then
        Assertions.assertThat(findProducts).isNotEmpty();
        assertThat(findProducts.size()).isEqualTo(products.size());
        Assertions.assertThat(findProducts).extracting(ProductResponse::getName).containsExactly("후라이드", "치즈버거");
    }


}
