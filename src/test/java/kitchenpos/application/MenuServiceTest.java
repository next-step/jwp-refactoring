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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

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

    private Product product;
    private MenuProduct menuProduct;
    private List<MenuProduct> menuProducts;
    private MenuGroup menuGroup;
    private Menu menu;

    @BeforeEach
    void beforeEach() {
        product = 상품_생성됨(1L, "소불고기", 1000);
        menuProduct = new MenuProduct.Builder(product.getId(), 1).build();
        menuProducts = Arrays.asList(menuProduct, menuProduct);
        menuGroup = MenuGroup.builder().id(1L).name("점심메뉴").build();
        menu = new Menu.Builder("점심특선", 2000, menuGroup.getId(), menuProducts).build();
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        // given
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.of(product));
        given(menuDao.save(any(Menu.class))).willReturn(new Menu.Builder().id(1L).build());
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(new MenuProduct.Builder().seq(1L).build());

        // when
        Menu created = menuService.create(menu);

        // then
        assertThat(created.getId()).isNotNull();

        // verify
        then(menuGroupDao).should(times(1)).existsById(anyLong());
        then(productDao).should(times(2)).findById(anyLong());
        then(menuDao).should(times(1)).save(any(Menu.class));
        then(menuProductDao).should(times(2)).save(any(MenuProduct.class));
    }

    @DisplayName("0원 보다 작은 금액으로 메뉴를 생성할 수 없다.")
    @Test
    void create_throwsException_ifPriceLessThanZero() {
        // given
        Menu menu = new Menu.Builder("점심특선", -1000, menuGroup.getId(), menuProducts).build();

        // when
        // then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));

        // verify
        then(menuGroupDao).should(never()).existsById(anyLong());
    }

    @DisplayName("메뉴를 구성하는 상품의 가격 * 수량의 합계는 메뉴 가격과 일치해야 한다.")
    @Test
    void create_throwsException_ifWrongPrice() {
        // given
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.of(product));

        Menu menu = new Menu.Builder("점심특선", 4000, menuGroup.getId(), menuProducts).build();

        // when
        // then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);

        // verify
        then(menuDao).should(never()).save(any(Menu.class));
    }

    public static Product 상품_생성됨(Long id, String name, long price) {
        return new Product(id, name, price);
    }
}
