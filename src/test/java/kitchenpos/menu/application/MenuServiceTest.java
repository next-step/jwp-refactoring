package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuRequest.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.MenuResponse.MenuProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create() {
        List<MenuProductRequest> menuProductRequests = Arrays.asList(new MenuProductRequest(1L, 2));
        MenuRequest given = new MenuRequest("두마리치킨", BigDecimal.valueOf(15000), 1L, menuProductRequests);
        Menu expected = given.toEntity();
        given(menuRepository.save(any())).willReturn(expected);

        MenuResponse actual = menuService.create(given);

        verify(menuValidator).validatePrice(any(Menu.class));
        verify(menuValidator).validateMenuGroup(any(Menu.class));
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
                () -> assertThat(actual.getMenuProducts().stream().map(MenuProductResponse::getProductId)).contains(1L)
        );
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        Menu twoChicken = new Menu("두마리치킨", BigDecimal.valueOf(15000), 1L);
        Menu soyChicken = new Menu("간장치킨", BigDecimal.valueOf(20000), 1L);
        given(menuRepository.findAll()).willReturn(Arrays.asList(twoChicken, soyChicken));

        List<MenuResponse> actual = menuService.list();

        assertThat(actual).hasSize(2);
        assertThat(actual.stream().map(MenuResponse::getName)).contains(twoChicken.getName(), soyChicken.getName());
    }
}
