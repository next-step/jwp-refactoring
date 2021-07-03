package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    private Menu menu;
    private Product product;
    private MenuGroup menuGroup;
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
        menuGroup.setId(1L);
        menuGroup.setName("반반시리즈");

        menu = new Menu();
        menu.setId(1L);
        menu.setName("간장레드반반치킨");
        menu.setPrice(new BigDecimal(18000));
        menu.setMenuGroupId(menuGroup.getId());

        product = new Product();
        product.setId(1L);
        product.setName("반반콤보");
        product.setPrice(new BigDecimal(18000));

        menuProduct = new MenuProduct();
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setSeq(1L);
        menuProduct.setQuantity(10L);

        menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);

        menu.setMenuProducts(menuProducts);
    }

    @DisplayName("메뉴의 가격이 0원 이상일때 등록이 가능하다.")
    @Test
    void create_0원_이하_예외() {
        menu.setPrice(new BigDecimal(-1));

        assertThatThrownBy(() -> menuService.create(this.menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴그룹 목록 중 하나에 속해있어야 등록이 가능하다.")
    @Test
    void create_메뉴그룹_아닐경우_예외() {
        menu.setMenuGroupId(3L);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 속한 상품 금액의 합은 메뉴의 가격보다 크거나 같아야 한다.")
    @Test
    void create_메뉴_가격_예외() {
        Menu 비싼가격메뉴 = new Menu();
        비싼가격메뉴.setId(1L);
        비싼가격메뉴.setPrice(new BigDecimal("500000000"));
        비싼가격메뉴.setMenuGroupId(menuGroup.getId());
        비싼가격메뉴.setMenuProducts(menuProducts);

        given(menuGroupDao.existsById(this.menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));

        // when then
        assertThatThrownBy(() -> menuService.create(비싼가격메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("1개 이상 등록된 상품으로 메뉴를 등록한다.")
    @Test
    void create() {
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));
        given(menuProductDao.save(menuProduct)).willReturn(menuProduct);
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(menuDao.save(menu)).willReturn(menu);

        Menu createdMenu = menuService.create(this.menu);

        assertThat(createdMenu.getId()).isEqualTo(this.menu.getId());
        assertThat(createdMenu.getName()).isEqualTo(this.menu.getName());
        assertThat(createdMenu.getPrice()).isEqualTo(this.menu.getPrice());
        assertThat(createdMenu.getMenuGroupId()).isEqualTo(this.menu.getMenuGroupId());
        assertThat(createdMenu.getMenuProducts()).isEqualTo(this.menu.getMenuProducts());
    }

    @DisplayName("메뉴의 목록을 조회한다.")
    @Test
    void searchList() {
        given(menuDao.findAll()).willReturn(Arrays.asList(menu));

        List<Menu> menus = menuService.list();

        assertThat(menus).containsExactly(menu);
        assertThat(menus.get(0).getId()).isEqualTo(menu.getId());
        assertThat(menus.get(0).getMenuProducts()).isEqualTo(menu.getMenuProducts());
        assertThat(menus.get(0).getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        assertThat(menus.get(0).getName()).isEqualTo(menu.getName());
        assertThat(menus.get(0).getPrice()).isEqualTo(menu.getPrice());
    }
}
