package kitchenpos.menu;

import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 관련 기능")
class MenuTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @Test
    @DisplayName("메뉴의 이름이 비어있을 경우 예외가 발생한다.")
    void emptyMenuName() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            menuService.create(new Menu(" "));
        });
    }

    @Test
    @DisplayName("메뉴의 가격이 0원 이상이 아닐 경우 예외가 발생한다.")
    void menuPriceLessThanZero() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(-1000)));
        });
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않을 경우 예외가 발생한다.")
    void nonExistMenuGroup() {
        // given
        when(menuGroupDao.existsById(any())).thenReturn(false);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000)));
        });
    }

    @Test
    @DisplayName("메뉴에 등록하고자 하는 상품이 존재하지 않을 경우 예외가 발생한다.")
    void nonExistProduct() {
        // given
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.empty());

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), Arrays.asList(new MenuProduct())));
        });
    }

    @Test
    @DisplayName("메뉴의 금액이 상품의 총 금액보다 크다면 예외가 발생한다.")
    void menuPriceMoreThanProductPriceSum() {
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(new Product(1L, "후라이드", BigDecimal.valueOf(9000))));

        assertThatIllegalArgumentException().isThrownBy(() -> {
            menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), Arrays.asList(new MenuProduct(1L, 1L))));
        });
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void createMenu() {
        // given
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(new Product(1L, "후라이드", BigDecimal.valueOf(9000))));
        when(menuDao.save(any())).thenReturn(new Menu(1L,"후라이드+후라이드", BigDecimal.valueOf(18000), 1L));
        when(menuProductDao.save(any())).thenReturn(new MenuProduct(1L, 1L, 1L, 2L));

        // when
        Menu menu = menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), 1L, Arrays.asList(new MenuProduct(1L, 2L))));

        // then
        assertAll(
                () -> assertThat(menu.getId()).isEqualTo(1L),
                () -> assertThat(menu.getName()).isEqualTo("후라이드+후라이드"),
                () -> assertThat(menu.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(18000)),
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(1L),
                () -> assertThat(menu.getMenuProducts()).extracting("seq").contains(1L)
        );
    }

    @Test
    @DisplayName("메뉴 목록을 조회할 수 있다.")
    void findMenu() {
        // given
        when(menuDao.findAll()).thenReturn(Arrays.asList(new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(18000), 1L)));
        when(menuProductDao.findAllByMenuId(any())).thenReturn(Arrays.asList(new MenuProduct(1L, 1L, 1L, 2L)));


        // when
        List<Menu> findByMenus = menuService.list();

        // then
        assertAll(
                () -> assertThat(findByMenus).extracting("id").contains(1L),
                () -> assertThat(findByMenus).extracting("menuProducts").isNotEmpty()
        );
    }
}
