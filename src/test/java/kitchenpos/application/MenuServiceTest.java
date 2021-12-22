package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        // given
        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(16_000));

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(1);
        menu.setMenuProducts(new ArrayList<>(Arrays.asList(menuProduct)));

        Product product = new Product();
        product.setPrice(new BigDecimal(16_000));

        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.ofNullable(product));
        given(menuDao.save(any())).willReturn(menu);
        given(menuProductDao.save(any())).willReturn(menuProduct);

        // when
        Menu result = menuService.create(menu);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPrice()).isEqualTo(menu.getPrice());
        assertThat(result.getMenuProducts().size()).isEqualTo(menu.getMenuProducts().size());
    }

    @Test
    @DisplayName("메뉴의 가격이 null 또는 0이면 등록에 실패한다.")
    void create_price_fail() {
        // given
        Menu 가격이_null = new Menu();
        Menu 가격이_0원 = new Menu();
        가격이_0원.setPrice(new BigDecimal(0));

        // when, then
        assertThatThrownBy(() -> menuService.create(가격이_null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> menuService.create(가격이_0원))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴그룹ID이면 등록에 실패한다.")
    void create_not_exist_menu_grourp_id() {
        // given
        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(16_000));

        given(menuGroupDao.existsById(any())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 상품이면 등록에 실패한다.")
    void create_not_exist_product() {
        // given
        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(16_000));

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(1);
        menu.setMenuProducts(new ArrayList<>(Arrays.asList(menuProduct)));

        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 메뉴 상품 가격들의 합보다 크면 등록에 실패한다.")
    void create_price_greater_than_sum() {
        // given
        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(16_000));

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(1);
        menu.setMenuProducts(new ArrayList<>(Arrays.asList(menuProduct)));

        Product product = new Product();
        product.setPrice(new BigDecimal(0));

        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.ofNullable(product));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void list() {
        // given
        List<Menu> menus = new ArrayList<>(Arrays.asList(new Menu(), new Menu()));
        List<MenuProduct> menuProducts = new ArrayList<>();

        given(menuDao.findAll()).willReturn(menus);
        given(menuProductDao.findAllByMenuId(any())).willReturn(menuProducts);

        // when
        List<Menu> result = menuService.list();

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(menus.size());
    }
}
