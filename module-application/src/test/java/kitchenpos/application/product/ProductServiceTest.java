package kitchenpos.application.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 생성에 성공한다.")
    void create() {
        Product product = Product.of("허니콤보", 19_000L);
        when(productRepository.save(any())).thenReturn(product);
        ProductRequest productRequest = new ProductRequest("허니콤보", 19_000L);

        ProductResponse actual = productService.create(productRequest);

        assertThat(actual.getName()).isEqualTo(productRequest.getName());
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void findAll() {
        Product honeyCombo = Product.of("허니콤보", 19_000L);
        Product redCombo = Product.of("레드콤보", 19_000L);
        when(productRepository.findAll()).thenReturn(Lists.list(honeyCombo, redCombo));

        List<ProductResponse> actual = productService.list();
        List<String> names = actual.stream()
                .map(ProductResponse::getName)
                .collect(Collectors.toList());

        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(2);
        assertThat(names).containsExactly(honeyCombo.getName(), redCombo.getName());
    }
}
