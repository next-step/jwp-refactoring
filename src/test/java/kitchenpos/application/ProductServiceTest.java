package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    public void createProduct() {
        //when
        when(productRepository.save(any())).thenReturn(Product.of("메뉴", 1000));
        ProductResponse result = productService.create(ProductRequest.of("메뉴", 1000));
        //then
        assertThat(result).isNotNull();
    }


    @DisplayName("상품 가격이 0보다 작으면 에러.")
    @Test
    public void createProductMinusPrice() {
        //given
        ProductRequest given = ProductRequest.of("메뉴", -1);
        //when
        //then
        assertThatThrownBy(() -> productService.create(given)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 조회한다.")
    @Test
    public void getProducts() {
        //given
        ProductRequest given = ProductRequest.of("메뉴",1000);
        ProductRequest given2 = ProductRequest.of("메뉴2", 2000);
        when(productRepository.findAll()).thenReturn(Arrays.asList(Product.of("메뉴", 1000), Product.of("메뉴2", 2000)));
        //when
        List<ProductResponse> result = productService.list();
        //then
        assertThat(result).hasSize(2);
    }
}
