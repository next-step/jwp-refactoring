package menu.application;

import menu.repository.MenuGroupRepository;
import product.repository.ProductRepository;
import product.domain.Product;
import menu.dto.MenuProductRequest;
import menu.dto.MenuRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @InjectMocks
    private MenuValidator menuValidator;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;

    private Product 감자튀김;
    private Product 치즈볼;

    @BeforeEach
    void setUp() {
        감자튀김 = new Product(1L, "감자튀김", BigDecimal.valueOf(1500));
        치즈볼 = new Product(2L, "피자", BigDecimal.valueOf(1000));
    }

    @Test
    void 등록된_상품으로만_메뉴을_구성해야한다() {
        // given
        MenuRequest request = new MenuRequest("A세트", BigDecimal.valueOf(5000), 1L,
                Arrays.asList(new MenuProductRequest(1L, 1), new MenuProductRequest(2L, 1)));
        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(productRepository.findByIdIn(any())).willReturn(Collections.singletonList(감자튀김));


        // when & then
        assertThatThrownBy(() -> menuValidator.validate(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_구성_상품들의_합보다_큰_경우() {
        // given
        MenuRequest request = new MenuRequest("A세트", BigDecimal.valueOf(5000), 1L,
                Arrays.asList(new MenuProductRequest(1L, 1), new MenuProductRequest(2L, 1)));
        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(productRepository.findByIdIn(any())).willReturn(Arrays.asList(감자튀김, 치즈볼));


        // when & then
        assertThatThrownBy(() -> menuValidator.validate(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록할_메뉴_그룹이_없는_경우() {
        // given
        MenuRequest request = new MenuRequest("A세트", BigDecimal.valueOf(5000), 1L,
                Arrays.asList(new MenuProductRequest(1L, 1), new MenuProductRequest(2L, 1)));
        given(menuGroupRepository.existsById(any())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> menuValidator.validate(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

}