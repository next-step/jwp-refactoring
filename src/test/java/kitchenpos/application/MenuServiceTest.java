package kitchenpos.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private MenuService menuService;

    private Product 허니콤보;
    private Product 뿌링클순살;
    private Product 치즈볼;
    private Product 콜라;
    private MenuGroup 인기그룹;
    private Menu 허니콤보세트;
    private Menu 뿌링클순살세트;

    @BeforeEach
    void setUp() {
        인기그룹 = new MenuGroup("인기그룹");
        허니콤보 = new Product("허니콤보", BigDecimal.valueOf(20000));
        뿌링클순살 = new Product( "뿌링클순살", BigDecimal.valueOf(22000));
        치즈볼 = new Product("치즈볼", BigDecimal.valueOf(5000));
        콜라 = new Product("콜라", BigDecimal.valueOf(2000));
        허니콤보세트 = new Menu("허니콤보세트", BigDecimal.valueOf(22000), 인기그룹);
        뿌링클순살세트 = new Menu("뿌링클순살세트", BigDecimal.valueOf(24000), 인기그룹);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성() {
        // given
        when(menuRepository.save(허니콤보세트)).thenReturn(허니콤보세트);

        // when
        Menu savedMenu = menuService.create(허니콤보세트);

        // then
        assertThat(savedMenu).isEqualTo(허니콤보세트);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void 메뉴_목록_조회() {
        // given
        List<Menu> menus = Arrays.asList(허니콤보세트, 뿌링클순살세트);
        when(menuService.list()).thenReturn(menus);

        // when
        List<Menu> selectMenus = menuService.list();

        // then
        assertAll(
                () -> assertThat(selectMenus).hasSize(menus.size()),
                () -> assertThat(selectMenus).isEqualTo(menus)
        );
    }
}
