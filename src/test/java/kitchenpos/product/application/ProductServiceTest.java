package kitchenpos.product.application;

import static kitchenpos.ServiceTestFactory.createProductBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.product.dao.FakeProductDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProductServiceTest {
    private final ProductService productService = new ProductService(new FakeProductDao());
    private Product honeyCombo;
    private Product redCombo;

    @BeforeEach
    void setUp() {
        honeyCombo = createProductBy(1L, "허니콤보", 20_000L);
        redCombo = createProductBy(2L,"레드콤보", 19_000L);
    }
    @Test

    @DisplayName("금액이 0원 미만이면 상품 생성에 실패한다.")
    void createWithInvalidPrice() {
        honeyCombo.setPrice(BigDecimal.valueOf(-1L));

        assertThatThrownBy(() -> {
            productService.create(honeyCombo);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 생성에 성공한다.")
    void create() {
        //when
        Product saved = productService.create(honeyCombo);
        //then
        assertThat(saved.getName()).isEqualTo(honeyCombo.getName());
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void findAll() {
        //given
        productService.create(honeyCombo);
        productService.create(redCombo);
        List<String> expectedNames = findProductNames(Arrays.asList(honeyCombo, redCombo));

        //when
        List<Product> actual = productService.list();
        List<String> actualNames = findProductNames(actual);

        //then
        assertThat(actual).isNotNull();
        assertThat(actualNames).containsExactlyElementsOf(expectedNames);
    }

    private List<String> findProductNames(List<Product> products) {
        return products
                .stream()
                .map(Product::getName)
                .collect(Collectors.toList());
    }
}
