package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductCreate;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.ProductFixture.양념치킨_1000원;
import static kitchenpos.fixture.ProductFixture.콜라_100원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        ProductFixture.cleanUp();

        this.productService = new ProductService(productDao);
    }

    @Test
    @DisplayName("create - 정상적인 상품 등록")
    void 정상적인_상품_등록() {
        // given
        ProductCreate product = new ProductCreate("name", new Price(1000));

        // when
        when(productDao.save(any())).thenAnswer(i -> i.getArgument(0));

        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(savedProduct.getName()).isEqualTo(product.getName());

        verify(productDao, VerificationModeFactory.times(1))
                .save(savedProduct);
    }

    @Test
    @DisplayName("list - 정상적인 상품 전체 조회")
    void 정상적인_상품_전체_조회() {
        // given
        List<Product> products = Arrays.asList(양념치킨_1000원, 콜라_100원);

        // when
        when(productDao.findAll()).thenReturn(products);

        List<Product> list = productService.list();

        // then
        assertThat(list).containsExactlyElementsOf(products);

        verify(productDao, VerificationModeFactory.times(1)).findAll();
    }
}