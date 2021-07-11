package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.exception.IllegalProductPriceException;
import org.junit.jupiter.api.DisplayName;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        Product product = new Product(1L, "신상품", BigDecimal.valueOf(15000));
        ProductRequest request = new ProductRequest("신상품", 15000L);
        given(productRepository.save(any())).willReturn(product);

        ProductResponse created = productService.create(request);

        assertAll(
                () -> assertThat(created.getName()).isEqualTo(product.getName()),
                () -> assertThat(created.getPrice()).isEqualTo(product.getPrice()));

        verify(productRepository, times(1)).save(any());
    }

    @DisplayName("상품을 등록에 실패한다. - 상품 등록시 가격값이 null 이거나 0보다 작으면 등록 실패한다.")
    @Test
    void fail_create() {
        ProductRequest product1 = new ProductRequest("신상품", -1L);
        ProductRequest product2 = new ProductRequest("신상품", null);

        assertThatThrownBy(() -> productService.create(product1))
                .isInstanceOf(IllegalProductPriceException.class);
        assertThatThrownBy(() -> productService.create(product2))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("상품 리스트를 조회한다.")
    @Test
    void list() {
        Product product1 = new Product(1L, "신상품1", BigDecimal.valueOf(15000));
        Product product2 = new Product(2L, "신상품2", BigDecimal.valueOf(17000));
        List<Product> products = Arrays.asList(product1, product2);
        given(productRepository.findAll()).willReturn(products);

        List<ProductResponse> findProducts = productService.list();

        assertAll(
                () -> assertThat(findProducts.get(0).getName()).isEqualTo(product1.getName()),
                () -> assertThat(findProducts.get(1).getName()).isEqualTo(product2.getName()));

        verify(productRepository, times(1)).findAll();
    }
}
