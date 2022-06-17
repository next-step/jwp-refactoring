package kitchenpos.application;

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
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

    private Menu menu;
    private MenuProduct menuProduct;
    private Product product;

    @BeforeEach
    void setUp() {
        menu = new Menu();
        menuProduct = new MenuProduct();
        product = new Product();
    }

    @Test
    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    void create() {
        //given
        menu.setPrice(BigDecimal.valueOf(999));
        product.setPrice(BigDecimal.valueOf(1000));
        menuProduct.setQuantity(1);
        menu.setMenuProducts(Arrays.asList(menuProduct));
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.of(product));
        given(menuProductDao.save(any())).willReturn(menuProduct);
        given(menuDao.save(any())).willReturn(menu);

        //when
        Menu savedMenu = menuService.create(menu);

        //then
        assertThat(savedMenu).isInstanceOf(Menu.class);
        assertThat(savedMenu.getMenuProducts()).containsExactly(menuProduct);

    }

    @Test
    @DisplayName("메뉴 가격이 NULL 이거나 음수이면 메뉴를 추가할 수 없다")
    void create_failed_1() {
        //then
        assertThatThrownBy(() -> menuService.create(menu)).isExactlyInstanceOf(IllegalArgumentException.class);

        //when
        menu.setPrice(BigDecimal.valueOf(0));

        //then
        assertThatThrownBy(() -> menuService.create(menu)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹에 속하지 않으면 메뉴를 추가할 수 없다")
    void create_failed_2() {
        //given
        menu.setPrice(BigDecimal.valueOf(10000));
        given(menuGroupDao.existsById(any())).willReturn(false);

        //then
        assertThatThrownBy(() -> menuService.create(menu)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 상품의 금액보다 메뉴 가격이 크거나 같으면 메뉴로 추가할 수 없다")
    void create_failed_3() {
        //given
        product.setPrice(BigDecimal.valueOf(10000));
        menu.setPrice(BigDecimal.valueOf(10000));
        menu.setMenuProducts(Arrays.asList(menuProduct));
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.of(product));

        //then
        assertThatThrownBy(() -> menuService.create(menu)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 상품 중 조회 되지 않는 경우가 있으면 메뉴로 추가할 수 없다")
    void create_failed_4() {
        //given
        menu.setPrice(BigDecimal.valueOf(10000));
        menu.setMenuProducts(Arrays.asList(menuProduct));
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> menuService.create(menu)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 메뉴 그룹을 조회할 수 있다.")
    void list() {
        //given
        given(menuDao.findAll()).willReturn(Arrays.asList(menu));
        given(menuProductDao.findAllByMenuId(any())).willReturn(Collections.singletonList(menuProduct));

        //then
        assertThat(menuService.list()).isNotEmpty();
    }
}
