package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private Long menuId;
    private Product product1;
    private Product product2;
    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;
    private List<MenuProduct> menuProducts;

    @BeforeEach
    void setUp() {
        menuId = 1L;
        product1 = new Product(1L, "양념치킨", BigDecimal.valueOf(18000L));
        product2 = new Product(2L, "후라이드치킨", BigDecimal.valueOf(17000L));
        menuProduct1 = new MenuProduct(1L, 1L, product1.getId(), 1);
        menuProduct2 = new MenuProduct(2L, 1L, product2.getId(), 1);
        menuProducts = Arrays.asList(menuProduct1, menuProduct2);
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void 메뉴_등록() {
        // given
        given(productDao.findById(product1.getId())).willReturn(Optional.of(product1));
        given(productDao.findById(product2.getId())).willReturn(Optional.of(product2));
        given(menuProductDao.save(menuProduct1)).willReturn(menuProduct1);
        given(menuProductDao.save(menuProduct2)).willReturn(menuProduct2);

        Menu expected = new Menu(menuId, "반반", BigDecimal.valueOf(30000), 1L, menuProducts);
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(menuDao.save(expected)).willReturn(expected);

        // when
        Menu actual = menuService.create(expected);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("메뉴의 가격이 없으면 등록이 실패한다.")
    @Test
    void 메뉴_생성_예외_메뉴_가격이_없음() {
        // given
        Menu menu = new Menu(menuId, "반반", null, 1L, menuProducts);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> menuService.create(menu)
        );
    }

    @DisplayName("메뉴의 가격이 없으면 0보다 작으면 등록이 실패한다.")
    @Test
    void 메뉴_생성_예외_메뉴_가격이_음수() {
        // given
        final BigDecimal price = BigDecimal.valueOf(-1000L);
        Menu menu = new Menu(menuId, "반반", price, 1L, menuProducts);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> menuService.create(menu)
        );
    }


    @DisplayName("단일 상품 가격의 합보다 메뉴의 가격이 같거나 크면 등록할 수 없다.")
    @ParameterizedTest
    @ValueSource(longs = {18000 + 17000 + 100, 18000 + 17000})
    void 메뉴_생성_예외_메뉴_가격이_같거나_큼(Long price) {
        // given
        Menu menu = new Menu(menuId, "반반", BigDecimal.valueOf(price), 1L, menuProducts);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> menuService.create(menu)
        );
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void 메뉴_목록_조회() {
        // given
        Menu menu1 = new Menu(menuId, "반반", BigDecimal.valueOf(30000), 1L, menuProducts);
        Menu menu2 = new Menu(menuId, "반반2", BigDecimal.valueOf(25000), 1L, menuProducts);
        given(menuDao.findAll()).willReturn(Arrays.asList(menu1, menu2));

        // when
        List<Menu> actual = menuService.list();

        // then
        assertThat(actual).containsAll(Arrays.asList(menu1, menu2));
    }

}
