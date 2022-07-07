package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.exception.MenuProductException;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static kitchenpos.factory.MenuFixture.*;
import static kitchenpos.factory.MenuFixture.메뉴_상품_생성;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @InjectMocks
    MenuValidator menuValidator;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    ProductRepository productRepository;

    MenuProduct 양념치킨한마리;
    MenuProduct 간장치킨한마리;

    MenuGroup 한마리메뉴;
    Product 양념치킨;
    Product 간장치킨;
    Menu 치킨;

    @Test
    @DisplayName("메뉴 유효성 테스트(Happy Path)")
    void validation() {
        //given
        한마리메뉴 = 메뉴그룹_생성(1L, "한마리메뉴");
        양념치킨 = 상품생성(1L, "양념치킨", 15000);
        간장치킨 = 상품생성(2L, "간장치킨", 30000);
        양념치킨한마리 = 메뉴_상품_생성(양념치킨.getId());
        간장치킨한마리 = 메뉴_상품_생성(간장치킨.getId());
        MenuProductRequest 양념치킨한마리Request = new MenuProductRequest(양념치킨.getId(), 1);
        MenuProductRequest 간장치킨한마리Request = new MenuProductRequest(간장치킨.getId(), 1);
        MenuRequest 치킨Request = new MenuRequest("치킨", new BigDecimal(30000),
        한마리메뉴.getId(), Arrays.asList(양념치킨한마리Request, 간장치킨한마리Request));
        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        given(productRepository.findById(양념치킨한마리.getProductId())).willReturn(Optional.of(양념치킨));
        given(productRepository.findById(간장치킨한마리.getProductId())).willReturn(Optional.of(간장치킨));

        //then
        menuValidator.validation(치킨Request);
    }

    @Test
    @DisplayName("메뉴 유효성 테스트 - 유효하지 않은 메뉴 그룹 Exception")
    void validationInvalidMenuGroup() {
        //given
        한마리메뉴 = 메뉴그룹_생성(1L, "한마리메뉴");
        양념치킨 = 상품생성(1L, "양념치킨", 15000);
        간장치킨 = 상품생성(2L, "간장치킨", 15000);
        양념치킨한마리 = 메뉴_상품_생성(양념치킨.getId());
        간장치킨한마리 = 메뉴_상품_생성(간장치킨.getId());
        MenuProductRequest 양념치킨한마리Request = new MenuProductRequest(양념치킨.getId(), 1);
        MenuProductRequest 간장치킨한마리Request = new MenuProductRequest(간장치킨.getId(), 1);
        MenuRequest 치킨Request = new MenuRequest("치킨", new BigDecimal(30000),
                한마리메뉴.getId(), Arrays.asList(양념치킨한마리Request, 간장치킨한마리Request));
        given(menuGroupRepository.existsById(anyLong())).willReturn(false);

        //then
        assertThatThrownBy(() -> {
            menuValidator.validation(치킨Request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 유효성 테스트 - 메뉴가격이 상품가격보다 비싸면 안된다")
    void validationInvalidMenuPrice() {
        //given
        한마리메뉴 = 메뉴그룹_생성(1L, "한마리메뉴");
        양념치킨 = 상품생성(1L, "양념치킨", 15000);
        간장치킨 = 상품생성(2L, "간장치킨", 15000);
        양념치킨한마리 = 메뉴_상품_생성(양념치킨.getId());
        간장치킨한마리 = 메뉴_상품_생성(간장치킨.getId());
        MenuProductRequest 양념치킨한마리Request = new MenuProductRequest(양념치킨.getId(), 1);
        MenuProductRequest 간장치킨한마리Request = new MenuProductRequest(간장치킨.getId(), 1);
        MenuRequest 치킨Request = new MenuRequest("치킨", new BigDecimal(100000),
                한마리메뉴.getId(), Arrays.asList(양념치킨한마리Request, 간장치킨한마리Request));
        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        given(productRepository.findById(양념치킨한마리.getProductId())).willReturn(Optional.of(양념치킨));
        given(productRepository.findById(간장치킨한마리.getProductId())).willReturn(Optional.of(간장치킨));

        //then
        assertThatThrownBy(() -> {
            menuValidator.validation(치킨Request);
        }).isInstanceOf(MenuProductException.class)
                .hasMessageContaining(MenuProductException.MENU_PRICE_MORE_EXPENSIVE_PRODUCTS_MSG);
    }

    @Test
    @DisplayName("메뉴 유효성 테스트 - 유효하지 않은 상품")
    void validationInvalidMenuProduct() {
        //given
        한마리메뉴 = 메뉴그룹_생성(1L, "한마리메뉴");
        양념치킨 = 상품생성(1L, "양념치킨", 15000);
        간장치킨 = 상품생성(2L, "간장치킨", 15000);
        양념치킨한마리 = 메뉴_상품_생성(양념치킨.getId());
        간장치킨한마리 = 메뉴_상품_생성(간장치킨.getId());
        MenuProductRequest 양념치킨한마리Request = new MenuProductRequest(양념치킨.getId(), 1);
        MenuProductRequest 간장치킨한마리Request = new MenuProductRequest(간장치킨.getId(), 1);
        MenuRequest 치킨Request = new MenuRequest("치킨", new BigDecimal(100000),
                한마리메뉴.getId(), Arrays.asList(양념치킨한마리Request, 간장치킨한마리Request));
        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        given(productRepository.findById(양념치킨한마리.getProductId())).willReturn(Optional.of(양념치킨));
        given(productRepository.findById(간장치킨한마리.getProductId())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> {
            menuValidator.validation(치킨Request);
        }).isInstanceOf(NoSuchElementException.class);
    }
}