package kitchenpos.application.command;

import kitchenpos.application.query.ProductQueryService;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductCreate;
import kitchenpos.dto.response.ProductViewResponse;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.ProductFixture.양념치킨_1000원;
import static kitchenpos.fixture.ProductFixture.콜라_100원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    private ProductService productService;
    private ProductQueryService productQueryService;

    @BeforeEach
    void setUp() {
        ProductFixture.cleanUp();

        productService = new ProductService(productRepository);
        productQueryService = new ProductQueryService(productRepository);
    }

    @Test
    @DisplayName("create - 정상적인 상품 등록")
    void 정상적인_상품_등록() {
        // given
        ProductCreate product = new ProductCreate(new Name("name"), new Price(1000));

        // when
        when(productRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        productService.create(product);

        // then
        verify(productRepository, VerificationModeFactory.times(1))
                .save(any());
    }

    @Test
    @DisplayName("list - 정상적인 상품 전체 조회")
    void 정상적인_상품_전체_조회() {
        // given
        List<Product> products = Arrays.asList(양념치킨_1000원, 콜라_100원);

        // when
        when(productRepository.findAll()).thenReturn(products);

        List<ProductViewResponse> list = productQueryService.list();

        // then
        List<ProductViewResponse> responses = products.stream()
                .map(ProductViewResponse::of)
                .collect(Collectors.toList());
        assertThat(list).containsExactlyElementsOf(responses);

        verify(productRepository, VerificationModeFactory.times(1)).findAll();
    }
}