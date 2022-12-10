package kitchenpos.menu.application;

import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("ProductService 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product 스테이크;
    private Product 스파게티;

    @BeforeEach
    void setUp() {
        스테이크 = new Product("스테이크", new BigDecimal(25000));
        스파게티 = new Product("스파게티", new BigDecimal(18000));

        ReflectionTestUtils.setField(스테이크, "id", 1L);
        ReflectionTestUtils.setField(스파게티, "id", 2L);
    }

    @Test
    void 상품_등록() {
        given(productRepository.save(any(Product.class))).willReturn(스테이크);

        ProductResponse response = productService.create(new ProductRequest(스테이크.getName(), 스테이크.getPrice()));

        assertThat(response).satisfies(res -> {
            assertEquals(스테이크.getId(), res.getId());
            assertEquals(스테이크.getName(), res.getName());
            assertEquals(스테이크.getPrice(), res.getPrice());
        });
    }

    @Test
    void 상품_목록_조회() {
        given(productRepository.findAll()).willReturn(Arrays.asList(스테이크, 스파게티));

        List<ProductResponse> responses = productService.list();

        assertAll(
                () -> assertThat(responses).hasSize(2),
                () -> assertThat(responses.stream().map(ProductResponse::getName).collect(toList()))
                        .containsExactly(스테이크.getName(), 스파게티.getName())
        );
    }
}
