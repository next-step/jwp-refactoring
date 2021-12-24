package kitchenpos.menu.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.domain.Price;
import kitchenpos.common.vo.MenuGroupId;
import kitchenpos.common.vo.ProductId;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴가 저장된다.")
    @Test
    void craete_menu() {
        // given
        MenuProduct 뿌링클콤보_뿌링클치킨 = MenuProduct.of(ProductId.of(1L), 1L);
        MenuProduct 뿌링클콤보_치킨무 = MenuProduct.of(ProductId.of(2L), 2L);
        MenuProduct 뿌링클콤보_코카콜라 = MenuProduct.of(ProductId.of(3L), 3L);

        Menu 뿌링클콤보 = Menu.of("뿌링클콤보", Price.of(18_000), MenuGroupId.of(1L), MenuProducts.of(List.of(뿌링클콤보_뿌링클치킨, 뿌링클콤보_치킨무, 뿌링클콤보_코카콜라)));

        when(menuValidator.getValidatedMenu(any(MenuDto.class))).thenReturn(뿌링클콤보);
        when(menuRepository.save(any(Menu.class))).thenReturn(뿌링클콤보);

        MenuDto 메뉴생성_요청전문 = MenuDto.of("뿌링클콤보", BigDecimal.valueOf(18_000), 1L, List.of(MenuProductDto.of(1L, 1L), MenuProductDto.of(2L, 2L), MenuProductDto.of(3L, 3L)));

        // when
        MenuDto savedMenu = menuService.create(메뉴생성_요청전문);

        // then
        Assertions.assertThat(savedMenu.getName()).isEqualTo("뿌링클콤보");
        Assertions.assertThat(savedMenu.getPrice()).isEqualTo(BigDecimal.valueOf(18_000));
        Assertions.assertThat(savedMenu.getMenuProducts()).isEqualTo(List.of(MenuProductDto.of(1L, 1L), MenuProductDto.of(2L, 2L), MenuProductDto.of(3L, 3L)));
    }

    @DisplayName("메뉴가 조회된다.")
    @Test
    void search_menu() {
        // given
        Menu 뿌링클콤보 = Menu.of("뿌링클콤보", Price.of(18_000), MenuGroupId.of(1L));

        when(menuRepository.findAll()).thenReturn(List.of(뿌링클콤보));

        // when
        List<MenuDto> searchedMenu = menuService.list();

        // then
        Assertions.assertThat(searchedMenu).isEqualTo(List.of(MenuDto.of("뿌링클콤보", BigDecimal.valueOf(18_000), 1L, Lists.newArrayList())));
    }
}
