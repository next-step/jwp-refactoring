package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.*;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 기능 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    private Product product;
    private MenuGroup menuGroup;
    private Menu menu;
    private MenuProduct menuProduct;
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
        menuGroup = new MenuGroup();
        menuGroup.setId(2L);
        menuGroup.setName("한마리메뉴");

        menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드치킨");
        menu.setPrice(new BigDecimal(16000));
        menu.setMenuGroupId(menuGroup.getId());

        product = new Product();
        product.setId(1L);
        product.setName("후라이드");
        product.setPrice(new BigDecimal(16000));

        menuProduct = new MenuProduct();
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setSeq(1L);
        menuProduct.setQuantity(99L);

        menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);

        menu.setMenuProducts(menuProducts);
    }

    @Test
    @DisplayName("1 개 이상의 등록된 상품으로 메뉴를 등록할 수 있다.")
    public void create() throws Exception {
        // given
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));
        given(menuDao.save(menu)).willReturn(menu);
        given(menuProductDao.save(menuProduct)).willReturn(menuProduct);

        // when
        Menu createdMenu = menuService.create(this.menu);

        // then
        assertThat(createdMenu.getId()).isEqualTo(this.menu.getId());
        assertThat(createdMenu.getName()).isEqualTo(this.menu.getName());
        assertThat(createdMenu.getPrice()).isEqualTo(this.menu.getPrice());
        assertThat(createdMenu.getMenuGroupId()).isEqualTo(this.menu.getMenuGroupId());
        assertThat(createdMenu.getMenuProducts()).isEqualTo(this.menu.getMenuProducts());
    }

    @Test
    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다 : 메뉴의 가격은 0 원 이상이어야 한다.")
    public void createFail1() throws Exception {
        // given
        menu.setPrice(new BigDecimal(-1));

        // when then
        assertThatThrownBy(() -> menuService.create(this.menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다 : 메뉴에 속한 상품 금액의 합은 메뉴의 가격보다 크거나 같아야 한다.")
    public void createFail2() throws Exception {
        // given
        Menu moreExpensiveMenu = new Menu();
        moreExpensiveMenu.setId(1L);
        moreExpensiveMenu.setPrice(new BigDecimal("1e9"));
        moreExpensiveMenu.setMenuGroupId(menuGroup.getId());
        moreExpensiveMenu.setMenuProducts(menuProducts);

        given(menuGroupDao.existsById(this.menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));

        // when then
        assertThatThrownBy(() -> menuService.create(moreExpensiveMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴는 특정 메뉴 그룹에 속해야 한다.")
    public void menuInMenuGroup() throws Exception {
        // given
        menu.setMenuGroupId(null);

        // when then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 목록을 조회할 수 있다.")
    public void list() throws Exception {
        // given
        given(menuDao.findAll()).willReturn(Arrays.asList(menu));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).containsExactly(menu);
        assertThat(menus.get(0).getId()).isEqualTo(menu.getId());
        assertThat(menus.get(0).getName()).isEqualTo(menu.getName());
        assertThat(menus.get(0).getPrice()).isEqualTo(menu.getPrice());
        assertThat(menus.get(0).getMenuProducts()).isEqualTo(menu.getMenuProducts());
        assertThat(menus.get(0).getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
    }
}
