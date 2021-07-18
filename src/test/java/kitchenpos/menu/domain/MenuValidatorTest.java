package kitchenpos.menu.domain;

import kitchenpos.common.Price;
import kitchenpos.exception.CannotFindException;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;


import static kitchenpos.common.Message.*;
import static kitchenpos.menu.MenuTestFixture.*;
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

    @DisplayName("메뉴 그룹이 기존에 등록되어 있지 않은 경우, 메뉴 등록시 예외가 발생한다")
    @Test
    void 메뉴그룹_등록되어있지_않은_경우_예외발생() {
        //Given
        when(menuGroupRepository.findById(any())).thenReturn(Optional.empty());

        //When + Then
        Throwable 메뉴그룹_없음_예외 = catchThrowable(() -> menuValidator.validate(맥모닝콤보_요청));
        assertThat(메뉴그룹_없음_예외).isInstanceOf(CannotFindException.class)
                .hasMessage(ERROR_MENUGROUP_NOT_FOUND.showText());
    }

    @DisplayName("메뉴 상품이 기존에 상품으로 등록되어 있지 않은 경우, 메뉴 등록시 예외가 발생한다")
    @Test
    void 메뉴상품_등록되어있지_않은_경우_예외발생() {
        //Given
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(new MenuGroup()));

        //When + Then
        Throwable 상품_없음_예외 = catchThrowable(() -> menuValidator.validate(맥모닝콤보_요청));
        assertThat(상품_없음_예외).isInstanceOf(CannotFindException.class)
                .hasMessage(ERROR_PRODUCT_NOT_FOUND.showText());
    }

    @DisplayName("메뉴 등록시, 가격이 메뉴상품 목록의 가격합보다 큰 경우 예외발생")
    @Test
    void 메뉴등록시_가격이_메뉴상품_목록의_가격합보다_큰_경우_예외발생() {
        //Given

        Product 아이스_아메리카노 = new Product(아이스_아메리카노_상품_ID, "아이스 아메리카노", Price.valueOf(2000));
        Product 에그맥머핀 = new Product(에그맥머핀_상품_ID, "에그 맥머핀", Price.valueOf(3000));

        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(new MenuGroup()));
        when(productRepository.findAllById(any())).thenReturn(Arrays.asList(아이스_아메리카노, 에그맥머핀));

        //When + Then
        Throwable 메뉴_가격_큼_예외 = catchThrowable(() -> menuValidator.validate(맥모닝콤보_요청));
        assertThat(메뉴_가격_큼_예외).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_MENU_PRICE_CANNOT_BE_BIGGER_THAN_MENUPRODUCTS_TOTAL.showText());
    }
}
