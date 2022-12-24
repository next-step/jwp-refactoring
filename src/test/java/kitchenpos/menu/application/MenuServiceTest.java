package kitchenpos.menu.application;

import kitchenpos.ServiceTest;
import kitchenpos.common.vo.Quantity;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static java.util.Collections.singletonList;
import static kitchenpos.common.fixture.NameFixture.*;
import static kitchenpos.common.fixture.PriceFixture.priceMenuA;
import static kitchenpos.common.fixture.PriceFixture.priceProductA;
import static kitchenpos.common.vo.Price.PRICE_MINIMUM_EXCEPTION_MESSAGE;
import static kitchenpos.menu.application.MenuService.MENU_GROUP_NOT_EXIST_EXCEPTION_MESSAGE;
import static kitchenpos.menu.domain.Menu.PRICE_NOT_NULL_EXCEPTION_MESSAGE;
import static kitchenpos.menu.domain.MenuValidator.MENU_PRICE_EXCEPTION_MESSAGE;
import static kitchenpos.menu.domain.fixture.MenuGroupFixture.menuGroupA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("MenuService")
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuValidator menuValidator;

    private MenuGroup menuGroupA;
    private Menu menuA;

    @BeforeEach
    public void setUp() {
        super.setUp();
        menuGroupA = menuGroupRepository.save(new MenuGroup(nameMenuGroupA()));
        Product productA = productRepository.save(new Product(1L, nameProductA(), priceProductA()));
        menuA = menuRepository.save(new Menu(nameMenuA(), priceMenuA(), menuGroupA(), new MenuProducts(singletonList(new MenuProduct(productA.getId(), new Quantity(1))))));
        menuService = new MenuService(menuRepository, menuGroupRepository, menuValidator);
    }

    @DisplayName("가격을 필수값으로 갖는다.")
    @ParameterizedTest
    @NullSource
    void create_fail_MenuGroupNull(BigDecimal price) {
        assertThatThrownBy(() -> menuService.create(new MenuCreateRequest(menuA.getMenuProducts().getMenuProducts(), menuGroupA.getId(), price, "A")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("가격은 0원보다 작을 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"-1"})
    void create_fail_minimumPrice(BigDecimal price) {
        assertThatThrownBy(() -> menuService.create(new MenuCreateRequest(menuA.getMenuProducts().getMenuProducts(), menuGroupA.getId(), price, "A")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_MINIMUM_EXCEPTION_MESSAGE);
    }

    @DisplayName("메뉴 그룹이 없을 경우 메뉴를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"1"})
    void create_fail_menuGroup(BigDecimal price) {
        assertThatThrownBy(() -> menuService.create(new MenuCreateRequest(menuA.getMenuProducts().getMenuProducts(), null, price, "menuA")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MENU_GROUP_NOT_EXIST_EXCEPTION_MESSAGE);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품의 합보다 클 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"3"})
    void create_fail_priceSum(BigDecimal price) {
        assertThatThrownBy(() -> menuService.create(new MenuCreateRequest(menuA.getMenuProducts().getMenuProducts(), menuGroupA.getId(), price, "menuA")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MENU_PRICE_EXCEPTION_MESSAGE);
    }

    @DisplayName("메뉴를 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1"})
    void create_success(BigDecimal price) {
        String name = "menuA";
        MenuResponse response = menuService.create(new MenuCreateRequest(menuA.getMenuProducts().getMenuProducts(), menuGroupA.getId(), price, name));
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo(name),
                () -> assertEquals(0, response.getPrice().compareTo(price)),
                () -> assertThat(response.getMenuProducts()).hasSize(1)
        );
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        assertThat(menuService.list()).hasSize(1);
    }
}
