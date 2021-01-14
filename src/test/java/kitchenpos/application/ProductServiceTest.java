package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 서비스에 관련한 기능")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    private Product 짬뽕;

    @BeforeEach
    void beforeEach() {
        짬뽕 = new Product();
        짬뽕.setId(1L);
        짬뽕.setName("짬뽕");
        짬뽕.setPrice(new BigDecimal(8_000));
    }

    @DisplayName("상품 생성")
    @Test
    void createProduct() {
        // Given
        given(productDao.save(짬뽕)).willReturn(짬뽕);
        // When
        Product actual = productService.create(짬뽕);
        // Then
        assertAll(
                () -> assertEquals(짬뽕.getId(), actual.getId()),
                () -> assertEquals(짬뽕.getName(), actual.getName()),
                () -> assertEquals(짬뽕.getPrice(), actual.getPrice())
        );
    }

    @DisplayName("예외 - 잘못된 가격의 상품 생성")
    @Test
    void exceptionToCreateProduct() {
        // Given
        짬뽕.setPrice(null);
        // When & Then
        assertThatThrownBy(() -> productService.create(짬뽕)).isInstanceOf(IllegalArgumentException.class);
        // Given
        짬뽕.setPrice(new BigDecimal(-1));
        // When & Then
        assertThatThrownBy(() -> productService.create(짬뽕)).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("모든 상품 목록 조회")
    @Test
    void findAllProducts() {
        // Given
        Product 짜장면 = new Product();
        짜장면.setId(1L);
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
