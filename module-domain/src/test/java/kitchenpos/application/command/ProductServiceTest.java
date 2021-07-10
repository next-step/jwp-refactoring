package kitchenpos.application.command;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.ProductCreate;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        ProductFixture.cleanUp();

        productService = new ProductService(productRepository);
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
}