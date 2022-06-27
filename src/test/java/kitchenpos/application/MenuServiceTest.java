package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    Long menuGroupId;
    Long productId1;
    Long productId2;
    Product product1;
    Product product2;

    @BeforeEach
    void setUp() {
        setMenuGroup();
        setProduct();
    }

    void setMenuGroup() {
        menuGroupId = 1L;
        when(menuGroupDao.existsById(menuGroupId)).thenReturn(true);
    }

    void setProduct() {
        productId1 = 1L;
        productId2 = 2L;
        product1 = new Product(productId1, "짜장면", Price.valueOf(6000));
        product2 = new Product(productId2, "짬뽕", Price.valueOf(7000));

        when(productDao.findById(productId1)).thenReturn(Optional.of(product1));
        when(productDao.findById(productId2)).thenReturn(Optional.of(product2));
    }

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void create() {
        // given
        MenuProduct menuProduct1 = new MenuProduct(1L, null, productId1, 2);
        MenuProduct menuProduct2 = new MenuProduct(2L, null, productId2, 1);
        Menu menu = new Menu("메뉴", Price.valueOf(19000), menuGroupId, Arrays.asList(menuProduct1, menuProduct2));
        when(menuDao.save(menu)).thenReturn(new Menu(1L, "메뉴", Price.valueOf(19000), menuGroupId));
        when(menuProductDao.save(menuProduct1)).thenReturn(menuProduct1);
        when(menuProductDao.save(menuProduct2)).thenReturn(menuProduct2);

        // when
        Menu actual = menuService.create(menu);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isNotNull();
            softAssertions.assertThat(actual.getMenuProducts()).containsExactly(menuProduct1, menuProduct2);
        });
    }

    @DisplayName("메뉴의 가격은 0원 이상이어야 한다")
    @Test
    void price_more_then_0() {
        // given
        Menu minusPrice = new Menu("세트1", Price.valueOf(-1), menuGroupId);

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
        Menu menu = new Menu("메뉴", Price.valueOf(15000), menuGroupId, singletonList(notExistsMenuProduct));

        // when then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 메뉴 상품의 금액의 합을 초과할 수 없다")
    @Test
    void price_less_then_products() {
        // given
        MenuProduct menuProduct1 = new MenuProduct(1L, null, productId1, 2); // 6000 * 2
        MenuProduct menuProduct2 = new MenuProduct(2L, null, productId2, 1); // 7000 * 1
        Menu priceMore = new Menu("메뉴", Price.valueOf(19001), menuGroupId, Arrays.asList(menuProduct1, menuProduct2));

        // when then
        assertThatThrownBy(() -> menuService.create(priceMore))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴그룹에 속해야 한다")
    @Test
    void menu_group_is_not_null() {
        // given
        Menu nullMenuGroup = new Menu("세트1", Price.ZERO, null);

        // when then
        assertThatThrownBy(() -> menuService.create(nullMenuGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void list() {
        // when
        Long menuId = 1L;
        when(menuDao.findAll()).thenReturn(singletonList(new Menu(menuId, "메뉴", Price.valueOf(19000), menuGroupId)));
        MenuProduct menuProduct1 = new MenuProduct(1L, null, productId1, 2);
        MenuProduct menuProduct2 = new MenuProduct(2L, null, productId2, 1);
        when(menuProductDao.findAllByMenuId(menuId)).thenReturn(Arrays.asList(menuProduct1, menuProduct2));
        List<Menu> menus = menuService.list();

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(menus).hasSize(1);
            softAssertions.assertThat(menus.get(0).getMenuProducts()).containsExactly(menuProduct1, menuProduct2);
        });
    }
}
