package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    private List<MenuProduct> menuProducts1;
    private List<MenuProduct> menuProducts2;

    @BeforeEach
    void setUp() {
        menuProducts1 = Arrays.asList(
                new MenuProduct(1L, 1L, 1L, 2),
                new MenuProduct(1L, 1L, 2L, 1)
        );

        menuProducts2 = Arrays.asList(
                new MenuProduct(1L, 2L, 1L, 3),
                new MenuProduct(1L, 2L, 2L, 1)
        );
    }


    @Test
    @DisplayName("메뉴 그룹이 없으면 메뉴를 등록할 수 없다.")
    void isNoneMenuGroup() {
        //given
        Menu menu = new Menu("메뉴", BigDecimal.valueOf(1), 1L, menuProducts1);
        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(false);

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));

    }

    @Test
    @DisplayName("메뉴의 가격이 0 이상 이어야만 한다.")
    void menuPriceMinZero() {
        //given
        Menu menu = new Menu("메뉴", BigDecimal.valueOf(-1), 1L, menuProducts1);

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));

    }

    @Test
    @DisplayName("메뉴의 가격은 메뉴 상품들의 합계보다 작아야 한다.")
    void productTotalIsBigAsMenuPriceIsBigAs() {
        //given
        Menu menu = new Menu("메뉴", BigDecimal.valueOf(5000), 1L, menuProducts1);
        final Product product1 = new Product("상품1", BigDecimal.valueOf(1000));
        final Product product2 = new Product("상품2", BigDecimal.valueOf(1000));
        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(menuProducts1.get(0).getProductId())).willReturn(Optional.of(product1));
        given(productDao.findById(menuProducts1.get(1).getProductId())).willReturn(Optional.of(product2));


        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));

    }

    @Test
    @DisplayName("메뉴가 등록 된다.")
    void createMenu() {
        //given
        Menu menu = new Menu(1L, "메뉴", BigDecimal.valueOf(2000), 1L, menuProducts1);
        final Product product1 = new Product("상품1", BigDecimal.valueOf(1000));
        final Product product2 = new Product("상품2", BigDecimal.valueOf(1000));
        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(menuProducts1.get(0).getProductId())).willReturn(Optional.of(product1));
        given(productDao.findById(menuProducts1.get(1).getProductId())).willReturn(Optional.of(product2));
        given(menuDao.save(menu)).willReturn(menu);

        //when
        final Menu saveMenu = menuService.create(menu);

        //then
        assertAll("메뉴가 등록이 됨",
                () -> assertThat(saveMenu.getName()).isEqualTo(menu.getName()),
                () -> assertThat(saveMenu.getMenuProducts()).hasSize(2)
        );
    }

    @Test
    @DisplayName("메뉴 목록을 조회")
    void listMenu() {
        //given
        Menu menu1 = new Menu(1L, "메뉴1", BigDecimal.valueOf(2000), 1L, menuProducts1);
        Menu menu2 = new Menu(2L, "메뉴2", BigDecimal.valueOf(3000), 1L, menuProducts2);
        given(menuDao.findAll()).willReturn(Arrays.asList(menu1, menu2));
        given(menuProductDao.findAllByMenuId(menu1.getId())).willReturn(menuProducts1);
        given(menuProductDao.findAllByMenuId(menu2.getId())).willReturn(menuProducts1);

        //when
        final List<Menu> menus = menuService.list();

        //then
        assertThat(menus).hasSize(2);
        assertThat(menus).extracting("name").contains("메뉴1", "메뉴2");
    }
}
