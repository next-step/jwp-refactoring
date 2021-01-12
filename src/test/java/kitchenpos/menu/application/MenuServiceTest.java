package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.menu.domain.Menu;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 비즈니스 로직을 처리하는 서비스 테스트")
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

    private MenuProduct menuProduct;
    private List<MenuProduct> menuProducts;
    private Menu menu;
    private Product product;

    @BeforeEach
    void setUp() {
        final Long menu = 1L;
        final Long menuGroupId = 1L;
        final String menuName = "두마리메뉴";
        final BigDecimal price = new BigDecimal("29000");

        menuProduct = new MenuProduct();
        menuProduct.setMenuId(menu);
        menuProduct.setProductId(1L);
        menuProduct.setSeq(1L);
        menuProduct.setQuantity(2L);

        menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);

        this.menu = new Menu();
        this.menu.setId(menu);
        this.menu.setName(menuName);
        this.menu.setPrice(price);
        this.menu.setMenuGroupId(menuGroupId);
        this.menu.setMenuProducts(menuProducts);

        product = new Product();
        product.setId(1L);
        product.setName("후라이드");
        product.setPrice(new BigDecimal("16000"));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    public void 메뉴_생성() {
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));
        given(menuDao.save(menu)).willReturn(menu);
        given(menuProductDao.save(menuProduct)).willReturn(menuProduct);

        final Menu createdMenu = menuService.create(menu);

        assertThat(createdMenu.getId()).isEqualTo(menu.getId());
        assertThat(createdMenu.getName()).isEqualTo(menu.getName());
        assertThat(createdMenu.getPrice()).isEqualTo(menu.getPrice());
        assertThat(createdMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
    }

    @DisplayName("메뉴 가격이 음수인 경우 메뉴를 생성할 수 없다.")
    @Test
    void 가격이_음수인_경우_메뉴_생성() {
        menu.setPrice(new BigDecimal(-1));

        assertThatThrownBy(() -> {
            final Menu createdMenu = menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴 그룹인 경우 메뉴를 생성할 수 없다.")
    @Test
    void 존재하지_않는_메뉴_그룹인_경우_메뉴_생성() {
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(false);

        assertThatThrownBy(() -> {
            final Menu createdMenu = menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 상품인 경우 메뉴를 생성할 수 없다.")
    @Test
    void 존재하지_않는_상품인_경우_메뉴_생성() {
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            final Menu createdMenu = menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴에 속한 제품의 가격과 수량을 곱하여 합산한 총액보다 큰 경우 메뉴를 생성할 수 없다.")
    @Test
    void 메뉴_가격이_제품_총액보다_큰_경우_메뉴_생성() {
        menu.getMenuProducts().get(0).setQuantity(1L);

        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));

        assertThatThrownBy(() -> {
            final Menu createdMenu = menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    void 메뉴_조회() {
        given(menuDao.findAll()).willReturn(Collections.singletonList(menu));
        given(menuProductDao.findAllByMenuId(menu.getId())).willReturn(menu.getMenuProducts());

        final List<Menu> responseMenus = menuService.list();
        final List<MenuProduct> menuProducts = responseMenus.stream()
            .flatMap(menu -> menu.getMenuProducts().stream())
            .collect(Collectors.toList());

        assertThat(responseMenus.get(0).getId()).isEqualTo(menuProduct.getMenuId());
        assertThat(menuProducts.get(0).getMenuId()).isEqualTo(menuProduct.getMenuId());
        assertThat(menuProducts.get(0).getQuantity()).isEqualTo(menuProduct.getQuantity());
    }
}
