package kitchenpos.menu.domain.product;


import kitchenpos.menu.aplication.ProductService;
import kitchenpos.menu.dto.ProductResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.ProductDomainFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@DisplayName("Mockito - 상품 관리")
class ProductMockitoTest {

    private ProductRepository productRepository;
    private ProductService productService;

    private void setUpMock() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    @DisplayName("상품 생성")
    void createProduct() {
        //given
        setUpMock();
        when(productRepository.save(any(Product.class))).thenReturn(사이다);

        //when
        final ProductResponse actual = productService.saveProduct(사이다_요청);

        //then
        assertThat(actual.getName()).isEqualTo(사이다.getName());
    }

    @Test
    @DisplayName("상품 조회")
    void findAllProduct() {
        //given
        setUpMock();
        when(productRepository.findAll()).thenReturn(Lists.newArrayList(사이다, 양념소스));
        List<String> expectedList = Lists.newArrayList(사이다, 양념소스).stream()
                .map(Product::getName)
                .collect(Collectors.toList());

        //when
        final List<ProductResponse> actual = productService.findAllProduct();
        List<String> actualList = actual.stream()
                .map(ProductResponse::getName)
                .collect(Collectors.toList());

        //then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actualList).containsAll(expectedList)
        );
    }

}
