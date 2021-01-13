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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private Menu menu1;
    private Menu menu2;
    private Menu menu3;
    private Product product;
    private List<Menu> menus = new ArrayList<>();
    private List<MenuProduct> menuProducts = new ArrayList<>();

    private MenuService menuService;

    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;

    @BeforeEach
    public void setUp() {
        product = new Product("테스트상품",new BigDecimal(20000));
        menuProducts.add(new MenuProduct(2L,1));
        menuProducts.add(new MenuProduct(3L,1));
        menu1 = new Menu("강정치킨", new BigDecimal(17000),1L, menuProducts);
        menu2 = new Menu("양념치킨", new BigDecimal(18000),2L, menuProducts);
        menu3 = new Menu("후라이드치킨", new BigDecimal(16000),3L, menuProducts);
        menus.add(menu1);
        menus.add(menu2);
        menus.add(menu3);
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @DisplayName("메뉴 등록 테스트")
    @Test
    void createMenu() {
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(java.util.Optional.ofNullable(product));
        mockSaveMenu(menu1);
        mockSaveMenu(menu2);
        mockSaveMenu(menu3);
        mockSaveMenuProduct(menuProducts.get(0));
        mockSaveMenuProduct(menuProducts.get(1));

        checkMenu(menuService.create(menu1), menu1);
        checkMenu(menuService.create(menu2), menu2);
        checkMenu(menuService.create(menu3), menu3);
    }

    @DisplayName("메뉴목록 조회 테스트")
    @Test
    void findMenuList() {
        when(menuDao.findAll()).thenReturn(menus);
        when(menuProductDao.findAllByMenuId(any())).thenReturn(menuProducts);

        List<Menu> resultMenus = menuService.list();

        assertThat(resultMenus.size()).isEqualTo(menus.size());
        List<String> menuNames =resultMenus.stream()
                .map(menu -> menu.getName())
                .collect(Collectors.toList());
        List<String> expectedMenuNames =menus.stream()
                .map(menu -> menu.getName())
                .collect(Collectors.toList());

        assertThat(menuNames).containsExactlyElementsOf(expectedMenuNames);

    }

    private void checkMenu(Menu resultMenu, Menu menu) {
        assertThat(resultMenu.getName()).isEqualTo(menu.getName());
        assertThat(resultMenu.getPrice()).isEqualTo(menu.getPrice());
        assertThat(resultMenu.getMenuProducts().size()).isEqualTo(menu.getMenuProducts().size());
        assertThat(resultMenu.getMenuProducts().get(0).getProductId()).isEqualTo(menu.getMenuProducts().get(0).getProductId());
    }

    private void mockSaveMenuProduct(MenuProduct menuProduct) {
        when(menuProductDao.save(menuProduct)).thenReturn(menuProduct);
    }

    private void mockSaveMenu(Menu menu) {
        when(menuDao.save(menu)).thenReturn(menu);
    }
}