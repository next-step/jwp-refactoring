package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @Test
    @DisplayName("상품을 등록한다.")
    void create() {
        // given
        String name = "후라이드";
        BigDecimal price = new BigDecimal(16_000);
        ProductCreateRequest request = new ProductCreateRequest(name, price);
        Product product = Product.of(name, price);
        given(productDao.save(any())).willReturn(product);

        // when
        ProductResponse result = productService.create(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(product.getName());
        assertThat(result.getPrice()).isEqualTo(product.getPriceValue());
    }

    @Test
    @DisplayName("상품 가격이 없으면 상품을 등록할 수 없다.")
    void create_null() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("후라이드", null);

        // when, then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("상품 가격이 0원 이상이 아니면 상품을 등록할 수 없다.")
    void create_negative_price() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("후라이드", new BigDecimal(-1));

        // when, then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void list() {
        // given
        List<Product> products = new ArrayList<>(Arrays.asList(new Product("후라이드", new BigDecimal(16_000)),
                new Product("양념치킨", new BigDecimal(16_000))));
        given(productDao.findAll()).willReturn(products);

        // when
        List<ProductResponse> result = productService.list();

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(products.size());
    }
}
