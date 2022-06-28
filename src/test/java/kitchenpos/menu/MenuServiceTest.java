package kitchenpos.menu;

import kitchenpos.application.MenuService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @InjectMocks
    MenuService menuService;

    @Mock
    MenuGroupDao menuGroupDao;

    @Mock
    ProductDao productDao;

    @Mock
    MenuProductDao menuProductDao;

    @Mock
    MenuDao menuDao;

    private MenuGroup 한마리메뉴;
    private Product 후라이드;
    private Product 양념;
    private MenuProduct 후라이드메뉴상품;
    private MenuProduct 양념메뉴상품;
    private Menu 후라이드치킨;
    private Menu 양념치킨;

    @BeforeEach
    void setUp() {
        한마리메뉴 = MenuGroup.of(1L, "한마리메뉴");

        후라이드 = Product.of(1L, "후라이드", new BigDecimal(16000));
        양념 = Product.of(2L, "양념치킨", new BigDecimal(16000));

        후라이드메뉴상품 = MenuProduct.of(1L, 1L, 후라이드.getId(), 1);
        양념메뉴상품 = MenuProduct.of(2L, 2L, 양념.getId(), 1);

        후라이드치킨 = Menu.of(1L, "후라이드치킨", new BigDecimal(16000), 한마리메뉴.getId(), Arrays.asList(후라이드메뉴상품));
        양념치킨 = Menu.of(2L, "양념치킨", new BigDecimal(17000), 한마리메뉴.getId(), Arrays.asList(양념메뉴상품));
    }

    @DisplayName("메뉴 등록")
    @Test
    void createMenu() {
        // given
        when(menuGroupDao.existsById(한마리메뉴.getId()))
                .thenReturn(true);
        when(productDao.findById(후라이드.getId()))
                .thenReturn(Optional.ofNullable(후라이드));
        when(menuProductDao.save(후라이드메뉴상품))
                .thenReturn(후라이드메뉴상품);
        when(menuDao.save(후라이드치킨))
                .thenReturn(후라이드치킨);

        // when
        Menu result = menuService.create(후라이드치킨);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(후라이드치킨.getId()),
                () -> assertThat(result.getName()).isEqualTo(후라이드치킨.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(후라이드치킨.getPrice()),
                () -> assertThat(result.getMenuGroupId()).isEqualTo(후라이드치킨.getMenuGroupId()),
                () -> assertThat(result.getMenuProducts()).isEqualTo(후라이드치킨.getMenuProducts())
        );
    }

    @DisplayName("메뉴 전체 조회")
    @Test
    void findAllMenus() {
        // given
        when(menuDao.findAll())
                .thenReturn(Arrays.asList(후라이드치킨, 양념치킨));

        // when
        List<Menu> list = menuService.list();

        // then
        assertAll(
                () -> assertThat(list).hasSize(2),
                () -> assertThat(list).containsExactly(후라이드치킨, 양념치킨)
        );
    }
}
