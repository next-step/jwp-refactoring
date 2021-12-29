package kitchenpos.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

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
    @DisplayName("메뉴를 등록한다.")
    void create() {
        // given
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(1);

        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(new BigDecimal(16_000),
                new ArrayList<>(Arrays.asList(menuProductCreateRequest)));

        MenuGroup menuGroup = new MenuGroup("두마리메뉴");
        Product product = new Product("후라이드", new BigDecimal(16_000));

        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProductCreateRequest.toEntity(product));

        Menu menu = menuCreateRequest.toEntity(menuGroup, menuProducts);

        given(menuGroupDao.findById(any())).willReturn(Optional.ofNullable(menuGroup));
        given(productDao.findById(any())).willReturn(Optional.ofNullable(product));
        given(menuDao.save(any())).willReturn(menu);

        // when
        MenuResponse result = menuService.create(menuCreateRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(menu.getName());
        assertThat(result.getPrice()).isEqualTo(menu.getPriceValue());
        assertThat(result.getMenuGroup().getName()).isEqualTo(menuGroup.getName());
        assertThat(result.getMenuProducts().size()).isEqualTo(menu.getMenuProducts().size());
    }

    @Test
    @DisplayName("메뉴그룹 없이 메뉴를 등록할 수 없다.")
    void create_not_exist_menu_grourp_id() {
        // given
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest();

        given(menuGroupDao.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 메뉴 그룹입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 상품이면 메뉴를 등록할 수 없다.")
    void create_not_exist_product() {
        // given
        MenuGroup menuGroup = new MenuGroup("두마리메뉴");
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(1);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(new BigDecimal(16_000),
                new ArrayList<>(Arrays.asList(menuProductCreateRequest)));

        given(menuGroupDao.findById(any())).willReturn(Optional.ofNullable(menuGroup));
        given(productDao.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 상품입니다.");
    }

    @Test
    @DisplayName("메뉴의 가격이 없으면 메뉴를 등록할 수 없다.")
    void create_price_null() {
        // given
        MenuGroup menuGroup = new MenuGroup("두마리메뉴");
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(1);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(null,
                new ArrayList<>(Arrays.asList(menuProductCreateRequest)));
        Product product = new Product("후라이드", new BigDecimal(16_000));

        given(menuGroupDao.findById(any())).willReturn(Optional.ofNullable(menuGroup));
        given(productDao.findById(any())).willReturn(Optional.ofNullable(product));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("메뉴의 가격이 0보다 작으면 메뉴를 등록할 수 없다.")
    void create_price_negative() {
        // given
        MenuGroup menuGroup = new MenuGroup("두마리메뉴");
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(1);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(new BigDecimal(-1),
                new ArrayList<>(Arrays.asList(menuProductCreateRequest)));
        Product product = new Product("후라이드", new BigDecimal(16_000));

        given(menuGroupDao.findById(any())).willReturn(Optional.ofNullable(menuGroup));
        given(productDao.findById(any())).willReturn(Optional.ofNullable(product));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("메뉴 가격이 메뉴 상품 가격들의 합보다 크면 메뉴를 등록할 수 없다.")
    void create_price_greater_than_sum() {
        // given
        MenuGroup menuGroup = new MenuGroup("두마리메뉴");
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(1);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(new BigDecimal(17_000),
                new ArrayList<>(Arrays.asList(menuProductCreateRequest)));
        Product product = new Product("후라이드", new BigDecimal(16_000));

        given(menuGroupDao.findById(any())).willReturn(Optional.ofNullable(menuGroup));
        given(productDao.findById(any())).willReturn(Optional.ofNullable(product));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 가격이 메뉴 상품 가격들의 합보다 큽니다.");
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void list() {
        // given
        List<MenuProduct> 후라이드_메뉴상품_목록 = new ArrayList<>();
        후라이드_메뉴상품_목록.add(new MenuProduct(new Product("후라이드", new BigDecimal(16_000)), 1));
        Menu 후라이드메뉴 = new Menu("후라이드치킨", new BigDecimal(16_000), new MenuGroup("두마리메뉴"), 후라이드_메뉴상품_목록);

        List<MenuProduct> 양념치킨_메뉴상품_목록 = new ArrayList<>();
        양념치킨_메뉴상품_목록.add(new MenuProduct(new Product("양념치킨", new BigDecimal(16_000)), 1));
        Menu 양념치킨메뉴 = new Menu("양념치킨", new BigDecimal(16_000), new MenuGroup("두마리메뉴"), 양념치킨_메뉴상품_목록);

        List<Menu> menus = new ArrayList<>(Arrays.asList(후라이드메뉴, 양념치킨메뉴));

        given(menuDao.findAll()).willReturn(menus);

        // when
        List<MenuResponse> result = menuService.list();

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(menus.size());
    }
}
