package kitchenpos.application.menu;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.product.ProductService;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.MenuDto;
import kitchenpos.dto.menu.MenuProductDto;
import kitchenpos.exception.menu.NotCorrectMenuPriceException;
import kitchenpos.exception.menu.NotFoundMenuGroupException;
import kitchenpos.exception.product.NotFoundProductException;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.menu.MenuRepository;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴가 저장된다.")
    @Test
    void craete_menu() {
        // given
        Product 뿌링클치킨 = Product.of("뿌링클치킨", Price.of(15_000));
        Product 치킨무 = Product.of("치킨무", Price.of(1_000));
        Product 코카콜라 = Product.of("코카콜라", Price.of(3_000));

        MenuGroup 치킨_메뉴그룹 = MenuGroup.of("치킨");

        Menu 뿌링클콤보 = Menu.of("뿌링클콤보", Price.of(18_000), 치킨_메뉴그룹);

        MenuProduct 뿌링클콤보_뿌링클치킨 = MenuProduct.of(뿌링클치킨, 1L);
        MenuProduct 뿌링클콤보_치킨무 = MenuProduct.of(치킨무, 2L);
        MenuProduct 뿌링클콤보_코카콜라 = MenuProduct.of(코카콜라, 3L);
        
        뿌링클콤보_뿌링클치킨.acceptMenu(뿌링클콤보);
        뿌링클콤보_치킨무.acceptMenu(뿌링클콤보);
        뿌링클콤보_코카콜라.acceptMenu(뿌링클콤보);

        when(menuGroupRepository.findById(nullable(Long.class))).thenReturn(Optional.of(치킨_메뉴그룹));
        when(productService.findById(nullable(Long.class))).thenReturn(뿌링클치킨, 치킨무, 코카콜라);
        when(productService.sumOfPrices(anyList())).thenReturn(Price.of(19_000));
        when(menuRepository.save(any(Menu.class))).thenReturn(뿌링클콤보);

        MenuDto 메뉴생성_요청전문 = MenuDto.of("뿌링클콤보", BigDecimal.valueOf(18_000), 1L, List.of(MenuProductDto.of(1L, 1L), MenuProductDto.of(2L, 2L), MenuProductDto.of(3L, 3L)));

        // when
        MenuDto savedMenu = menuService.create(메뉴생성_요청전문);

        // then
        Assertions.assertThat(savedMenu.getName()).isEqualTo("뿌링클콤보");
        Assertions.assertThat(savedMenu.getPrice()).isEqualTo(BigDecimal.valueOf(18_000));
        Assertions.assertThat(savedMenu.getMenuProducts()).isEqualTo(List.of(MenuProductDto.of(null, 1L), MenuProductDto.of(null, 2L), MenuProductDto.of(null, 3L)));
    }

    @DisplayName("메뉴에대한 메뉴그룹이 없으면 예외가 발생한다.")
    @Test
    void exception_createMenu_containNotExistMenuGroup() {
        // given
        MenuGroup 치킨_메뉴그룹 = MenuGroup.of("치킨");
        Menu 뿌링클콤보 = Menu.of("뿌링클콤보", Price.of(18_000), 치킨_메뉴그룹);

        when(productService.sumOfPrices(anyList())).thenReturn(Price.of(19_000));
        when(menuGroupRepository.findById(nullable(Long.class))).thenThrow(NotFoundMenuGroupException.class);

        // when
        // then
        Assertions.assertThatExceptionOfType(NotFoundMenuGroupException.class)
                    .isThrownBy(() -> menuService.create(MenuDto.of(뿌링클콤보)));
    }

    @DisplayName("미등록 상품이 포함된 메뉴를 생성시 예외가 발생한다.")
    @Test
    void exception_createMenu_notExistProduct() {
        // given
        Product 뿌링클치킨 = Product.of("뿌링클치킨", Price.of(15_000));
        Product 치킨무 = Product.of("치킨무", Price.of(1_000));
        Product 코카콜라 = Product.of("코카콜라", Price.of(3_000));
        
        MenuProduct 뿌링클콤보_뿌링클치킨 = MenuProduct.of(뿌링클치킨, 1L);
        MenuProduct 뿌링클콤보_치킨무 = MenuProduct.of(치킨무, 1L);
        MenuProduct 뿌링클콤보_코카콜라 = MenuProduct.of(코카콜라, 1L);

        MenuGroup 치킨_메뉴그룹 = MenuGroup.of("치킨");
        Menu 뿌링클콤보 = Menu.of("뿌링클콤보", Price.of(18_000), 치킨_메뉴그룹);
        
        뿌링클콤보_뿌링클치킨.acceptMenu(뿌링클콤보);
        뿌링클콤보_치킨무.acceptMenu(뿌링클콤보);
        뿌링클콤보_코카콜라.acceptMenu(뿌링클콤보);

        when(menuGroupRepository.findById(뿌링클콤보.getMenuGroup().getId())).thenReturn(Optional.of(치킨_메뉴그룹));
        when(productService.findById(뿌링클콤보_뿌링클치킨.getProduct().getId())).thenReturn(뿌링클치킨);
        when(productService.findById(뿌링클콤보_치킨무.getProduct().getId())).thenReturn(치킨무);
        when(productService.findById(뿌링클콤보_코카콜라.getProduct().getId())).thenThrow(NotFoundProductException.class);
        when(productService.sumOfPrices(anyList())).thenReturn(Price.of(19_000));

        // when
        // then
        Assertions.assertThatExceptionOfType(NotFoundProductException.class)
                    .isThrownBy(() -> menuService.create(MenuDto.of(뿌링클콤보)));
    }

    @DisplayName("메뉴 가격이 상품의 가격 총합이 보다 클 시 예외가 발생한다.")
    @Test
    void exception_createMenu_productPriceSumGreaterThanMenuPrice() {
        // given
        MenuGroup 치킨_메뉴그룹 = MenuGroup.of("치킨");
        Menu 뿌링클콤보 = Menu.of("뿌링클콤보", Price.of(20_000), 치킨_메뉴그룹);

        when(productService.sumOfPrices(anyList())).thenReturn(Price.of(19_000));

        // when
        // then
        Assertions.assertThatExceptionOfType(NotCorrectMenuPriceException.class)
                    .isThrownBy(() -> menuService.create(MenuDto.of(뿌링클콤보)));
    }

    @DisplayName("메뉴가 조회된다.")
    @Test
    void search_menu() {
        // given
        MenuGroup 치킨_메뉴그룹 = MenuGroup.of("치킨");
        Menu 뿌링클콤보 = Menu.of("뿌링클콤보", Price.of(18_000), 치킨_메뉴그룹);

        when(menuRepository.findAll()).thenReturn(List.of(뿌링클콤보));

        // when
        List<MenuDto> searchedMenu = menuService.list();

        // then
        Assertions.assertThat(searchedMenu).isEqualTo(List.of(MenuDto.of("뿌링클콤보", BigDecimal.valueOf(18_000), null, Lists.newArrayList())));
    }
}
