package kitchenpos.application;

import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("메뉴 서비스")
public class MenuServiceTest extends ServiceTestBase {
    private final MenuService menuService;

    @Autowired
    public MenuServiceTest(MenuService menuGroupService) {
        this.menuService = menuGroupService;
    }

    @BeforeEach
    void setUp() {
        setUpProduct();
        setUpMenuGroup();
        setUpMenu();
        setUpMenuProduct();
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void createMenu() {
        Menu savedMenu = menuService.create(menu);
        assertThat(savedMenu.getName()).isEqualTo(menu.getName());
    }

    @DisplayName("가격이 부적합한 메뉴를 등록한다")
    @ParameterizedTest
    @MethodSource
    void createMenuWithIllegalArguments(BigDecimal price) {
        menu.setPrice(price);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    private static Stream<Arguments> createMenuWithIllegalArguments() {
        return Stream.of(
                Arguments.of((Object)null),
                Arguments.of(new BigDecimal(-1)),
                Arguments.of(new BigDecimal(300000))
        );
    }

    @DisplayName("메뉴 그룹이 등록되지 않은 메뉴를 등록한다")
    @Test
    void createMenuWithNotExistsMenuGroup() {
        menu.setMenuGroupId(10L);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("제품이 등록되지 않은 메뉴를 등록한다")
    @Test
    void createMenuWithNotExistsProduct() {
        menuProduct.setProductId(10L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("제품을 조회한다")
    @Test
    void findAllProduct() {
        List<Menu> menus = menuService.list();
        assertThat(menus.size()).isEqualTo(1);
        assertThat(menus.get(0)).isEqualTo(menu);
    }
}
