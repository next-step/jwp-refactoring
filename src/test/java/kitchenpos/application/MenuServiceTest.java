package kitchenpos.application;

import static kitchenpos.application.MenuGroupServiceTest.createMenuGroup;
import static kitchenpos.application.ProductServiceTest.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

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
    private MenuGroup 빅맥세트;
    private Product 감자;
    private Product 토마토;
    private Product 양상추;
    private Menu 빅맥버거;
    private Menu 감자튀김;

    @BeforeEach
    void setUp() {
        빅맥세트 = createMenuGroup(1L, "빅맥세트");
        감자 = createProduct(1L, "감자", 1000);
        토마토 = createProduct(2L, "토마토", 1000);
        양상추 = createProduct(3L, "양상추", 500);

        빅맥버거 = createMenu(1L, "빅맥버거", 3000, 1L,
                Arrays.asList(createMenuProduct(1L, 1L, 토마토.getId(), 1), createMenuProduct(2L, 1L, 양상추.getId(), 4)));
        감자튀김 = createMenu(2L, "감자튀김", 3000, 1L,
                Arrays.asList(createMenuProduct(1L, 1L, 감자.getId(), 2)));
    }

    @Test
    void 메뉴_생성_가격_없음_예외() {
        Menu 가격없는메뉴 = createEmptyPriceMenu(3L, "가격없는메뉴", 1L,
                Arrays.asList(createMenuProduct(1L, 1L, 토마토.getId(), 1), createMenuProduct(2L, 1L, 양상추.getId(), 4)));
        assertThatThrownBy(
                () -> menuService.create(가격없는메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성_가격_0_미만_예외() {
        Menu 가격음수메뉴 = createMenu(3L, "가격음수메뉴", -1000, 1L,
                Arrays.asList(createMenuProduct(1L, 1L, 토마토.getId(), 1), createMenuProduct(2L, 1L, 양상추.getId(), 4)));

        assertThatThrownBy(
                () -> menuService.create(가격음수메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성_존재하지_않는_메뉴그룹_예외() {
        when(menuGroupDao.existsById(100L)).thenReturn(false);
        Menu 메뉴그룹없음 = createMenu(3L, "메뉴그룹없음", 1000, 100L,
                Arrays.asList(createMenuProduct(1L, 1L, 토마토.getId(), 1), createMenuProduct(2L, 1L, 양상추.getId(), 4)));

        assertThatThrownBy(
                () -> menuService.create(메뉴그룹없음)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성_존재하지_않는_상품_예외() {
        when(menuGroupDao.existsById(빅맥세트.getId())).thenReturn(true);
        when(productDao.findById(토마토.getId())).thenReturn(Optional.ofNullable(토마토));
        when(productDao.findById(양상추.getId())).thenThrow(IllegalArgumentException.class);

        Menu 상품없음 = createMenu(3L, "상품없음", 1000, 빅맥세트.getId(),
                Arrays.asList(createMenuProduct(1L, 빅맥버거.getId(), 토마토.getId(), 1),
                        createMenuProduct(2L, 빅맥버거.getId(), 양상추.getId(), 4)));

        assertThatThrownBy(
                () -> menuService.create(상품없음)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성_메뉴_가격이_상품_가격_합_보다_큼_예외() {
        when(menuGroupDao.existsById(빅맥세트.getId())).thenReturn(true);
        when(productDao.findById(토마토.getId())).thenReturn(Optional.ofNullable(토마토));
        when(productDao.findById(양상추.getId())).thenReturn(Optional.ofNullable(양상추));

        Menu 메뉴가격큼 = createMenu(3L, "메뉴가격큼", 5000, 1L,
                Arrays.asList(createMenuProduct(1L, 1L, 토마토.getId(), 1), createMenuProduct(2L, 1L, 양상추.getId(), 4)));

        assertThatThrownBy(
                () -> menuService.create(메뉴가격큼)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성() {
        when(menuGroupDao.existsById(빅맥세트.getId())).thenReturn(true);
        when(productDao.findById(토마토.getId())).thenReturn(Optional.ofNullable(토마토));
        when(productDao.findById(양상추.getId())).thenReturn(Optional.ofNullable(양상추));
        when(menuDao.save(빅맥버거)).thenReturn(빅맥버거);

        Menu result = menuService.create(빅맥버거);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(빅맥버거.getId()),
                () -> assertThat(result.getName()).isEqualTo(빅맥버거.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(빅맥버거.getPrice())
        );
    }

    @Test
    void 메뉴_목록() {
        when(menuDao.findAll()).thenReturn(Arrays.asList(빅맥버거, 감자튀김));

        List<Menu> result = menuService.list();
        assertThat(toIdList(result)).containsExactlyElementsOf(toIdList(Arrays.asList(빅맥버거, 감자튀김)));
    }

    private Menu createMenu(Long id, String name, long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    private Menu createEmptyPriceMenu(Long id, String name, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(null);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    private MenuProduct createMenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    private List<Long> toIdList(List<Menu> menus) {
        return menus.stream()
                .map(Menu::getId)
                .collect(Collectors.toList());
    }
}