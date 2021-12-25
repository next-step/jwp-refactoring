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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;

@DisplayName("상품 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    public void 상품_생성() {
        Product 크림_파스타 = new Product.builder()
                .id(1L)
                .name("크림 파스타")
                .price(new BigDecimal(18_000))
                .build();
        lenient().when(productDao.save(크림_파스타))
                .thenReturn(크림_파스타);
        assertThat(productService.create(크림_파스타))
                .isNotNull()
                .isInstanceOf(Product.class)
                .isEqualTo(크림_파스타);
    }

    @Test
    public void 상품가격이_음수일_경우_생성실패() {
        Product 크림_파스타 = new Product.builder()
                .id(1L)
                .name("크림 파스타")
                .price(new BigDecimal(-19_000))
                .build();
        lenient().when(productDao.save(크림_파스타))
                .thenThrow(IllegalArgumentException.class);
        assertThatThrownBy(() -> productService.create(크림_파스타))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("0보다 큰 가격을 입력해야 합니다.");
    }

    @Test
    public void 상품목록_조회() {
        Product 크림_파스타 = new Product.builder()
                .id(1L)
                .name("크림 파스타")
                .price(new BigDecimal(18_000))
                .build();
        productService.create(크림_파스타);
        Product 토마토_파스타 = new Product.builder()
                .id(2L)
                .name("토마토 파스타")
                .price(new BigDecimal(19_000))
                .build();
        productService.create(토마토_파스타);

        lenient().when(productDao.findAll())
                .thenReturn(Arrays.asList(크림_파스타, 토마토_파스타));
        assertThat(productService.list())
                .hasSize(2)
                .contains(크림_파스타, 토마토_파스타);
    }

}
