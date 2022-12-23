package kitchenpos.unit.product;

import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.port.ProductPort;
import kitchenpos.product.domain.Product;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductPort productPort;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품을 등록 할 수 있다")
    void createProduct() {
        Product 스테이크 = new Product(new ProductPrice(BigDecimal.valueOf(10_000)), "스테이크");

        given(productPort.save(any())).willReturn(스테이크);

        ProductResponse result = productService.create(new ProductRequest("스테이크", BigDecimal.valueOf(10_000)));

        assertThat(result.getId()).isEqualTo(스테이크.getId());
        assertThat(result.getPrice()).isEqualTo(스테이크.getPrice());
        assertThat(result.getName()).isEqualTo(스테이크.getName());
    }

    @Test
    @DisplayName("상품의 가격은 0원 이상이여야한다")
    void createProductPriceZero() {
        assertThatThrownBy(() ->
                productService.create(new ProductRequest("스테이크", BigDecimal.valueOf(-1)))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 리스트를 받을 수 있다")
    void getProductList() {
        Product 스테이크 = new Product(new ProductPrice(BigDecimal.valueOf(200)), "스테이크");
        Product 감자튀김 = new Product(new ProductPrice(BigDecimal.valueOf(200)), "감자튀김");

        given(productPort.findAll()).willReturn(Arrays.asList(스테이크, 감자튀김));

        List<Product> result = productService.list();

        assertThat(result).hasSize(2);
    }
}
