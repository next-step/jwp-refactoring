package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixtures.MenuFixtures;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixtures.MenuGroupFixtures.createMenuGroup;
import static kitchenpos.fixtures.MenuProductFixtures.createMenuProduct;
import static kitchenpos.fixtures.MenuProductFixtures.createMenuProducts;
import static kitchenpos.fixtures.ProductFixtures.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

/**
 * packageName : kitchenpos.application
 * fileName : MenuServiceTest
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
@DisplayName("메뉴 비즈니스 오브젝트 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    private Menu menu;
    private Product product;
    private List<MenuProduct> menuProducts;

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

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = createMenuGroup(1L, "두마리메뉴");
        product = createProduct(1L, "고추바사삭치킨", new BigDecimal(18_000));
        MenuProduct menuProduct = createMenuProduct(1L, null, product.getId(), 1L);
        menuProducts = createMenuProducts(menuProduct, menuProduct);
        menu = MenuFixtures.createMenu(1L, "두마리메뉴", new BigDecimal(36_000), menuGroup.getId(), menuProducts);
    }

    @Test
    @DisplayName("메뉴를 조회할 수 있다.")
    public void list() {
        // given
        given(menuDao.findAll()).willReturn(Lists.newArrayList(menu));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).hasSize(1);
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    public void create() {
        // given
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.ofNullable(product));
        given(menuDao.save(menu)).willReturn(menu);
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(menuProducts.get(0), menuProducts.get(1));

        // when
        Menu actual = menuService.create(menu);

        // then
        assertAll(
                () -> assertThat(actual).isEqualTo(menu),
                () -> assertThat(actual.getMenuProducts()).hasSize(2),
                () -> assertThat(actual.getMenuProducts()).extracting(MenuProduct::getMenuId)
                        .containsOnly(menu.getId())
        );
    }

    @ParameterizedTest(name = "value: " + ParameterizedTest.ARGUMENTS_PLACEHOLDER)
    @ValueSource(ints = {-10, -5, -1})
    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다: int")
    public void createFailByPrice(int candidate) {
        // when
        menu.setPrice(new BigDecimal(candidate));

        // then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다: null")
    public void createFailByPriceNull() {
        //when
        menu.setPrice(null);

        // then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격은 메뉴상품들의 수량과 가격의 합과 일치하여야 한다.")
    public void createFailByMenusPrices() {
        // given
        given(productDao.findById(anyLong())).willReturn(Optional.ofNullable(product));
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        menu.setPrice(new BigDecimal(37_000));

        // then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴그룹이 등록되어 있어야 한다.")
    public void createFail() {
        // given
        given(menuGroupDao.existsById(anyLong())).willReturn(false);

        // then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴상품은 상품이 등록되어 있어야 한다.")
    public void createFailByMenuProduct() {
        // given
        given(menuGroupDao.existsById(anyLong())).willReturn(true);

        // then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }
}
