package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuRequest;
import kitchenpos.event.MenuCreatedEvent;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @DisplayName("메뉴를 등록한다.")
    @Test
    void createTest() {

        MenuGroup 한마리메뉴 = new MenuGroup("한마리메뉴");
        Product 후라이드 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));

        Menu 후라이드치킨 = new Menu("후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴);

        MenuProduct 후라이드치킨_상품 = new MenuProduct(후라이드치킨, 후라이드, 1L);
        후라이드치킨.addMenuProduct(후라이드치킨_상품);

        when(menuGroupRepository.findById(후라이드치킨.getMenuGroup().getId())).thenReturn(Optional.of(한마리메뉴));
        MenuService menuService = new MenuService(menuRepository, menuGroupRepository, eventPublisher);

        Menu expectedMenu = mock(Menu.class);
        when(expectedMenu.getId()).thenReturn(1L);
        when(expectedMenu.getName()).thenReturn("후라이드치킨");
        when(expectedMenu.getPrice()).thenReturn(BigDecimal.valueOf(16000));

        when(menuRepository.save(후라이드치킨)).thenReturn(expectedMenu);

        // when
        Menu created_후라이드치킨 = menuService.create(MenuRequest.from(후라이드치킨));
        // then
        verify(eventPublisher).publishEvent(any(MenuCreatedEvent.class));
        assertThat(created_후라이드치킨.getId()).isNotNull();
        assertThat(created_후라이드치킨.getName()).isEqualTo(후라이드치킨.getName());
        assertThat(created_후라이드치킨.getPrice()).isEqualTo(후라이드치킨.getPrice());
    }

    @DisplayName("메뉴의 목록을 조회한다.")
    @Test
    void getListTest() {
        // given
        Menu menu = mock(Menu.class);

        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu));
        MenuService menuService = new MenuService(menuRepository, menuGroupRepository, eventPublisher);

        // when
        List<Menu> menus = menuService.list();
        // then
        assertThat(menus).contains(menu);
    }

}
