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

    private Product chip;
    private Product display;
    private MenuProduct chipProduct;
    private MenuProduct displayProduct;

    @BeforeEach
    void setUp() {
        chip = new Product(1L, "M1", BigDecimal.valueOf(10000));
        display = new Product(2L, "레티나", BigDecimal.valueOf(5000));
        chipProduct = new MenuProduct(1L, 1L, 1L, 10);
        displayProduct = new MenuProduct(2L, 1L, 2L, 10);
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create() {
        Menu macbook = new Menu(1L, "맥북", new BigDecimal(13000), 1L, Arrays.asList(chipProduct, displayProduct));
        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(1L)).willReturn(Optional.of(chip));
        given(productDao.findById(2L)).willReturn(Optional.of(display));
        given(menuProductDao.save(chipProduct)).willReturn(chipProduct);
        given(menuProductDao.save(displayProduct)).willReturn(displayProduct);
        given(menuDao.save(macbook)).willReturn(macbook);

        Menu savedMenu = menuService.create(macbook);

        assertAll(
                () -> assertThat(savedMenu.getMenuProducts()).contains(chipProduct, displayProduct),
                () -> assertThat(savedMenu.getName()).isEqualTo(macbook.getName()),
                () -> assertThat(savedMenu.getPrice()).isEqualTo(macbook.getPrice())
        );
    }

    @DisplayName("가격이 존재하지 않는 메뉴는 등록할 수 없다.")
    @Test
    void createWithPriceIsNull() {
        Menu macbook = new Menu(1L, "맥북", null, 1L, Arrays.asList(chipProduct, displayProduct));

        assertThatThrownBy(() -> menuService.create(macbook))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 0원 미만인 메뉴는 등록할 수 없다.")
    @Test
    void createWithPriceIsNegative() {
        Menu macbook = new Menu(1L, "맥북", new BigDecimal(-1000), 1L, Arrays.asList(chipProduct, displayProduct));

        assertThatThrownBy(() -> menuService.create(macbook))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격의 총합(가격 * 갯수)보다 메뉴 가격이 큰 경우 메뉴를 등록할 수 없다.")
    @Test
    void createWithPriceGoeMenuPrice() {
        Menu macbook = new Menu(1L, "맥북", new BigDecimal(16000), 1L, Arrays.asList(chipProduct, displayProduct));

        assertThatThrownBy(() -> menuService.create(macbook))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        Menu macbook = new Menu(1L, "맥북", new BigDecimal(13000), 1L, Arrays.asList(chipProduct, displayProduct));
        Menu asus = new Menu(1L, "Asus", new BigDecimal(10000), 1L, Arrays.asList(chipProduct, displayProduct));
        given(menuDao.findAll()).willReturn(Arrays.asList(macbook, asus));

        List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(2);
        assertThat(menus).contains(macbook, asus);
    }
}
