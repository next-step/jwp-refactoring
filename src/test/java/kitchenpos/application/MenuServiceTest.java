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

import static kitchenpos.application.ProductServiceTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    private Product product;
    private MenuGroup menuGroup;
    private MenuProduct menuProduct;
    private List<MenuProduct> menuProducts = new ArrayList<>();

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
        menuGroup = 메뉴_그룹_생성(1L, "간장시리즈");

        product = 상품_생성(1L, "한마리치킨", new BigDecimal(15000));
    }

    @DisplayName("메뉴의 가격이 0원 이상일때 등록이 가능하다.")
    @Test
    void create_0원_이하_예외() {
        Menu menu = 가격_0원_이하인_메뉴_생성();

        가격_0원_이하인_메뉴_생성시_예외_발생함(menu);
    }

    @DisplayName("존재하는 메뉴 그룹을 입력해야 등록이 가능하다.")
    @Test
    void create_메뉴그룹_아닐경우_예외() {
        Menu menu = 존재하지_않는_메뉴그룹으로_메뉴_생성();

        존재하지_않는_메뉴그룹으로_메뉴_생성시_예외_발생함(menu);
    }

    @DisplayName("메뉴에 속한 상품 금액의 합은 메뉴의 가격보다 크거나 같아야 한다.")
    @Test
    void create_메뉴_가격_예외() {
        Menu menu = 메뉴에_속한_상품보다_비싼_메뉴_가격_생성();

        메뉴_그룹_존재함();

        메뉴에_속한_상품보다_비싼_메뉴_가격_생성시_예외_발생(menu);
    }

    @DisplayName("1개 이상 등록된 상품으로 메뉴를 등록한다.")
    @Test
    void create() {
        Menu menu = 메뉴_상품이_존재하는_메뉴_생성();

//        메뉴_생성에_필요한_값_설정(menu);
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));
        given(menuProductDao.save(menuProduct)).willReturn(menuProduct);
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(menuDao.save(menu)).willReturn(menu);
        Menu createdMenu = 메뉴_생성_요청(menu);

        메뉴_생성됨(createdMenu, menu);
    }

    @DisplayName("메뉴의 목록을 조회한다.")
    @Test
    void searchList() {
        Menu menu = 메뉴_상품이_존재하는_메뉴_생성();

        메뉴_목록_조회에_필요한_값_설정(menu);

        List<Menu> menus = 메뉴_목록_조회_요청();

        메뉴_목록_조회됨(menus, menu);
    }

    private MenuGroup 메뉴_그룹_생성(Long id, String name) {
        return new MenuGroup(id, name);
    }

    private MenuProduct 메뉴_상품_생성(Long seq, Long menuId, Long productId, Long quantity) {
        return new MenuProduct(seq, menuId, productId, quantity);
    }

    private void 메뉴_상품_목록_추가(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    private Menu 메뉴_생성(Long id, String name, BigDecimal price, Long menuGroupId) {
        return new Menu(id, name, price, menuGroupId);
    }

    private Menu 가격_0원_이하인_메뉴_생성() {
        return 메뉴_생성(1L, "간장소스치킨", new BigDecimal(-10), menuGroup.getId());
    }

    private void 가격_0원_이하인_메뉴_생성시_예외_발생함(Menu menu) {
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void 존재하지_않는_메뉴그룹으로_메뉴_생성시_예외_발생함(Menu menu) {
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Menu 존재하지_않는_메뉴그룹으로_메뉴_생성() {
        return 메뉴_생성(1L, "메뉴생성", new BigDecimal(18000), 3L);
    }

    private void 메뉴에_속한_상품보다_비싼_메뉴_가격_생성시_예외_발생(Menu menu) {
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void 메뉴_그룹_존재함() {
        given(menuGroupDao.existsById(menuGroup.getId())).willReturn(true);
    }

    private Menu 메뉴에_속한_상품보다_비싼_메뉴_가격_생성() {
        Menu menu = new Menu(3L, "레드허콤보", new BigDecimal("200000"), menuGroup.getId());
        menuProduct = 메뉴_상품_생성(1L, menu.getId(), product.getId(), 10L);
        menuProducts.add(menuProduct);
        menu.updateMenuProducts(menuProducts);
        return menu;
    }

    private void 메뉴_생성됨(Menu createdMenu, Menu menu) {
        assertThat(createdMenu.getId()).isEqualTo(menu.getId());
        assertThat(createdMenu.getName()).isEqualTo(menu.getName());
        assertThat(createdMenu.getPrice()).isEqualTo(menu.getPrice());
        assertThat(createdMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        assertThat(createdMenu.getMenuProducts()).isEqualTo(menu.getMenuProducts());
    }

    private Menu 메뉴_생성_요청(Menu menu) {
        return menuService.create(menu);
    }

    private void 메뉴_생성에_필요한_값_설정(Menu menu) {
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));
        given(menuProductDao.save(menuProduct)).willReturn(menuProduct);
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(menuDao.save(menu)).willReturn(menu);
    }

    private void 메뉴_목록_조회에_필요한_값_설정(Menu menu) {
        given(menuDao.findAll()).willReturn(Arrays.asList(menu));
    }

    private void 메뉴_목록_조회됨(List<Menu> menus, Menu menu) {
        assertThat(menus).containsExactly(menu);
        assertThat(menus.get(0).getId()).isEqualTo(menu.getId());
        assertThat(menus.get(0).getMenuProducts()).isEqualTo(menu.getMenuProducts());
        assertThat(menus.get(0).getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        assertThat(menus.get(0).getName()).isEqualTo(menu.getName());
        assertThat(menus.get(0).getPrice()).isEqualTo(menu.getPrice());
    }

    private List<Menu>  메뉴_목록_조회_요청() {
        return menuService.list();
    }

    private Menu 메뉴_상품이_존재하는_메뉴_생성() {
        Menu menu = 메뉴_생성(1L, "간장소스치킨", new BigDecimal(15000), menuGroup.getId());
        menuProduct = 메뉴_상품_생성(1L, menu.getId(), product.getId(), 1L);
        메뉴_상품_목록_추가(menuProduct);
        menu.updateMenuProducts(menuProducts);
        return menu;
    }
}
