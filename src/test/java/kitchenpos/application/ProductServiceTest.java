package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 서비스에 관련한 기능")
@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductService productService;

    private Product 짬뽕;

    @BeforeEach
    void beforeEach() {
        짬뽕 = new Product();
        짬뽕.setId(1L);
        짬뽕.setName("짬뽕");
        짬뽕.setPrice(new BigDecimal(8_000));
    }

    @DisplayName("`상품`을 생성한다.")
    @Test
    void createProduct() {
        // Given
        String name = "짬뽕";
        BigDecimal price = BigDecimal.valueOf(8_000);
        ProductRequest request = new ProductRequest(name, price);

        // When
        Product actual = productService.create(request);

        // Then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(name),
                () -> assertThat(actual.getPrice().intValue()).isEqualTo(price.intValue())
        );
    }

    @DisplayName("가격은 필수이고, 0원 이상이 아니면 `상품`을 생성할 수 없다.")
    @Test
    void exceptionToCreateProduct() {
        // Given
        ProductRequest invalidRequest1 = new ProductRequest("짬뽕", null);

        // When & Then
        assertThatThrownBy(() -> productService.create(invalidRequest1)).isInstanceOf(IllegalArgumentException.class);

        // Given
        ProductRequest invalidRequest2 = new ProductRequest("짬뽕", BigDecimal.valueOf(-1));

        // When & Then
        assertThatThrownBy(() -> productService.create(invalidRequest2)).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("모든 `상품` 목록을 조회한다.")
    @Test
    void findAllProducts() {
        // Given
        Product 짜장면 = new Product();
        짜장면.setId(2L);
        짜장면.setName("짜장면");
        짜장면.setPrice(new BigDecimal(6_000));
        given(productDao.findAll()).willReturn(Arrays.asList(짬뽕, 짜장면));

        // When
        List<Product> actual = productService.list();

        // Then
        assertAll(
                () -> assertThat(actual).extracting(Product::getId)
                        .containsExactly(짬뽕.getId(), 짜장면.getId()),
                () -> assertThat(actual).extracting(Product::getName)
                        .containsExactly(짬뽕.getName(), 짜장면.getName()),
                () -> assertThat(actual).extracting(Product::getPrice)
                        .containsExactly(짬뽕.getPrice(), 짜장면.getPrice())
        );
    }
}
