package kitchenpos.application;

import java.util.List;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static kitchenpos.domain.MenuFixture.createMenu;
import static kitchenpos.domain.MenuProductFixture.createMenuProduct;
import static kitchenpos.domain.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

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

    private Product 후라이드치킨;
    private Product 양념치킨;
    private Product 콜라;
    private MenuProduct 후라이드치킨상품;
    private MenuProduct 양념치킨상품;
    private MenuProduct 콜라상품;

    @BeforeEach
    void setUp() {
        후라이드치킨 = createProduct(1L, "후라이드치킨", BigDecimal.valueOf(10000));
        양념치킨 = createProduct(2L, "양념치킨", BigDecimal.valueOf(5000));
        콜라 = createProduct(3L, "콜라", BigDecimal.valueOf(3000));

        후라이드치킨상품 = createMenuProduct(1L, 1L, 1L, 10);
        양념치킨상품 = createMenuProduct(2L, 1L, 2L, 10);
        콜라상품 = createMenuProduct(3L, 1L, 3L, 1);
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create() {
        Menu 두마리치킨 = createMenu(1L, "두마리치킨", new BigDecimal(13000), 1L, Arrays.asList(후라이드치킨상품, 양념치킨상품));
        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(1L)).willReturn(Optional.of(후라이드치킨));
        given(productDao.findById(2L)).willReturn(Optional.of(양념치킨));
        given(menuProductDao.save(후라이드치킨상품)).willReturn(후라이드치킨상품);
        given(menuProductDao.save(양념치킨상품)).willReturn(양념치킨상품);
        given(menuDao.save(두마리치킨)).willReturn(두마리치킨);

        Menu savedMenu = menuService.create(두마리치킨);

        assertAll(
                () -> assertThat(savedMenu.getMenuProducts()).contains(후라이드치킨상품, 양념치킨상품),
                () -> assertThat(savedMenu.getName()).isEqualTo(두마리치킨.getName()),
                () -> assertThat(savedMenu.getPrice()).isEqualTo(두마리치킨.getPrice())
        );
    }

    @DisplayName("가격이 존재하지 않는 메뉴는 등록할 수 없다.")
    @Test
    void createWithPriceIsNull() {
        Menu 두마리치킨 = createMenu(1L, "두마리치킨", null, 1L, Arrays.asList(후라이드치킨상품, 양념치킨상품));

        assertThatThrownBy(() -> menuService.create(두마리치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 0원 미만인 메뉴는 등록할 수 없다.")
    @Test
    void createWithPriceIsNegative() {
        Menu 두마리치킨 = createMenu(1L, "두마리치킨", new BigDecimal(-1000), 1L, Arrays.asList(후라이드치킨상품, 양념치킨상품));

        assertThatThrownBy(() -> menuService.create(두마리치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격의 총합(가격 * 갯수)보다 메뉴 가격이 큰 경우 메뉴를 등록할 수 없다.")
    @Test
    void createWithPriceGoeMenuPrice() {
        Menu 두마리치킨 = createMenu(1L, "두마리치킨", new BigDecimal(16000), 1L, Arrays.asList(후라이드치킨상품, 양념치킨상품));

        assertThatThrownBy(() -> menuService.create(두마리치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        Menu 두마리치킨 = createMenu(1L, "두마리치킨", new BigDecimal(13000), 1L, Arrays.asList(후라이드치킨상품, 양념치킨상품));
        Menu 양념세트 = createMenu(1L, "양념세트", new BigDecimal(10000), 1L, Arrays.asList(후라이드치킨상품, 콜라상품));
        given(menuDao.findAll()).willReturn(Arrays.asList(두마리치킨, 양념세트));

        List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(2);
        assertThat(menus).contains(두마리치킨, 양념세트);
    }
}
