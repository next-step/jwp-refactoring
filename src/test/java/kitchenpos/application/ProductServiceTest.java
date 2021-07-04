package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 서비스")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        // Given
        Product 피자 = new Product(1L, "피자", new BigDecimal(20000));
        given(productDao.save(any())).willReturn(피자);

        // When
        productService.create(피자);

        // Then
        verify(productDao, times(1)).save(any());
    }

    @DisplayName("가격은 필수입력항목이다.")
    @Test
    void create_Fail_01() {
        // Given
        Product 햄버거 = new Product(1L, "햄버거", null);

        // When & Then
        assertThatThrownBy(() -> productService.create(햄버거))
            .isInstanceOf(IllegalArgumentException.class);
        verify(productDao, never()).save(any());
    }

    @DisplayName("가격은 0원 이상이어야 한다.")
    @Test
    void create_Fail_02() {
        // Given
        Product 햄버거 = new Product(1L, "햄버거", new BigDecimal(-1));

        // When & Then
        assertThatThrownBy(() -> productService.create(햄버거))
            .isInstanceOf(IllegalArgumentException.class);
        verify(productDao, never()).save(any());
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        // Given
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "피자", new BigDecimal(20000)));
        given(productDao.findAll()).willReturn(products);

        // When & Then
        assertThat(productService.list()).hasSize(1);
        verify(productDao, times(1)).findAll();
    }

}
