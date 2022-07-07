package kitchenpos.product.application;

import kitchenpos.TestProductRequestFactory;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.ProductValidator;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductValidator productValidator;

    @InjectMocks
    private ProductService productService;

    private ProductRequest 진매;
    private ProductRequest 진순이;

    @BeforeEach
    void setUp() {
        진매 = TestProductRequestFactory.create("진라면 매운맛", 5_000);
        진순이 = TestProductRequestFactory.create("진라면 순한맛", 5_000);
    }

    @Test
    @DisplayName("상품을 등록할 수 있다")
    void create() {
        // given
        doNothing().when(productValidator).validatePrice(any());
        given(productRepository.save(any(Product.class))).willReturn(Product.of(진매));

        //when
        ProductResponse savedProduct = productService.create(진매);

        //then
        assertThat(savedProduct.getName()).isEqualTo(진매.getName());
    }

    @ParameterizedTest
    @DisplayName("상품의 가격이 0원 미만이면 등록할 수 없다")
    @CsvSource(value = {"-1", "null"}, nullValues = {"null"})
    void createException1(BigDecimal price) {
        // given
        진매 = TestProductRequestFactory.create("진라면 이상한 맛", price);

        //when & then
        assertThatThrownBy(() -> productService.create(진매)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 미만으로 설정할 수 없습니다.");
    }

    @Test
    @DisplayName("상품의 목록을 조회할 수 있다")
    void list() {
        // given
        given(productRepository.findAll()).willReturn(Arrays.asList(Product.of(진매), Product.of(진순이)));

        // when
        List<ProductResponse> list = productService.list();

        // then
        Assertions.assertThat(list).hasSize(2);
    }
}
