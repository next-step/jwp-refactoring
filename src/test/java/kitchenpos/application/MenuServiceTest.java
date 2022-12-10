package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
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
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 서비스 테스트")
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

    private Product 후라이드;
    private MenuProduct 후라이드치킨상품;
    private Menu 후라이드치킨;

    @BeforeEach
    void setUp() {
        후라이드 = Product.of(1L, "후라이드", BigDecimal.valueOf(16_000));
        후라이드치킨상품 = MenuProduct.of(1L, 1L, 1L, 2);
        후라이드치킨 = Menu.of(1L, "후라이드치킨", BigDecimal.valueOf(16_000), 1L, Arrays.asList(후라이드치킨상품));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(후라이드));
        when(menuDao.save(any())).thenReturn(후라이드치킨);
        when(menuProductDao.save(any())).thenReturn(후라이드치킨상품);

        MenuResponse result = menuService.create(후라이드치킨);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getMenuProducts()).hasSize(1),
                () -> assertThat(result.getMenuProducts().get(0).getSeq()).isEqualTo(후라이드치킨상품.getSeq())
        );
    }

    @DisplayName("메뉴 가격이 없으면(null) 메뉴 생성 시 예외가 발생한다.")
    @Test
    void createException() {
        Menu 후라이드치킨 = Menu.of(1L, "후라이드치킨", null, 1L, Arrays.asList(후라이드치킨상품));

        Assertions.assertThatThrownBy(() -> menuService.create(후라이드치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 음수면 메뉴 생성 시 예외가 발생한다.")
    @Test
    void createException2() {
        List<MenuProduct> 메뉴상품_목록 = Arrays.asList(후라이드치킨상품);
        Menu 후라이드치킨 = Menu.of(1L, "후라이드치킨", BigDecimal.valueOf(-1), 1L, 메뉴상품_목록);

        Assertions.assertThatThrownBy(() -> menuService.create(후라이드치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 메뉴그룹이 존재하지 않으면 메뉴 생성 시 예외가 발생한다.")
    @Test
    void createException3() {
        when(menuGroupDao.existsById(any())).thenReturn(false);

        Assertions.assertThatThrownBy(() -> menuService.create(후라이드치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품에 등록되지 않은 메뉴 상품으로 메뉴를 생성 시 예외가 발생한다.")
    @Test
    void createException4() {
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> menuService.create(후라이드치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품들의 가격의 합보다 크면 메뉴를 생성 시 예외가 발생한다.")
    @Test
    void createException5() {
        List<MenuProduct> 메뉴상품_목록 = Arrays.asList(후라이드치킨상품);
        Menu 후라이드치킨 = Menu.of(1L, "후라이드치킨", BigDecimal.valueOf(50_000), 1L, 메뉴상품_목록);

        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(후라이드));

        Assertions.assertThatThrownBy(() -> menuService.create(후라이드치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        when(menuDao.findAll()).thenReturn(Arrays.asList(후라이드치킨));
        when(menuProductDao.findAllByMenuId(any())).thenReturn(Arrays.asList(후라이드치킨상품));

        List<MenuResponse> results = menuService.list();

        assertAll(
                () -> assertThat(results).hasSize(1),
                () -> assertThat(results.get(0).getMenuProducts())
                        .containsExactly(MenuProductResponse.from(후라이드치킨상품))
        );
    }
}
