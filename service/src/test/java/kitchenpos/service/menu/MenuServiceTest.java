package kitchenpos.service.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.service.menu.application.MenuService;
import kitchenpos.service.menu.application.MenuValidator;
import kitchenpos.service.menu.dto.MenuProductRequest;
import kitchenpos.service.menu.dto.MenuProductResponse;
import kitchenpos.service.menu.dto.MenuRequest;
import kitchenpos.service.menu.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuDao;
    @Mock
    private MenuValidator menuValidator;
    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    void create() {
        //given
        MenuRequest request = new MenuRequest("menu1", 10000L, 1L,
                Arrays.asList(new MenuProductRequest(1L, 10), new MenuProductRequest(2L, 20)));

        //when
        MenuResponse savedMenu = menuService.create(request);

        //then
        assertThat(savedMenu.getProducts().stream().map(MenuProductResponse::getQuantity).collect(Collectors.toList()))
                .isNotEmpty().containsExactlyInAnyOrder(10, 20);

    }

    @Test
    @DisplayName("전체 메뉴 그룹을 조회할 수 있다.")
    void list() {
        //given
        Menu menu = new Menu("menu1", 10000L, 1L);
        given(menuDao.findAll()).willReturn(Arrays.asList(menu));

        //then
        assertThat(menuService.list()).isNotEmpty();
    }
}
