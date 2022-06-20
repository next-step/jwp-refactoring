package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 서비스에 대한 테스트")
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

    private Menu 메뉴_후라이드_치킨Set;
    private Menu 메뉴_양념_치킨Set;
    private List<Menu> 메뉴_목록;

    private MenuProduct 후라이드_치킨Set_치킨;
    private MenuProduct 후라이드_치킨Set_감자튀김;
    private MenuProduct 양념_치킨Set_치킨;
    private MenuProduct 양념_치킨Set_감자튀김;

    private Product 후라이드_치킨;
    private Product 양념_치킨;
    private Product 감자튀김;

    @BeforeEach
    void setUp() {
        후라이드_치킨Set_치킨 = MenuProduct.of(1L, 1L, 1L, 1);
        후라이드_치킨Set_감자튀김 = MenuProduct.of(1L, 1L, 3L, 1);
        양념_치킨Set_치킨 = MenuProduct.of(1L, 2L, 2L, 1);
        양념_치킨Set_감자튀김 = MenuProduct.of(1L, 2L, 3L, 1);

        메뉴_후라이드_치킨Set = Menu.of(1L, "후라이드 치킨", BigDecimal.valueOf(18000L), 1L,
            Arrays.asList(후라이드_치킨Set_치킨, 후라이드_치킨Set_감자튀김));
        메뉴_양념_치킨Set = Menu.of(2L, "양념 치킨", BigDecimal.valueOf(19000L), 1L,
            Arrays.asList(양념_치킨Set_치킨, 양념_치킨Set_감자튀김));
        메뉴_목록 = Arrays.asList(메뉴_후라이드_치킨Set, 메뉴_양념_치킨Set);

        후라이드_치킨 = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(15000L));
        양념_치킨 = Product.of(2L, "양념치킨", BigDecimal.valueOf(16000L));
        감자튀김 = Product.of(3L, "감자튀김", BigDecimal.valueOf(5000L));
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void create_test() {
        // given
        when(menuGroupDao.existsById(메뉴_후라이드_치킨Set.getMenuGroupId()))
            .thenReturn(true);
        when(productDao.findById(후라이드_치킨.getId()))
            .thenReturn(Optional.of(후라이드_치킨));
        when(productDao.findById(감자튀김.getId()))
            .thenReturn(Optional.of(감자튀김));
        when(menuDao.save(메뉴_후라이드_치킨Set))
            .thenReturn(메뉴_후라이드_치킨Set);

        // when
        Menu menu = menuService.create(메뉴_후라이드_치킨Set);

        // then
        assertAll(
            () -> assertThat(menu.getId()).isEqualTo(메뉴_후라이드_치킨Set.getId()),
            () -> assertThat(menu.getName()).isEqualTo(메뉴_후라이드_치킨Set.getName()),
            () -> assertThat(menu.getPrice()).isEqualTo(메뉴_후라이드_치킨Set.getPrice())
        );
    }

    @DisplayName("메뉴 등록시 메뉴의 금액이 없거나 0 미만이면 예외가 발생한다")
    @Test
    void create_exception_test() {
        메뉴_후라이드_치킨Set.setPrice(null);

        // then
        assertThatThrownBy(() -> {
            menuService.create(메뉴_후라이드_치킨Set);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록시 메뉴의 금액이 없거나 0 미만이면 예외가 발생한다")
    @Test
    void create_exception_test2() {
        메뉴_후라이드_치킨Set.setPrice(BigDecimal.valueOf(-500));

        // then
        assertThatThrownBy(() -> {
            menuService.create(메뉴_후라이드_치킨Set);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록시 등록할 메뉴의 그룹이 존재하지 않으면 예외가 발생한다")
    @Test
    void create_exception_test3() {
        // given
        메뉴_후라이드_치킨Set.setMenuGroupId(null);

        // then
        assertThatThrownBy(() -> {
            menuService.create(메뉴_후라이드_치킨Set);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록시 등록할 메뉴의 상품이 존재하지 않으면 예외가 발생한다")
    @Test
    void create_exception_test4() {
        // given
        when(menuGroupDao.existsById(메뉴_후라이드_치킨Set.getMenuGroupId()))
            .thenReturn(true);
        when(productDao.findById(후라이드_치킨Set_치킨.getProductId()))
            .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> {
            menuService.create(메뉴_후라이드_치킨Set);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록할 메뉴의 상품들의 금액의 합보다 메뉴의 금액이 더 크면 예외가 발생한다")
    @Test
    void create_exception_test5() {
        // given
        후라이드_치킨.setPrice(BigDecimal.valueOf(10000L));
        감자튀김.setPrice(BigDecimal.valueOf(5000L));

        when(menuGroupDao.existsById(메뉴_후라이드_치킨Set.getMenuGroupId()))
            .thenReturn(true);
        when(productDao.findById(후라이드_치킨Set_치킨.getProductId()))
            .thenReturn(Optional.of(후라이드_치킨));
        when(productDao.findById(후라이드_치킨Set_감자튀김.getProductId()))
            .thenReturn(Optional.of(감자튀김));

        // then
        assertThatThrownBy(() -> {
            menuService.create(메뉴_후라이드_치킨Set);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 메뉴 목록을 조회한다")
    @Test
    void findAll_test() {
        // given
        when(menuDao.findAll())
            .thenReturn(메뉴_목록);
        when(menuProductDao.findAllByMenuId(1L))
            .thenReturn(Arrays.asList(후라이드_치킨Set_치킨, 후라이드_치킨Set_감자튀김));
        when(menuProductDao.findAllByMenuId(2L))
            .thenReturn(Arrays.asList(양념_치킨Set_치킨, 양념_치킨Set_감자튀김));

        // when
        List<Menu> result = menuService.list();

        // then
        assertThat(result).hasSize(2);
    }
}
