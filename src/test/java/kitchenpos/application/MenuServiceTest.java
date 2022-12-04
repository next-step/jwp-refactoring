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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

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

    private MenuGroup 양식;
    private Menu 양식_세트;
    private Product 스파게티;
    private Product 스테이크;
    private Product 에이드;
    private MenuProduct 양식_스파게티;
    private MenuProduct 양식_스테이크;
    private MenuProduct 양식_에이드;
    private List<MenuProduct> 메뉴_상품;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup(1L, "양식");
        양식_세트 = new Menu(1L, "양식 세트", null, 양식.getId());

        스파게티 = new Product(1L, "스파게티", new BigDecimal(18000));
        스테이크 = new Product(2L, "스테이크", new BigDecimal(25000));
        에이드 = new Product(3L, "에이드", new BigDecimal(3500));

        양식_스파게티 = new MenuProduct(1L, 양식_세트.getId(), 스파게티.getId(), 1);
        양식_스테이크 = new MenuProduct(2L, 양식_세트.getId(), 스테이크.getId(), 1);
        양식_에이드 = new MenuProduct(3L, 양식_세트.getId(), 에이드.getId(), 2);

        메뉴_상품 = Arrays.asList(양식_스파게티, 양식_스테이크, 양식_에이드);
    }

    @Test
    void 메뉴의_가격이_null이면_메뉴를_등록할_수_없음() {
        양식_세트.setPrice(null);

        assertThatThrownBy(() -> {
            menuService.create(양식_세트);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, -2, -5, -10 })
    void 메뉴의_가격이_0보다_작으면_메뉴를_등록할_수_없음(int price) {
        양식_세트.setPrice(new BigDecimal(price));

        assertThatThrownBy(() -> {
            menuService.create(양식_세트);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹에_속해_있지_않은_메뉴를_등록할_수_없음() {
        long notSavedMenuGroupId = 1L;
        양식_세트.setPrice(new BigDecimal(50000));
        양식_세트.setMenuGroupId(notSavedMenuGroupId);

        given(menuGroupDao.existsById(notSavedMenuGroupId)).willReturn(false);

        assertThatThrownBy(() -> {
            menuService.create(양식_세트);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴에_포함된_상품이_등록된_상품이_아니면_메뉴를_등록할_수_없음() {
        양식_세트.setPrice(new BigDecimal(50000));
        양식_세트.setMenuProducts(메뉴_상품);

        given(menuGroupDao.existsById(양식.getId())).willReturn(true);
        given(productDao.findById(스파게티.getId())).willReturn(Optional.of(스파게티));
        given(productDao.findById(스테이크.getId())).willReturn(Optional.of(스테이크));
        given(productDao.findById(에이드.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            menuService.create(양식_세트);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴에_포함된_상품의_총_금액보다_메뉴의_가격이_크면_메뉴를_등록할_수_없음() {
        양식_세트.setPrice(new BigDecimal(55000));
        양식_세트.setMenuProducts(메뉴_상품);

        given(menuGroupDao.existsById(양식.getId())).willReturn(true);
        given(productDao.findById(스파게티.getId())).willReturn(Optional.of(스파게티));
        given(productDao.findById(스테이크.getId())).willReturn(Optional.of(스테이크));
        given(productDao.findById(에이드.getId())).willReturn(Optional.of(에이드));

        assertThatThrownBy(() -> {
            menuService.create(양식_세트);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_등록() {
        양식_세트.setPrice(new BigDecimal(50000));
        양식_세트.setMenuProducts(메뉴_상품);

        given(menuGroupDao.existsById(양식.getId())).willReturn(true);
        given(productDao.findById(스파게티.getId())).willReturn(Optional.of(스파게티));
        given(productDao.findById(스테이크.getId())).willReturn(Optional.of(스테이크));
        given(productDao.findById(에이드.getId())).willReturn(Optional.of(에이드));
        given(menuProductDao.save(양식_스파게티)).willReturn(양식_스파게티);
        given(menuProductDao.save(양식_스테이크)).willReturn(양식_스테이크);
        given(menuProductDao.save(양식_에이드)).willReturn(양식_에이드);
        given(menuDao.save(양식_세트)).willReturn(양식_세트);

        Menu savedMenu = menuService.create(양식_세트);

        assertThat(savedMenu).satisfies(menu -> {
           assertEquals(양식_세트.getId(), savedMenu.getId());
           assertEquals(양식_세트.getName(), savedMenu.getName());
           assertEquals(양식_세트.getPrice(), savedMenu.getPrice());
           assertEquals(양식_세트.getMenuGroupId(), savedMenu.getMenuGroupId());
           assertEquals(양식_세트.getMenuProducts().size(), savedMenu.getMenuProducts().size());
        });
    }

    @Test
    void 메뉴_목록_조회() {
        양식_세트.setPrice(new BigDecimal(50000));
        양식_세트.setMenuProducts(메뉴_상품);

        given(menuDao.findAll()).willReturn(Arrays.asList(양식_세트));
        given(menuProductDao.findAllByMenuId(양식_세트.getId())).willReturn(메뉴_상품);

        List<Menu> menus = menuService.list();

        assertAll(
                () -> assertThat(menus).hasSize(1),
                () -> assertThat(menus).containsExactly(양식_세트)
        );
    }
}
