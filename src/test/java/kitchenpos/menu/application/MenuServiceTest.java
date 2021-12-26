package kitchenpos.menu.application;

import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuGroupRepository menuGroupRepository;

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

    private MenuRequest menuRequest;

    private MenuProductRequest menuProductRequest;

    @BeforeEach
    void setUp() {
        product = Product.of(1L, "후라이드치킨", new BigDecimal(16000.00));
        product2 = Product.of(2L, "양념치킨", new BigDecimal(16000.00));
        menuGroup = MenuGroup.of(1L, "메뉴그룹1");
        menu = Menu.of(1L, "후라이드치킨", new BigDecimal(16000.00), menuGroup, Arrays.asList(menuProduct));
        menu2 = Menu.of(2L, "양념치킨", new BigDecimal(16000.00), menuGroup, Arrays.asList(menuProduct2));
        menuProduct = MenuProduct.of(1L, menu, product, 1);
        menuProduct2 = MenuProduct.of(2L, menu2, product2, 1);
        menuProductRequest = new MenuProductRequest(1L, 1);
        menuRequest = new MenuRequest("후라이드치킨", new BigDecimal(16000.00), 1L, Arrays.asList(menuProductRequest));
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        // given
        when(menuGroupRepository.findById(1L)).thenReturn(Optional.of(menuGroup));
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(menuRepository.save(any())).thenReturn(menu);

        // when
        Menu expected = menuService.create(menuRequest);

        // then
        assertThat(menu.getId()).isEqualTo(expected.getId());
    }

    @DisplayName("메뉴그룹없이 메뉴를 등록할 수 없다.")
    @Test
    void create2() {
        //then
        assertThatThrownBy(
                () -> menuService.create(menuRequest)
        ).isInstanceOf(MenuGroupNotFoundException.class);
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
