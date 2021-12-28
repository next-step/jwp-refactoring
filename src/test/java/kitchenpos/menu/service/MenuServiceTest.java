package kitchenpos.menu.service;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuValidator menuValidator;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성() {
        // given
        MenuGroup menuGroup = MenuGroup.of("튀김류");
        Product product = Product.of("양념치킨", Price.of(BigDecimal.valueOf(5000)));

        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);
        MenuRequest menuRequest = new MenuRequest("치킨", BigDecimal.valueOf(5000), menuGroup.getId(), Collections.singletonList(menuProductRequest));

        Menu menu = Menu.of("치킨", Price.of(BigDecimal.valueOf(5000)), 1L, new ArrayList<>());
        Menu savedMenu = Menu.of("치킨", Price.of(BigDecimal.valueOf(5000)), 1L, Collections.singletonList(MenuProduct.of(1L, Quantity.of(1L))));

        given(menuRepository.save(menu)).willReturn(savedMenu);

        // when
        MenuResponse response = menuService.create(menuRequest);

        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(menuRequest.getName()),
                () -> assertThat(response.getMenuProducts().size()).isEqualTo(1)
        );
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void 메뉴_목록_조회() {
        // given
        Menu savedMenu = Menu.of("치킨", Price.of(BigDecimal.valueOf(5000)), 1L, Collections.singletonList(MenuProduct.of(1L, Quantity.of(1L))));

        given(menuRepository.findAll()).willReturn(Collections.singletonList(savedMenu));

        // when
        List<MenuResponse> response = menuService.list();

        // then
        assertThat(response.size()).isEqualTo(1);
    }
}
