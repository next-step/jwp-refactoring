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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("MenuService 테스트")
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

    private Product 불고기;
    private Product 김치;
    private Product 공기밥;
    private MenuGroup 한식;
    private MenuProduct 불고기상품;
    private MenuProduct 김치상품;
    private MenuProduct 공기밥상품;
    private Menu 불고기정식;

    @BeforeEach
    void setUp() {
        불고기 = new Product(1L, "불고기", BigDecimal.valueOf(10_000));
        김치 = new Product(2L, "김치", BigDecimal.valueOf(1_000));
        공기밥 = new Product(3L, "공기밥", BigDecimal.valueOf(1_000));
        한식 = new MenuGroup(1L, "한식");
        불고기정식 = new Menu(1L, "불고기정식", BigDecimal.valueOf(12_000L), 한식.getId(), new ArrayList<>());
        불고기상품 = new MenuProduct(1L, 불고기정식.getId(), 불고기.getId(), 1L);
        김치상품 = new MenuProduct(2L, 불고기정식.getId(), 김치.getId(), 1L);
        공기밥상품 = new MenuProduct(3L, 불고기정식.getId(), 공기밥.getId(), 1L);
        불고기정식.setMenuProducts(Arrays.asList(불고기상품, 김치상품, 공기밥상품));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // given
        when(menuGroupDao.existsById(불고기정식.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(불고기상품.getProductId())).thenReturn(Optional.of(불고기));
        when(productDao.findById(김치상품.getProductId())).thenReturn(Optional.of(김치));
        when(productDao.findById(공기밥상품.getProductId())).thenReturn(Optional.of(공기밥));
        when(menuDao.save(불고기정식)).thenReturn(불고기정식);
        when(menuProductDao.save(불고기상품)).thenReturn(불고기상품);
        when(menuProductDao.save(김치상품)).thenReturn(김치상품);
        when(menuProductDao.save(공기밥상품)).thenReturn(공기밥상품);

        // when
        Menu result = menuService.create(불고기정식);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(불고기정식.getId()),
                () -> assertThat(result.getName()).isEqualTo(불고기정식.getName())
        );
    }

    @DisplayName("메뉴 가격이 null이면 예외가 발생한다.")
    @Test
    void createNullPriceMenuException() {
        // given
        불고기정식 = new Menu(1L, "불고기정식", null, 1L, new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> menuService.create(불고기정식))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 0 미만이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -1000, -20000})
    void createUnderZeroPriceMenuException(int input) {
        // given
        불고기정식 = new Menu(1L, "불고기정식", BigDecimal.valueOf(input), 1L, new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> menuService.create(불고기정식))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 속한 메뉴그룹이 없을 경우 예외가 발생한다.")
    @Test
    void notExistMenuGroupException() {
        // given
        불고기정식 = new Menu(1L, "불고기정식", BigDecimal.valueOf(1_000), 1L, new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> menuService.create(불고기정식))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 포함된 상품이 없을 경우 예외가 발생한다.")
    @Test
    void notExistProductException() {
        // given
        불고기정식 = new Menu(1L, "불고기정식", BigDecimal.valueOf(1_000), 1L, new ArrayList<>());
        when(menuGroupDao.existsById(불고기정식.getMenuGroupId())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> menuService.create(불고기정식))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 메뉴에 포함된 상품 가격의 합보다 작아야 한다.")
    @Test
    void menuPriceException() {
        // given
        불고기정식.setPrice(BigDecimal.valueOf(200_000));
        when(menuGroupDao.existsById(불고기정식.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(불고기상품.getProductId())).thenReturn(Optional.of(불고기));
        when(productDao.findById(김치상품.getProductId())).thenReturn(Optional.of(김치));
        when(productDao.findById(공기밥상품.getProductId())).thenReturn(Optional.of(공기밥));

        // when & then
        assertThatThrownBy(() -> menuService.create(불고기정식))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 조회할 수 있다.")
    @Test
    void findAllMenu() {
        // given
        List<Menu> menus = Arrays.asList(불고기정식);
        when(menuDao.findAll()).thenReturn(menus);
        when(menuProductDao.findAllByMenuId(불고기정식.getId())).thenReturn(Arrays.asList(불고기상품, 김치상품, 공기밥상품));

        // when
        List<Menu> results = menuService.list();

        // then
        assertAll(
                () -> assertThat(results).hasSize(1),
                () -> assertThat(results.get(0).getId()).isEqualTo(불고기정식.getId()),
                () -> assertThat(results.get(0).getName()).isEqualTo(불고기정식.getName())
        );
    }
}
