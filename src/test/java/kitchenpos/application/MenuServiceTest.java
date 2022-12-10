package kitchenpos.application;

import static kitchenpos.domain.MenuFixture.*;
import static kitchenpos.domain.MenuProductFixture.*;
import static kitchenpos.domain.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴 등록 API - 가격 없음")
    @Test
    void create_price_null() {
        // given
        MenuProduct menuProduct = menuProductParam(1L, 2L);
        Menu menu = menuParam("후라이드+후라이드", null, 1L, Collections.singletonList(menuProduct));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 API - 가격 0원")
    @Test
    void create_price_zero() {
        // given
        MenuProduct menuProduct = menuProductParam(1L, 2L);
        Menu menu = menuParam("후라이드+후라이드", BigDecimal.ZERO, 1L, Collections.singletonList(menuProduct));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 API - 메뉴 그룹 존재 X")
    @Test
    void create_menuGroup_notExists() {
        // given
        MenuProduct menuProduct = menuProductParam(1L, 2L);
        Menu menu = menuParam("후라이드+후라이드", new BigDecimal(17000), 1L, Collections.singletonList(menuProduct));
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 API - 메뉴 상품 존재 X")
    @Test
    void create_product_notExists() {
        // given
        MenuProduct menuProduct = menuProductParam(1L, 2L);
        Menu menu = menuParam("후라이드+후라이드", new BigDecimal(17000), 1L, Collections.singletonList(menuProduct));
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 API - 메뉴 상품 가격, 상품 가격 합계 초과")
    @Test
    void create_product_price_invalid() {
        // given
        MenuProduct menuProduct = menuProductParam(1L, 2L);
        Menu menu = menuParam("후라이드+후라이드", new BigDecimal(17000), 1L, Collections.singletonList(menuProduct));
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        Product savedProduct = savedProduct(1L, new BigDecimal(5000));
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(savedProduct));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 API")
    @Test
    void create() {
        // given
        MenuProduct menuProduct = menuProductParam(1L, 2L);
        Menu menu = menuParam("후라이드+후라이드", new BigDecimal(17000), 1L, Collections.singletonList(menuProduct));
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        Product savedProduct = savedProduct(1L, new BigDecimal(10000));
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(savedProduct));
        Menu savedMenu = savedMenu(1L, menu);
        given(menuDao.save(menu)).willReturn(savedMenu);
        MenuProduct savedMenuProduct = savedMenuProduct(1L, menu.getId(), menuProduct);
        given(menuProductDao.save(menuProduct)).willReturn(savedMenuProduct);

        // when
        Menu actual = menuService.create(menu);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(savedMenu.getName()),
            () -> assertThat(actual.getPrice()).isEqualTo(savedMenu.getPrice()),
            () -> assertThat(actual.getMenuGroupId()).isEqualTo(savedMenu.getMenuGroupId()),
            () -> assertThat(actual.getMenuProducts()).containsExactly(savedMenuProduct)
        );
    }

    @Test
    void list() {
        // given
        Menu savedMenu = savedMenu(1L, "메뉴", new BigDecimal(13000), 1L);
        given(menuDao.findAll()).willReturn(Arrays.asList(savedMenu));
        MenuProduct menuProduct1 = savedMenuProduct(1L, savedMenu.getId(), 1L, 2L);
        MenuProduct menuProduct2 = savedMenuProduct(2L, savedMenu.getId(), 2L, 3L);
        given(menuProductDao.findAllByMenuId(savedMenu.getId())).willReturn(Arrays.asList(menuProduct1, menuProduct2));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).hasSize(1);
        assertThat(menus.get(0).getName()).isEqualTo(savedMenu.getName());
        assertThat(menus.get(0).getMenuProducts()).containsExactly(menuProduct1, menuProduct2);
    }
}
