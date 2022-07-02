package kitchenpos.service.product;

import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.service.product.application.ProductService;
import kitchenpos.service.product.dto.ProductRequest;
import kitchenpos.service.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품을 생성할 수 있다.")
    void create() {
        //given
        ProductRequest request = new ProductRequest("product", 0);
        given(productRepository.save(any())).willReturn(request.toProduct());

        //when
        ProductResponse result = productService.create(request);

        //then
        assertThat(result.getName()).isEqualTo("product");
        assertThat(result.getPrice()).isEqualTo(0);
    }

    @Test
    @DisplayName("전체 상품을 조회할 수 있다.")
    void list() {
        //given
        Product product1 = new Product("product1", 100);
        Product product2 = new Product("product2", 0);
        given(productRepository.findAll()).willReturn(Arrays.asList(product1, product2));

        //when
        List<ProductResponse> products = productService.list();

        //then
        assertThat(products.stream().map(ProductResponse::getName).collect(Collectors.toList()))
                .containsExactlyInAnyOrder("product1", "product2");
    }
}
