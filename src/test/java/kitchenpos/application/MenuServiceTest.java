//package kitchenpos.application;
//
//import kitchenpos.domain.menu.Menu;
//import kitchenpos.domain.menu.MenuProduct;
//import kitchenpos.domain.product.Product;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@DisplayName("메뉴 서비스 테스트")
//@ExtendWith(MockitoExtension.class)
//class MenuServiceTest {
//
//    @Mock
//    private MenuDao menuDao;
//
//    @Mock
//    private MenuGroupDao menuGroupDao;
//
//    @Mock
//    private MenuProductDao menuProductDao;
//
//    @Mock
//    private ProductDao productDao;
//
//    @InjectMocks
//    private MenuService menuService;
//
//    private Product 허니콤보;
//    private MenuProduct 허니콤보상품;
//    private Menu 프리미엄메뉴;
//
//    @BeforeEach
//    void setUp() {
//        허니콤보 = Product.of(1L, "허니콤보", BigDecimal.valueOf(16_000));
//        허니콤보상품 = MenuProduct.of(1L, 1L, 1L, 2);
//        프리미엄메뉴 = Menu.of(1L, "프리미엄메뉴", BigDecimal.valueOf(16_000), 1L, Arrays.asList(허니콤보상품));
//    }
//
//    @DisplayName("메뉴를 생성한다.")
//    @Test
//    void create() {
//        when(menuGroupDao.existsById(any())).thenReturn(true);
//        when(productDao.findById(any())).thenReturn(Optional.of(허니콤보));
//        when(menuDao.save(any())).thenReturn(프리미엄메뉴);
//        when(menuProductDao.save(any())).thenReturn(허니콤보상품);
//
//        Menu result = menuService.create(프리미엄메뉴);
//
//        assertAll(
//                () -> assertThat(result).isNotNull(),
//                () -> assertThat(result.getMenuProducts()).hasSize(1),
//                () -> assertThat(result.getMenuProducts().get(0)).isEqualTo(허니콤보상품)
//        );
//    }
//
//    @DisplayName("메뉴 가격이 없으면(null) 메뉴 생성 시 예외가 발생한다.")
//    @Test
//    void createException() {
//        Menu 프리미엄메뉴 = Menu.of(1L, "프리미엄메뉴", null, 1L, Arrays.asList(허니콤보상품));
//
//        Assertions.assertThatThrownBy(() -> menuService.create(프리미엄메뉴))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("메뉴 가격이 음수면 메뉴 생성 시 예외가 발생한다.")
//    @Test
//    void createException2() {
//        List<MenuProduct> 메뉴상품_목록 = Arrays.asList(허니콤보상품);
//        Menu 프리미엄메뉴 = Menu.of(1L, "프리미엄메뉴", BigDecimal.valueOf(-1), 1L, 메뉴상품_목록);
//
//        Assertions.assertThatThrownBy(() -> menuService.create(프리미엄메뉴))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("메뉴의 메뉴그룹이 존재하지 않으면 메뉴 생성 시 예외가 발생한다.")
//    @Test
//    void createException3() {
//        when(menuGroupDao.existsById(any())).thenReturn(false);
//
//        Assertions.assertThatThrownBy(() -> menuService.create(프리미엄메뉴))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("상품에 등록되지 않은 메뉴 상품으로 메뉴를 생성 시 예외가 발생한다.")
//    @Test
//    void createException4() {
//        when(menuGroupDao.existsById(any())).thenReturn(true);
//        when(productDao.findById(any())).thenReturn(Optional.empty());
//
//        Assertions.assertThatThrownBy(() -> menuService.create(프리미엄메뉴))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("메뉴의 가격이 메뉴 상품들의 가격의 합보다 크면 메뉴를 생성 시 예외가 발생한다.")
//    @Test
//    void createException5() {
//        List<MenuProduct> 메뉴상품_목록 = Arrays.asList(허니콤보상품);
//        Menu 프리미엄메뉴 = Menu.of(1L, "프리미엄메뉴", BigDecimal.valueOf(50_000), 1L, 메뉴상품_목록);
//
//        when(menuGroupDao.existsById(any())).thenReturn(true);
//        when(productDao.findById(any())).thenReturn(Optional.of(허니콤보));
//
//        Assertions.assertThatThrownBy(() -> menuService.create(프리미엄메뉴))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("메뉴 목록을 조회한다.")
//    @Test
//    void list() {
//        when(menuDao.findAll()).thenReturn(Arrays.asList(프리미엄메뉴));
//        when(menuProductDao.findAllByMenuId(any())).thenReturn(Arrays.asList(허니콤보상품));
//
//        List<Menu> results = menuService.list();
//
//        assertAll(
//                () -> assertThat(results).hasSize(1),
//                () -> assertThat(results.get(0).getMenuProducts()).containsExactly(허니콤보상품)
//        );
//    }
//}
