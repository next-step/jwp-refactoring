package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kitchenpos.common.domian.Quantity;
import kitchenpos.menu.domain.ProductsQuantities;
import kitchenpos.menu.domain.Quantities;
import kitchenpos.menu.domain.Products;
import kitchenpos.menu.dto.MenuListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.domian.Price;
import kitchenpos.common.error.CustomException;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menuproduct.dto.MenuProductRequest;
import kitchenpos.menu.repository.MenuDao;
import kitchenpos.menugroup.repository.MenuGroupDao;
import kitchenpos.menuproduct.repository.MenuProductDao;
import kitchenpos.product.repository.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.domain.Menu;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 테스트")
class MenuServiceTest {

    public static final String 순대국 = "순대국";
    public static final long PRODUCT_ID = 1L;
    public static final long QUANTITY = 8L;

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
    private MenuRequest menuRequest;
    private MenuGroup menuGroup;
    private Product product;

    @BeforeEach
    void setup() {
        int requestAmount = 1000;

        menuGroup = new MenuGroup("국밥");
        product = new Product(PRODUCT_ID, "순대", new Price(1000));

        List<Product> products = Arrays.asList(new Product(PRODUCT_ID, "순대", new Price(requestAmount)));
        Map<Long, Quantity> quantities = new HashMap<Long, Quantity>() {{ put(PRODUCT_ID, new Quantity(QUANTITY)); }};

        Price requestPrice = new Price(BigDecimal.valueOf(requestAmount * QUANTITY));
        ProductsQuantities productsQuantities = new ProductsQuantities(new Products(products, products.size()), new Quantities(quantities, quantities.size()), requestPrice);

        menu = Menu.of(menuGroup, "순대국", productsQuantities);
    }

    @DisplayName("사용자는 메뉴를 만들 수 있다.")
    @Test
    void create() {
        // given
        menuRequest = new MenuRequest("순대국", 8000, 1L, Arrays.asList(new MenuProductRequest(PRODUCT_ID, QUANTITY)));
        // when
        when(menuGroupDao.findById(any())).thenReturn(Optional.of(menuGroup));
        when(productDao.findByIds(any())).thenReturn(Arrays.asList(product));
        when(menuDao.save(any())).thenReturn(menu);
        MenuResponse createdMenu = menuService.create(menuRequest);

        // then
        assertThat(createdMenu).isNotNull();
        assertThat(createdMenu.getName()).isEqualTo(순대국);
    }

    @DisplayName("사용자는 메뉴 리스트를 조회 할 수 있다.")
    @Test
    void findAll() {
        // given

        // when
        when(menuDao.findAll()).thenReturn(Arrays.asList(menu));
        MenuListResponse menus = menuService.list();
        // then
        assertThat(menus.getMenuResponses().size()).isEqualTo(1);
        assertThat(menus.getMenuResponses().get(0).getName()).isEqualTo(순대국);
    }

    @DisplayName("메뉴 가격이 음수 일 수 없다.")
    @Test
    void createFailedByPriceNegative() {
        // given
        menuRequest = new MenuRequest(순대국, -100, 1L, Arrays.asList(new MenuProductRequest(PRODUCT_ID, QUANTITY)));
        // when
        // then
        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(CustomException.class);
    }

    @DisplayName("메뉴는 메뉴 그룹에 반드시 포함 되어야 한다.")
    @Test
    void createFailedByMenuGroup() {
        // given
        menuRequest = new MenuRequest(순대국, 0, 0L, Arrays.asList(new MenuProductRequest(PRODUCT_ID, QUANTITY)));
        // when
        // then
        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(CustomException.class);
    }

    @DisplayName("메뉴의 상품은 반드시 존재해야 한다.")
    @Test
    void createFailedByPrice() {
        // given
        menuRequest = new MenuRequest(순대국, 0, 1L, Arrays.asList(new MenuProductRequest(PRODUCT_ID, QUANTITY)));
        // when
        when(menuGroupDao.findById(any())).thenReturn(Optional.of(menuGroup));
        // then
        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(CustomException.class);
    }
}