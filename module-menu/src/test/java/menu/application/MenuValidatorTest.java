package menu.application;

import static menu.fixture.MenuFixture.메뉴_요청_데이터_생성;
import static menu.fixture.MenuProductFixture.메뉴상품_요청_데이터_생성;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;

import common.exception.InvalidPriceException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import menu.dto.MenuProductRequestDto;
import menu.dto.MenuRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import product.domain.Product;
import product.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    private MenuValidator menuValidator;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        menuValidator = new MenuValidator(productRepository);
    }

    @DisplayName("가격이 상품가격의 합보다 크면 생성할 수 없다.")
    @Test
    void checkValidPrice_fail_menuPriceGe() {
        //given
        Long productId1 = 1L;
        Long productId2 = 2L;
        List<MenuProductRequestDto> menuProductRequests
                = Arrays.asList(메뉴상품_요청_데이터_생성(productId1, 2), 메뉴상품_요청_데이터_생성(productId2, 1));
        MenuRequestDto request = 메뉴_요청_데이터_생성("menu", BigDecimal.valueOf(1001), 1L, menuProductRequests);

        given(productRepository.findById(productId1))
                .willReturn(Optional.of(new Product(productId1, "product1", BigDecimal.valueOf(300)))); // 300 * 2 = 600
        given(productRepository.findById(productId2))
                .willReturn(Optional.of(new Product(productId2, "product2", BigDecimal.valueOf(400)))); // 400 * 1 = 300

        //when//then
        assertThatExceptionOfType(InvalidPriceException.class)
                .isThrownBy(() -> menuValidator.validate(request));
    }

    @DisplayName("상품이 존재하지 않으면 생성할 수 없다.")
    @Test
    void create_fail_productNotExists() {
        //given
        Long productId1 = 1L;
        Long productId2 = 2L;
        List<MenuProductRequestDto> menuProductRequests
                = Arrays.asList(메뉴상품_요청_데이터_생성(productId1, 2), 메뉴상품_요청_데이터_생성(productId2, 1));
        MenuRequestDto request = 메뉴_요청_데이터_생성("menu", BigDecimal.valueOf(1001), 1L, menuProductRequests);

        given(productRepository.findById(productId1))
                .willReturn(Optional.empty());

        //when //then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> menuValidator.validate(request));
    }

}