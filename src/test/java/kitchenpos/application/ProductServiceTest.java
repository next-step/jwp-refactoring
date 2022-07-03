package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    Product 스낵랩;

    @BeforeEach
    void setUp() {
        스낵랩 = new Product(1L, "스낵랩", BigDecimal.valueOf(3000));
    }

    @Test
    @DisplayName("상품 정상 등록")
    public void saveSuccess() {
        given(productRepository.save(스낵랩)).willReturn(스낵랩);

        assertThat(productService.create(스낵랩).getId()).isEqualTo(스낵랩.getId());
    }

    @Test
    public void list() {
        Product 맥모닝 = new Product(1L, "맥모닝", BigDecimal.valueOf(4000));
        given(productRepository.findAll()).willReturn(Arrays.asList(스낵랩, 맥모닝));

        assertThat(productService.list()).contains(스낵랩, 맥모닝);
    }
}