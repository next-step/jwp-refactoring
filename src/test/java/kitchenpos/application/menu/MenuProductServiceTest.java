package kitchenpos.application.menu;

import static kitchenpos.utils.generator.ProductFixtureGenerator.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:MenuProduct")
class MenuProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuProductService menuProductService;

    private Long firstProductId, secondProductId;
    private Product firstProduct, secondProduct;
    private MenuProductRequest firstMenuProductRequest, secondMenuProductRequest;
    private List<MenuProductRequest> menuProductRequestList;

    @BeforeEach
    void setUp() {
        firstProductId = 1L;
        secondProductId = 2L;
        firstProduct = 상품_생성();
        secondProduct = 상품_생성();
        firstMenuProductRequest = new MenuProductRequest(firstProductId, 1);
        secondMenuProductRequest = new MenuProductRequest(secondProductId, 2);
        menuProductRequestList = Arrays.asList(firstMenuProductRequest, secondMenuProductRequest);
    }

    @Test
    @DisplayName("메뉴 상품 요청 객체와 상품을 매핑하여 메뉴 상품 산출")
    public void findMenuProductByMenuProductRequest() {
        // Given
        given(productRepository.findById(firstProductId)).willReturn(Optional.of(firstProduct));
        given(productRepository.findById(secondProductId)).willReturn(Optional.of(secondProduct));

        // When
        List<MenuProduct> actualMenuProduct = menuProductService
            .findMenuProductByMenuProductRequest(menuProductRequestList);

        // Then
        verify(productRepository, times(2)).findById(anyLong());
        assertThat(actualMenuProduct)
            .extracting(MenuProduct::getProduct, MenuProduct::getQuantity)
            .contains(
                tuple(firstProduct, firstMenuProductRequest.getQuantity()),
                tuple(secondProduct, secondMenuProductRequest.getQuantity())
            );
    }
}
