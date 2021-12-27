package kitchenpos.menu.domain;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.product.application.ProductService;
import kitchenpos.common.domain.Price;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.menu.exception.NotCorrectMenuPriceException;
import kitchenpos.menugroup.exception.NotFoundMenuGroupException;
import kitchenpos.product.exception.NotFoundProductException;
import kitchenpos.common.vo.MenuGroupId;
import kitchenpos.common.vo.ProductId;

@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {
    @Mock
    private MenuGroupService menuGroupService;

    @Mock
    private ProductService productService;
    
    @InjectMocks
    private MenuValidator menuValidator;
    
    @DisplayName("메뉴생성시 미등록 상품이 포함되면 예외가 발생된다.")
    @Test
    void exception_createMenu_notExistProduct() {
        // given
        Product 뿌링클치킨 = Product.of(1L, "뿌링클치킨", Price.of(15_000));
        Product 치킨무 = Product.of(2L, "치킨무", Price.of(1_000));
        Product 코카콜라 = Product.of(3L, "코카콜라", Price.of(3_000));

        MenuProduct 뿌링클콤보_뿌링클치킨 = MenuProduct.of(ProductId.of(뿌링클치킨.getId()), 1L);
        MenuProduct 뿌링클콤보_치킨무 = MenuProduct.of(ProductId.of(치킨무.getId()), 1L);
        MenuProduct 뿌링클콤보_코카콜라 = MenuProduct.of(ProductId.of(코카콜라.getId()), 1L);

        MenuGroup 치킨_메뉴그룹 = MenuGroup.of("치킨");

        Menu 뿌링클콤보 = Menu.of("뿌링클콤보", Price.of(18_000), MenuGroupId.of(치킨_메뉴그룹.getId()), MenuProducts.of(List.of(뿌링클콤보_뿌링클치킨, 뿌링클콤보_치킨무, 뿌링클콤보_코카콜라)));

        when(productService.findAllByIds(anyList())).thenReturn(List.of(뿌링클치킨, 치킨무));
        when(menuGroupService.findById(nullable(Long.class))).thenReturn(치킨_메뉴그룹);
        
        // when
        // then
        Assertions.assertThatExceptionOfType(NotFoundProductException.class)
                    .isThrownBy(() -> menuValidator.getValidatedMenu(MenuDto.of(뿌링클콤보)));
    }

    @DisplayName("메뉴등록시 메뉴 가격이 상품의 가격 총합이 보다 클 시 예외가 발생된다.")
    @Test
    void exception_createMenu_productPriceSumGreaterThanMenuPrice() {
        // given
        Product 뿌링클치킨 = Product.of(1L, "뿌링클치킨", Price.of(15_000));
        Product 치킨무 = Product.of(2L, "치킨무", Price.of(1_000));
        Product 코카콜라 = Product.of(3L, "코카콜라", Price.of(3_000));

        MenuProduct 뿌링클콤보_뿌링클치킨 = MenuProduct.of(ProductId.of(뿌링클치킨.getId()), 1L);
        MenuProduct 뿌링클콤보_치킨무 = MenuProduct.of(ProductId.of(치킨무.getId()), 1L);
        MenuProduct 뿌링클콤보_코카콜라 = MenuProduct.of(ProductId.of(코카콜라.getId()), 1L);

        MenuGroup 치킨_메뉴그룹 = MenuGroup.of("치킨");

        Menu 뿌링클콤보 = Menu.of("뿌링클콤보", Price.of(28_000), MenuGroupId.of(치킨_메뉴그룹.getId()), MenuProducts.of(List.of(뿌링클콤보_뿌링클치킨, 뿌링클콤보_치킨무, 뿌링클콤보_코카콜라)));

        when(productService.findAllByIds(anyList())).thenReturn(List.of(뿌링클치킨, 치킨무, 코카콜라));
        when(menuGroupService.findById(nullable(Long.class))).thenReturn(치킨_메뉴그룹);

        // when
        // then
        Assertions.assertThatExceptionOfType(NotCorrectMenuPriceException.class)
                    .isThrownBy(() -> menuValidator.getValidatedMenu(MenuDto.of(뿌링클콤보)));
    }


    @DisplayName("메뉴에대한 메뉴그룹이 없으면 예외가 발생한다.")
    @Test
    void exception_createMenu_containNotExistMenuGroup() {
        // given
        MenuGroup 치킨_메뉴그룹 = MenuGroup.of("치킨");
        Menu 뿌링클콤보 = Menu.of("뿌링클콤보", Price.of(18_000), MenuGroupId.of(치킨_메뉴그룹.getId()));

        when(menuGroupService.findById(nullable(Long.class))).thenThrow(NotFoundMenuGroupException.class);

        // when
        // then
        Assertions.assertThatExceptionOfType(NotFoundMenuGroupException.class)
                    .isThrownBy(() -> menuValidator.getValidatedMenu(MenuDto.of(뿌링클콤보.getName(), BigDecimal.valueOf(18_000), 치킨_메뉴그룹.getId(), List.of(MenuProductDto.of(1L, 1L), MenuProductDto.of(2L, 1L), MenuProductDto.of(3L, 1L)))));
    }
}
