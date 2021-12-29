package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        // Given
        Product productRequest = new Product("강정치킨", BigDecimal.valueOf(17_000));
        given(productDao.save(productRequest)).willReturn(new Product(1L, productRequest.getName(), productRequest.getPrice()));

        // When
        Product productResponse = productService.create(productRequest);

        // Then
        assertAll(
                () -> assertThat(productResponse.getId()).isNotNull(),
                () -> assertThat(productResponse.getName()).isEqualTo(productRequest.getName()),
                () -> assertThat(productResponse.getPrice()).isEqualTo(productRequest.getPrice())
        );
    }

    @DisplayName("상품 가격이 0 미만으로 등록될 경우 에러처리")
    @Test
    void createLessThanZero() {
        // Given
        Product productRequest = new Product("강정치킨", BigDecimal.valueOf(-1));

        // When, Then
        assertThatThrownBy(() -> productService.create(productRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 전체 리스트를 조회한다.")
    @Test
    void findAll() {
        // Given
        Product chicken = new Product(1L, "치킨", BigDecimal.valueOf(20000));
        Product pizza = new Product(2L, "피자", BigDecimal.valueOf(25000));
        given(productDao.findAll()).willReturn(Arrays.asList(chicken, pizza));

        // When
        List<Product> products = productService.list();

        // Then
        assertAll(
                () -> assertThat(products.size()).isEqualTo(2),
                () -> assertThat(products).containsExactly(chicken, pizza)
        );
    }
}