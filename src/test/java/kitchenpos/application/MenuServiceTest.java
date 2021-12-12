package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.menu.MenuService;
import kitchenpos.application.product.ProductService;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.MenuDto;
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

    private MenuGroup 치킨_메뉴그룹;

    private Menu 뿌링클콤보;

    private Product 뿌링클치킨;
    private Product 치킨무;
    private Product 코카콜라;

    private MenuProduct 뿌링클콤보_뿌링클치킨;
    private MenuProduct 뿌링클콤보_치킨무;
    private MenuProduct 뿌링클콤보_코카콜라;

    @BeforeEach
    void setUp() {
        치킨_메뉴그룹 = MenuGroup.of("치킨");

        뿌링클치킨 = Product.of(1L, "뿌링클치킨", Price.of(15_000));
        치킨무 = Product.of(2L, "치킨무", Price.of(1_000));
        코카콜라 = Product.of(3L, "코카콜라", Price.of(3_000));

        뿌링클콤보_뿌링클치킨 = MenuProduct.of(뿌링클콤보, 뿌링클치킨, 1L);
        뿌링클콤보_치킨무 = MenuProduct.of(뿌링클콤보, 치킨무, 1L);
        뿌링클콤보_코카콜라 = MenuProduct.of(뿌링클콤보, 코카콜라, 1L);

        뿌링클콤보 = Menu.of("뿌링클콤보", Price.of(18_000), 치킨_메뉴그룹, List.of(뿌링클콤보_뿌링클치킨, 뿌링클콤보_치킨무, 뿌링클콤보_코카콜라));
    }

    @DisplayName("메뉴가 저장된다.")
    @Test
    void craete_menu() {
        // when
        when(menuGroupRepository.findById(this.뿌링클콤보.getMenuGroup().getId())).thenReturn(Optional.of(this.치킨_메뉴그룹));
        when(productService.findById(anyLong())).thenReturn(this.뿌링클치킨, this.치킨무, this.코카콜라);
        when(productService.sumOfPrices(anyList())).thenReturn(Price.of(19_000));


        when(menuRepository.save(any(Menu.class))).thenReturn(this.뿌링클콤보);

        MenuDto savedMenu = menuService.create(MenuDto.of(this.뿌링클콤보));

        // then
        Assertions.assertThat(savedMenu).isEqualTo(MenuDto.of(this.뿌링클콤보));
    }

    @DisplayName("메뉴에대한 메뉴그룹이 없으면 예외가 발생한다.")
    @Test
    void exception_createMenu_containNotExistMenuGroup() {
        // given
        when(productService.sumOfPrices(anyList())).thenReturn(Price.of(19_000));
        when(menuGroupRepository.findById(null)).thenThrow(IllegalArgumentException.class);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> menuService.create(MenuDto.of(this.뿌링클콤보)));
    }

    @DisplayName("미등록 상품이 포함된 메뉴를 생성시 예외가 발생한다.")
    @Test
    void exception_createMenu_notExistProduct() {
        // given
        when(menuGroupRepository.findById(this.뿌링클콤보.getMenuGroup().getId())).thenReturn(Optional.of(this.치킨_메뉴그룹));
        when(productService.findById(this.뿌링클콤보_뿌링클치킨.getProduct().getId())).thenReturn(this.뿌링클치킨);
        when(productService.findById(this.뿌링클콤보_치킨무.getProduct().getId())).thenReturn(this.치킨무);
        when(productService.findById(this.뿌링클콤보_코카콜라.getProduct().getId())).thenThrow(IllegalArgumentException.class);
        when(productService.sumOfPrices(anyList())).thenReturn(Price.of(19_000));

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> menuService.create(MenuDto.of(this.뿌링클콤보)));
    }

    @DisplayName("메뉴 가격이 상품의 가격 총합이 보다 클 시 예외가 발생한다.")
    @Test
    void exception_createMenu_productPriceSumGreaterThanMenuPrice() {
        // given
        when(productService.sumOfPrices(anyList())).thenReturn(Price.of(19_000));
        this.뿌링클콤보.changePrice(Price.of(20_000));

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> menuService.create(MenuDto.of(this.뿌링클콤보)));
    }

    @DisplayName("메뉴가 조회된다.")
    @Test
    void search_menu() {
        // given
        when(menuRepository.findAll()).thenReturn(List.of(this.뿌링클콤보));

        // when
        List<MenuDto> searchedMenu = menuService.list();

        // then
        Assertions.assertThat(searchedMenu).isEqualTo(List.of(MenuDto.of(this.뿌링클콤보)));
    }
}
