package kitchenpos.menu.application;

import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void createProduct() {
        // given
        ProductRequest 생성할_상품_요청 = new ProductRequest("짬뽕", BigDecimal.valueOf(9000));
        Product 생성할_상품 = new Product(1L, 생성할_상품_요청.getName(), 생성할_상품_요청.getPrice());
        given(productRepository.save(any()))
                .willReturn(생성할_상품);

        // when
        ProductResponse 생성된_상품_응답 = productService.create(생성할_상품_요청);

        // then
        상품_생성_성공(생성된_상품_응답, 생성할_상품_요청);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void listProduct() {
        // given
        List<Product> 조회할_상품_목록 = Arrays.asList(
                new Product(1L, "짬뽕", BigDecimal.valueOf(9000)),
                new Product(2L, "볶음밥", BigDecimal.valueOf(8000)),
                new Product(3L, "탕수육", BigDecimal.valueOf(7000))
        );
        given(productRepository.findAll())
                .willReturn(조회할_상품_목록);

        // when
        List<ProductResponse> 조회된_상품_목록_응답 = productService.list();

        // then
        상품_목록_조회_성공(조회된_상품_목록_응답, 조회할_상품_목록);
    }

    private void 상품_생성_성공(ProductResponse actual, ProductRequest expected) {
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice())
        );
    }

    private void 상품_목록_조회_성공(List<ProductResponse> 조회된_상품_목록, List<Product> 조회할_상품_목록) {
        assertAll(
                () -> assertThat(조회된_상품_목록).hasSize(조회할_상품_목록.size()),
                () -> assertThat(조회된_상품_목록.get(0).getId()).isEqualTo(조회할_상품_목록.get(0).getId()),
                () -> assertThat(조회된_상품_목록.get(0).getName()).isEqualTo(조회할_상품_목록.get(0).getName()),
                () -> assertThat(조회된_상품_목록.get(0).getPrice()).isEqualTo(조회할_상품_목록.get(0).getPrice()),
                () -> assertThat(조회된_상품_목록.get(1).getId()).isEqualTo(조회할_상품_목록.get(1).getId()),
                () -> assertThat(조회된_상품_목록.get(1).getName()).isEqualTo(조회할_상품_목록.get(1).getName()),
                () -> assertThat(조회된_상품_목록.get(1).getPrice()).isEqualTo(조회할_상품_목록.get(1).getPrice())
        );
    }
}
