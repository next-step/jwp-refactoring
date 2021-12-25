package kitchenpos.product;

import kitchenpos.product.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
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
    private ProductDao productDao;

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
        Product 후라이드 = new Product();
        ReflectionTestUtils.setField(후라이드, "id", 1L);
        ReflectionTestUtils.setField(후라이드, "name", "후라이드");
        ReflectionTestUtils.setField(후라이드, "productPrice", new ProductPrice("16000"));

        Product 치즈버거 = new Product();
        ReflectionTestUtils.setField(치즈버거, "id", 2L);
        ReflectionTestUtils.setField(치즈버거, "name", "치즈버거");
        ReflectionTestUtils.setField(치즈버거, "productPrice", new ProductPrice("8000"));

        products.add(후라이드);
        products.add(치즈버거);

        when(productRepository.findAll()).thenReturn(products);

        //when
        List<ProductResponse> findProducts = productService.list();

        //then
        assertThat(findProducts).isNotEmpty();
        assertThat(findProducts.size()).isEqualTo(products.size());
        assertThat(findProducts).extracting(ProductResponse::getName).containsExactly("후라이드", "치즈버거");
    }


}
