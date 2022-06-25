package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 관련")
@SpringBootTest
class MenuServiceTest {
    @Autowired
    MenuService menuService;
    @MockBean
    MenuDao menuDao;
    @MockBean
    MenuGroupDao menuGroupDao;
    @MockBean
    MenuProductDao menuProductDao;
    @MockBean
    ProductDao productDao;

    Long 짜장면id = 1L;
    Long 짬뽕id = 2L;
    Product 짜장면 = new Product(짜장면id, "짜장면", BigDecimal.valueOf(6000));
    Product 짬뽕 = new Product(짬뽕id, "짬뽕", BigDecimal.valueOf(7000));
    MenuProduct 메뉴_짜장면_2 = new MenuProduct(1L, null, 짜장면id, 2);
    MenuProduct 메뉴_짬뽕_1 = new MenuProduct(2L, null, 짬뽕id, 1);
    Long menuGroupId = 1L;

    @BeforeEach
    void setUp() {
        when(menuGroupDao.existsById(menuGroupId)).thenReturn(true);
        when(productDao.findById(짜장면id)).thenReturn(Optional.of(짜장면));
        when(productDao.findById(짬뽕id)).thenReturn(Optional.of(짬뽕));
    }

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void create() {
        // given
        Menu 메뉴 = new Menu("메뉴", BigDecimal.valueOf(19000), menuGroupId, Arrays.asList(메뉴_짜장면_2, 메뉴_짬뽕_1));
        when(menuDao.save(메뉴)).thenReturn(new Menu(1L, "메뉴", BigDecimal.valueOf(19000), menuGroupId));
        when(menuProductDao.save(메뉴_짜장면_2)).thenReturn(메뉴_짜장면_2);
        when(menuProductDao.save(메뉴_짬뽕_1)).thenReturn(메뉴_짬뽕_1);

        // when
        Menu actual = menuService.create(메뉴);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isNotNull();
            softAssertions.assertThat(actual.getMenuProducts()).containsExactly(메뉴_짜장면_2, 메뉴_짬뽕_1);
        });
    }

    @DisplayName("메뉴의 가격은 0원 이상이어야 한다")
    @Test
    void price_more_then_0() {
        // given
        Menu minusPrice = new Menu("세트1", BigDecimal.valueOf(-1), menuGroupId);

        // when then
        assertThatThrownBy(() -> menuService.create(minusPrice))
                        .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("없는 메뉴 상품을 등록할 수 없다")
    @Test
    void product_is_exists() {
        // given
        Long notExistsProductId = 1000L;
        MenuProduct notExistsMenuProduct = new MenuProduct(1L, null, notExistsProductId, 1);
        Menu menu = new Menu("메뉴", BigDecimal.valueOf(15000), menuGroupId, singletonList(notExistsMenuProduct));

        // when then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 메뉴 상품의 금액의 합을 초과할 수 없다")
    @Test
    void price_less_then_products() {
        // given
        Menu priceMore = new Menu("메뉴", BigDecimal.valueOf(20000), menuGroupId, Arrays.asList(메뉴_짜장면_2, 메뉴_짬뽕_1));

        // when then
        assertThatThrownBy(() -> menuService.create(priceMore))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴그룹에 속해야 한다")
    @Test
    void menu_group_is_not_null() {
        // given
        Menu nullMenuGroup = new Menu("세트1", BigDecimal.ZERO, null);

        // when then
        assertThatThrownBy(() -> menuService.create(nullMenuGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void list() {
        // when
        Long menuId = 1L;
        when(menuDao.findAll()).thenReturn(singletonList(new Menu(menuId, "메뉴", BigDecimal.valueOf(19000), menuGroupId)));
        when(menuProductDao.findAllByMenuId(menuId)).thenReturn(Arrays.asList(메뉴_짜장면_2, 메뉴_짬뽕_1));
        List<Menu> menus = menuService.list();

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(menus).hasSize(1);
            softAssertions.assertThat(menus.get(0).getMenuProducts()).containsExactly(메뉴_짜장면_2, 메뉴_짬뽕_1);
        });
    }
}
