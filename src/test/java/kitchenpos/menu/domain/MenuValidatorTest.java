package kitchenpos.menu.domain;

import kitchenpos.common.Price;
import kitchenpos.common.Quantity;
import kitchenpos.exception.CannotFindException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.common.Message.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    MenuValidator menuValidator;

    private String 메뉴이름 = "메뉴이름";
    private Price 가격 = Price.valueOf(15000);
    private Long 메뉴그룹_ID = 1L;
    private Long 후라이드_상품_ID = 1L;
    private Long 콜라_상품_ID = 2L;

    private final MenuProduct 후라이드_한마리 = new MenuProduct(후라이드_상품_ID, Quantity.of(1L));
    private final MenuProduct 콜라_한개 = new MenuProduct(콜라_상품_ID, Quantity.of(1L));
    private List<MenuProduct> 메뉴상품들 = Arrays.asList(후라이드_한마리, 콜라_한개);
    private Menu 메뉴 = new Menu(메뉴이름, 가격, 메뉴그룹_ID, 메뉴상품들);

    @DisplayName("메뉴 그룹이 기존에 등록되어 있지 않은 경우, 메뉴 등록시 예외가 발생한다")
    @Test
    void 메뉴그룹_등록되어있지_않은_경우_예외발생() {
        //Given
        when(menuGroupRepository.findById(any())).thenReturn(Optional.empty());

        //When + Then
        Throwable 메뉴그룹_없음_예외 = catchThrowable(() -> menuValidator.validate(메뉴));
        assertThat(메뉴그룹_없음_예외).isInstanceOf(CannotFindException.class)
                .hasMessage(ERROR_MENUGROUP_NOT_FOUND.showText());
    }

    @DisplayName("메뉴 상품이 기존에 상품으로 등록되어 있지 않은 경우, 메뉴 등록시 예외가 발생한다")
    @Test
    void 메뉴상품_등록되어있지_않은_경우_예외발생() {
        //Given
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(new MenuGroup()));

        //When + Then
        Throwable 상품_없음_예외 = catchThrowable(() -> menuValidator.validate(메뉴));
        assertThat(상품_없음_예외).isInstanceOf(CannotFindException.class)
                .hasMessage(ERROR_PRODUCT_NOT_FOUND.showText());
    }

    @DisplayName("메뉴 등록시, 가격이 메뉴상품 목록의 가격합보다 큰 경우 예외발생")
    @Test
    void 메뉴등록시_가격이_메뉴상품_목록의_가격합보다_큰_경우_예외발생() {
        //Given
        Product 후라이드 = new Product("후라이드", Price.valueOf(5000));
        Product 콜라 = new Product("콜라", Price.valueOf(1000));

        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(new MenuGroup()));
        when(productRepository.findAllById(any())).thenReturn(Arrays.asList(후라이드, 콜라));

        //When + Then
        Throwable 메뉴_가격_큼_예외 = catchThrowable(() -> menuValidator.validate(메뉴));
        assertThat(메뉴_가격_큼_예외).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_MENU_PRICE_CANNOT_BE_BIGGER_THAN_MENUPRODUCTS_TOTAL.showText());
    }
}
