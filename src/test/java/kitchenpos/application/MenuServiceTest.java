package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @Test
    void 메뉴를_등록할_수_있다() {
        MenuProduct menuProduct = new MenuProduct(1L, 2L, 1L, 1l);
        Product 후라이드치킨_상품 = new Product(1L, "후라이드치킨", new BigDecimal(16000.00));
        Menu 후라이드치킨_메뉴 = new Menu(1L, "후라이드치킨", new BigDecimal(16000.00), 1L, Collections.singletonList(menuProduct));
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.of(후라이드치킨_상품));
        given(menuDao.save(any())).willReturn(후라이드치킨_메뉴);

        Menu menu = menuService.create(후라이드치킨_메뉴);

        assertThat(menu).isEqualTo(후라이드치킨_메뉴);
    }

    @Test
    void 메뉴등록시_가격이_0원_이하면_오류발생() {
        Menu 잘못된_가격이_측정된_메뉴 = new Menu(1L, "잘못된_가격이_측정된_메뉴", new BigDecimal(-1), 1L, null);

        ThrowingCallable 잘못된_가격의_메뉴_등록 = () -> menuService.create(잘못된_가격이_측정된_메뉴);

        assertThatIllegalArgumentException().isThrownBy(잘못된_가격의_메뉴_등록);
    }

    @Test
    void 등록_된_메뉴그룹만_지정할_수_있다() {
        Menu 후라이드치킨 = new Menu(1L, "후라이드치킨", new BigDecimal(16000.00), 1L, null);
        given(menuGroupDao.existsById(any())).willReturn(false);

        ThrowingCallable 메뉴그룹이_등록되어_있지_않다 = () -> menuService.create(후라이드치킨);

        assertThatIllegalArgumentException().isThrownBy(메뉴그룹이_등록되어_있지_않다);
    }

    @Test
    void 등록_된_상품만_지정할_수_있다() {
        MenuProduct menuProduct = new MenuProduct(1L, 2L, 1L, 1l);
        Menu 후라이드치킨 = new Menu(1L, "후라이드치킨", new BigDecimal(16000.00), 1L, Collections.singletonList(menuProduct));
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willThrow(IllegalArgumentException.class);

        ThrowingCallable 상품이_등록되어_있지_않다 = () -> menuService.create(후라이드치킨);

        assertThatIllegalArgumentException().isThrownBy(상품이_등록되어_있지_않다);
    }

    @Test
    void 메뉴_목록을_조회할_수_있다() {
        Menu 후라이드치킨 = new Menu(1L, "후라이드치킨", new BigDecimal(16000.00), 1L, null);
        Menu 양념치킨 = new Menu(2L, "양념치킨", new BigDecimal(18000.00), 1L, null);
        given(menuDao.findAll()).willReturn(Arrays.asList(후라이드치킨, 양념치킨));

        List<Menu> menus = menuService.list();

        assertAll(
                () -> assertThat(menus).hasSize(2),
                () -> assertThat(menus).containsExactly(후라이드치킨, 양념치킨)
        );
    }
}
