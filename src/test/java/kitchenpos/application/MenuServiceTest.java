package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repos.MenuGroupRepository;
import kitchenpos.repos.MenuProductRepository;
import kitchenpos.repos.MenuRepository;
import kitchenpos.repos.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    MenuProductRepository menuProductRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    MenuService menuService;

    private Product product;

    private Product product2;

    private MenuProduct menuProduct;

    private MenuProduct menuProduct2;

    private MenuGroup menuGroup;

    private Menu menu;

    private Menu menu2;

    @BeforeEach
    void setUp() {
        product = Product.of(1L, "후라이드치킨", new BigDecimal(16000.00));
        product2 = Product.of(2L, "양념치킨", new BigDecimal(16000.00));
        menuGroup = MenuGroup.of(1L, "메뉴그룹1");
        menu = Menu.of(1L, "후라이드치킨", new BigDecimal(16000.00), menuGroup, Arrays.asList(menuProduct));
        menu2 = Menu.of(2L, "양념치킨", new BigDecimal(16000.00), menuGroup, Arrays.asList(menuProduct2));
        menuProduct = MenuProduct.of(1L, menu, product, 1);
        menuProduct2 = MenuProduct.of(2L, menu2, product2, 1);
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        // given
        when(menuGroupRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(menuRepository.save(menu)).thenReturn(menu);
        when(menuProductRepository.save(menuProduct)).thenReturn(menuProduct);

        // when
        Menu expected = menuService.create(menu);

        // then
        assertThat(menu.getId()).isEqualTo(expected.getId());
    }

    @DisplayName("가격이 없거나 음수인 경우에는 메뉴를 등록할 수 없다.")
    @Test
    void create2() {
        // given
        Menu 가격_음수_메뉴 = Menu.of(1L, "후라이드치킨", new BigDecimal(-1000), menuGroup, Arrays.asList(menuProduct));
        Menu 가격_없는_메뉴 = Menu.of(1L, "후라이드치킨", null, menuGroup, Arrays.asList(menuProduct));

        //then
        assertAll(
                () -> assertThatThrownBy(
                        () -> menuService.create(가격_음수_메뉴)
                ).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(
                        () -> menuService.create(가격_없는_메뉴)
                ).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("메뉴그룹없이 메뉴를 등록할 수 없다.")
    @Test
    void create3() {
        // given
        menuGroup.setId(null);
        Menu 메뉴_그룹_없는_메뉴 = Menu.of(1L, "후라이드치킨", new BigDecimal(16000.00), menuGroup, Arrays.asList(menuProduct));
        when(menuGroupRepository.existsById(any())).thenReturn(false);

        //then
        assertThatThrownBy(
                () -> menuService.create(메뉴_그룹_없는_메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴상품 목록의 총 금액의 합보다 메뉴 금액이 비싼 경우 등록할 수 없다.")
    @Test
    void create4() {
        // given
        Menu 메뉴_금액_비싼_메뉴 = Menu.of(1L, "후라이드치킨", new BigDecimal(33000.00), null, Arrays.asList(menuProduct, menuProduct2));
        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));

        //then
        assertThatThrownBy(
                () -> menuService.create(메뉴_금액_비싼_메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // given
        List<Menu> actual = Arrays.asList(menu, menu2);
        when(menuRepository.findAll()).thenReturn(actual);

        // when
        List<Menu> expected = menuService.list();

        // then
        assertThat(actual.size()).isEqualTo(expected.size());
    }
}
