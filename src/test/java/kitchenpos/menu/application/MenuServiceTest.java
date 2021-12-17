package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
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

    private final BigDecimal price = BigDecimal.valueOf(10000);
    private final Product product = new Product();
    private final MenuProduct menuProduct = new MenuProduct();
    private final Menu menu = new Menu();

    @BeforeEach
    void setUp() {
        product.setId(1L);
        product.setName("상품");
        product.setPrice(price);

        menuProduct.setSeq(1L);
        menuProduct.setQuantity(1L);
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);

        menu.setId(1L);
        menu.setMenuGroupId(1L);
        menu.setName("메뉴");
        menu.setPrice(price);
        menu.setMenuProducts(Arrays.asList(menuProduct));
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        when(menuGroupDao.existsById(anyLong()))
            .thenReturn(true);
        when(productDao.findById(anyLong()))
            .thenReturn(Optional.of(product));
        when(menuDao.save(any(Menu.class)))
            .thenReturn(menu);
        when(menuProductDao.save(any(MenuProduct.class)))
            .thenReturn(menuProduct);

        Menu saved = menuService.create(menu);

        assertAll(() -> {
            assertThat(saved.getName()).isEqualTo("메뉴");
            assertThat(saved.getMenuProducts())
                .extracting(MenuProduct::getMenuId)
                .containsExactly(saved.getId());
        });
    }

    @Test
    @DisplayName("메뉴 등록 시 메뉴 그룹이 없는 경우 실패")
    void createValidateMenuGroup() {
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격 체크 실패")
    void createValidatePrice() {
        menu.setPrice(BigDecimal.valueOf(-1));
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);

        menu.setPrice(null);
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격은 구성 상품의 총 합보다 작거나 같아야 한다.")
    void createValidateSumPrice() {
        menu.setPrice(BigDecimal.valueOf(20000));
        when(menuGroupDao.existsById(anyLong()))
            .thenReturn(true);
        when(productDao.findById(anyLong()))
            .thenReturn(Optional.of(product));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Disabled
    @Test
    @DisplayName("메뉴 이름 없는 경우 실패")
    void createValidateName() {
        // TODO: 2021/12/17 이름 정보 필수이므로 비지니스 로직 추가 필요. 현재는 실패하는 테스트임.
        menu.setName(null);

        when(menuGroupDao.existsById(anyLong()))
            .thenReturn(true);
        when(productDao.findById(anyLong()))
            .thenReturn(Optional.of(product));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void list() {
        when(menuDao.findAll())
            .thenReturn(Arrays.asList(menu));
        when(menuProductDao.findAllByMenuId(anyLong()))
            .thenReturn(Arrays.asList(menuProduct));

        List<Menu> menus = menuService.list();

        assertThat(menus.size()).isEqualTo(1);
        assertThat(menus).extracting(Menu::getName).containsExactly("메뉴");
        assertThat(menus.get(0).getMenuProducts())
            .extracting(MenuProduct::getMenuId).containsExactly(1L);
    }
}
