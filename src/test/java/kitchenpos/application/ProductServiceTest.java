package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;
    private Product 토마토;
    private Product 감자;

    @BeforeEach
    void setUp() {
        토마토 = createProduct(1L, "토마토", 1000);
        감자 = createProduct(2L, "감자", 2000);
    }

    @Test
    void 상품_생성() {
        when(productDao.save(토마토)).thenReturn(토마토);
        Product result = productService.create(토마토);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(토마토.getId()),
                () -> assertThat(result.getName()).isEqualTo(토마토.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(토마토.getPrice())
        );
    }

    @Test
    void 상품_생성_가격_없는_경우_예외() {
        assertThatThrownBy(
                () -> productService.create(createProduct(3L, "공기"))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_생성_가격_0_미만_예외() {
        assertThatThrownBy(
                () -> productService.create(createProduct(3L, "공기", -1))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_목록_조회() {
        when(productDao.findAll()).thenReturn(Arrays.asList(토마토, 감자));
        List<Product> result = productService.list();
        assertThat(toIdList(result)).containsExactlyElementsOf(toIdList(Arrays.asList(토마토, 감자)));
    }

    public static Product createProduct(Long id, String name, long price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }

    private Product createProduct(Long id, String name) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);

        return product;
    }

    private List<Long> toIdList(List<Product> products) {
        return products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());
    }
}