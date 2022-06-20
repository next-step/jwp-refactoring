package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품을 생성할 수 있다.")
    void create() {
        //given
        ProductRequest request = new ProductRequest("product", BigDecimal.TEN);
        given(productDao.save(any())).willReturn(request.toProduct());

        //when
        ProductResponse result = productService.create(request);

        //then
        assertThat(result.getName()).isEqualTo("product");
        assertThat(result.getPrice()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    @DisplayName("상품 가격이 NULL 이거나 음수면 생성할 수 없다.")
    void create_fail() {
        //given
        ProductRequest request = new ProductRequest("product", null);

        //then
        assertThatThrownBy(() -> productService.create(request)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 상품을 조회할 수 있다.")
    void list() {
        //given
        Product product1 = new Product("product1", BigDecimal.TEN);
        Product product2 = new Product("product2", BigDecimal.ZERO);
        given(productDao.findAll()).willReturn(Arrays.asList(product1, product2));

        //when
        List<ProductResponse> products = productService.list();

        //then
        assertThat(products.stream().map(product -> product.getName()).collect(Collectors.toList()))
                .containsExactlyInAnyOrder("product1", "product2");
    }
}
