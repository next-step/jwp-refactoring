package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuFactory menuFactory;
    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 생성한다")
    @Test
    void testCreate() {
        // given
        MenuRequest menuRequest = new MenuRequest();
        Menu expectedMenu = new Menu("대표메뉴", 16000L, new MenuGroup());

        given(menuFactory.create(any(MenuRequest.class))).willReturn(expectedMenu);
        given(menuRepository.save(any(Menu.class))).willReturn(expectedMenu);

        // when
        MenuResponse menu = menuService.create(menuRequest);

        // then
        assertThat(menu).isEqualTo(MenuResponse.of(expectedMenu));
    }

    @DisplayName("모든 메뉴를 조회한다")
    @Test
    void testList() {
        // given
        List<Menu> expectedMenus = Arrays.asList(new Menu(1L, "대표 메뉴", 16000, new MenuGroup(), Collections.emptyList()));
        given(menuRepository.findAll()).willReturn(expectedMenus);

        // when
        List<MenuResponse> menus = menuService.list();

        // then
        assertThat(menus).isEqualTo(MenuResponse.ofList(expectedMenus));
    }
}
