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
    private List<MenuProduct> menuProducts;
    private MenuGroup menuGroup;
    private Menu menu;

    @BeforeEach
    void beforeEach() {
        product = new Product.Builder("소불고기", 1000).id(1L).build();
        menuProducts = Arrays.asList(
                new MenuProduct.Builder(product.getId(), 1).build(),
                new MenuProduct.Builder(product.getId(), 1).build()
        );
        menuGroup = new MenuGroup.Builder().id(1L).name("점심메뉴").build();
        menu = new Menu.Builder("점심특선", 2000, menuGroup.getId(), menuProducts).build();
    }

    @Test
    void create() {
        // given
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.of(product));
        given(menuDao.save(any(Menu.class))).willReturn(new Menu.Builder().id(1L).build());
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(new MenuProduct.Builder().build());

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

    @Test
    void create_throwsException_ifPriceLessThanZero() {
        // when
        // then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(
                new Menu.Builder("점심특선", -1000, menuGroup.getId(), menuProducts).build())
        );

        // verify
        then(menuGroupDao).should(never()).existsById(anyLong());
    }

    @Test
    void create_throwsException_ifWrongAmount() {
        // given
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.of(product));

        // when
        // then
        assertThatThrownBy(() -> menuService.create(
                new Menu.Builder("점심특선", 4000, menuGroup.getId(), menuProducts).build())
        ).isInstanceOf(IllegalArgumentException.class);

        // verify
        then(menuDao).should(never()).save(any(Menu.class));
    }
}
