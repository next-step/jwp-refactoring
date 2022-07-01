package kitchenpos.application;

import static kitchenpos.utils.generator.ProductFixtureGenerator.generateProduct;
import static kitchenpos.utils.generator.ProductFixtureGenerator.generateProducts;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:Product")
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품을 생성한다.")
    public void createProduct() {
        // Given
        final Product given = generateProduct();
        given(productDao.save(any(Product.class))).will(AdditionalAnswers.returnsFirstArg());

        // When
        Product actual = productService.create(given);

        // Then
        verify(productDao).save(any(Product.class));
        assertThat(actual).isEqualTo(given);
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    public void getProducts() {
        // Given
        final int generateProductCount = 5;
        List<Product> givenProducts = generateProducts(generateProductCount);
        given(productDao.findAll()).willReturn(givenProducts);

        // When
        List<Product> actualProducts = productService.list();

        // Then
        verify(productDao).findAll();
        assertThat(actualProducts).hasSize(generateProductCount);
    }
}
