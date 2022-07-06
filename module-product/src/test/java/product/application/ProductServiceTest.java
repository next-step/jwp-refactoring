package product.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static product.fixture.ProductFixture.상품_데이터_생성;
import static product.fixture.ProductFixture.상품_요청_데이터_생성;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import product.dto.ProductRequestDto;
import product.dto.ProductResponseDto;
import product.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        //given
        String name = "product";
        BigDecimal price = BigDecimal.valueOf(1000);
        ProductRequestDto request = 상품_요청_데이터_생성(name, price);

        Long id = 1L;
        given(productRepository.save(any())).willReturn(상품_데이터_생성(id, name, price));

        //when
        ProductResponseDto response = productService.create(request);

        //then
        상품_데이터_확인(response, id, name, price);
    }

    @DisplayName("상품을 전체 조회한다.")
    @Test
    void list() {
        //given
        Long id = 1L;
        String name = "product";
        BigDecimal price = BigDecimal.valueOf(1000);
        given(productRepository.findAll()).willReturn(Arrays.asList(상품_데이터_생성(id, name, price)));

        //when
        List<ProductResponseDto> response = productService.list();

        //then
        assertEquals(1, response.size());
        상품_데이터_확인(response.get(0), id, name, price);
    }

    private void 상품_데이터_확인(ProductResponseDto response, Long id, String name, BigDecimal price) {
        assertAll(
                () -> assertEquals(id, response.getId()),
                () -> assertEquals(name, response.getName()),
                () -> assertEquals(price, response.getPrice())
        );
    }
}