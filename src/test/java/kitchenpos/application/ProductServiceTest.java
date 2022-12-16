package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductResponse;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    private Product 후라이드;
    private Product 양념;

    @BeforeEach
    void setUp() {
        후라이드 = new Product("후라이드", BigDecimal.valueOf(16000));
        양념 = new Product("양념", BigDecimal.valueOf(17000));
    }

    @Test
    void 생성() {
        given(productRepository.save(any())).willReturn(후라이드);

        ProductResponse response = productService.create(후라이드);

        assertAll(
                () -> assertThat(response.getName()).isEqualTo("후라이드"),
                () -> assertThat(response.getPrice()).isEqualTo(BigDecimal.valueOf(16000))
        );
    }

    @Test
    void 조회() {
        given(productRepository.findAll()).willReturn(Arrays.asList(후라이드, 양념));

        List<ProductResponse> responses = productService.list();

        assertThat(responses.size()).isEqualTo(2);
    }
}
