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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    MenuDao menuDao;
    @Mock
    MenuGroupDao menuGroupDao;
    @Mock
    MenuProductDao menuProductDao;
    @Mock
    ProductDao productDao;
    @InjectMocks
    MenuService menuService;

    MenuGroup recommendedMenuGroup;
    Product chicken;
    Product pizza;
    Menu menuRequest;

    @BeforeEach
    void setup() {
        recommendedMenuGroup = new MenuGroup(1L, "추천메뉴");
        chicken = new Product(1L, "치킨", BigDecimal.valueOf(20000));
        pizza = new Product(2L, "피자", BigDecimal.valueOf(25000));
        menuRequest = new Menu("후라이드+피자", BigDecimal.valueOf(19000), recommendedMenuGroup.getId(),
                Arrays.asList(new MenuProduct(chicken.getId(), 1), new MenuProduct(pizza.getId(), 1)));
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        // Given
        given(menuGroupDao.existsById(menuRequest.getMenuGroupId())).willReturn(true);
        given(productDao.findById(chicken.getId())).willReturn(Optional.of(chicken));
        given(productDao.findById(pizza.getId())).willReturn(Optional.of(pizza));
        given(menuDao.save(menuRequest)).willReturn(new Menu(1L, "후라이드+피자", BigDecimal.valueOf(19000), recommendedMenuGroup.getId(),
                Arrays.asList(new MenuProduct(chicken.getId(), 1), new MenuProduct(pizza.getId(), 1))));

        // When
        Menu menuResponse = menuService.create(menuRequest);

        // Then
        assertAll(
                () -> assertThat(menuResponse.getId()).isNotNull(),
                () -> assertThat(menuResponse.getMenuGroupId()).isEqualTo(recommendedMenuGroup.getId()),
                () -> assertThat(menuResponse.getName()).isEqualTo(menuRequest.getName()),
                () -> assertThat(menuResponse.getPrice()).isEqualTo(menuRequest.getPrice())
        );
    }

    @DisplayName("메뉴 가격 관련 에러 처리")
    @Test
    void menuPriceException() {
        // Given
        Menu minusPriceMenuRequest = new Menu("후라이드+후라이드", BigDecimal.valueOf(-1), recommendedMenuGroup.getId(),
                Arrays.asList(new MenuProduct(chicken.getId(), 1), new MenuProduct(pizza.getId(), 1)));
        Menu overPriceMenuRequest = new Menu("후라이드+후라이드", BigDecimal.valueOf(-1), recommendedMenuGroup.getId(),
                Arrays.asList(new MenuProduct(chicken.getId(), 1), new MenuProduct(pizza.getId(), 1)));

        // When, then
        assertAll(
                () -> assertThatThrownBy(() -> menuService.create(minusPriceMenuRequest)).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> menuService.create(overPriceMenuRequest)).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("메뉴 그룹 존재하지 않을경우 에러처리")
    @Test
    void notExistMenuGroupException() {
        // Given
        Menu notExistMenuGroupMenuRequest = new Menu("후라이드+후라이드", BigDecimal.valueOf(-1), recommendedMenuGroup.getId(),
                Arrays.asList());

        // When, Then
        assertThatThrownBy(() -> menuService.create(notExistMenuGroupMenuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 리스트 조회")
    @Test
    void findAll() {
        // Given
        Menu savedMenu = new Menu("후라이드+피자", BigDecimal.valueOf(19000), recommendedMenuGroup.getId(),
                Arrays.asList(new MenuProduct(chicken.getId(), 1), new MenuProduct(pizza.getId(), 1)));
        given(menuDao.findAll()).willReturn(Arrays.asList(savedMenu));

        // When
        List<Menu> menusResponse = menuService.list();

        // Then
        assertAll(
                () -> assertThat(menusResponse.size()).isEqualTo(1),
                () -> assertThat(menusResponse).containsExactly(savedMenu)
        );
    }
}
