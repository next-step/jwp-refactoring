package kitchenpos.menu.application;

import kitchenpos.exception.KitchenposException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_MENU_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductValidator productValidator;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴 생성 테스트")
    @Test
    void createTest() {
        // given
        MenuProduct 메뉴_불고기 = new MenuProduct(1l, 3);
        MenuGroup 메뉴_그룹 = new MenuGroup("메뉴그룹");

        MenuRequest menuRequest = MenuRequest.Builder.of("메뉴1", BigDecimal.valueOf(2000L))
                                                     .menuGroupId(1L)
                                                     .menuProducts(Arrays.asList(new MenuProductRequest(1L, 1000)))
                                                     .build();

        Mockito.when(menuGroupRepository.findById(any())).thenReturn(Optional.of(메뉴_그룹));
        Mockito.when(menuRepository.save(any())).thenReturn(menuRequest.toMenu(메뉴_그룹, Arrays.asList(메뉴_불고기)));

        // when
        MenuResponse result = menuService.create(menuRequest);

        // then
        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("메뉴1");
        });
        Mockito.verify(menuGroupRepository).findById(any());
        Mockito.verify(menuRepository).save(any());
    }

    @DisplayName("메뉴 그룹이 없는 경우")
    @Test
    void notExistedMenuGroup() {
        // given
        MenuRequest menuRequest = MenuRequest.Builder.of("메뉴1", BigDecimal.valueOf(2000L))
                                                     .menuGroupId(1L)
                                                     .menuProducts(
                                                         Arrays.asList(new MenuProductRequest()))
                                                     .build();
        Mockito.when(menuGroupRepository.findById(any())).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(KitchenposException.class)
            .hasMessageContaining(NOT_FOUND_MENU_GROUP.getMessage());
    }

}
