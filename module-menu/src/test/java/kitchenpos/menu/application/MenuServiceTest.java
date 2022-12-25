package kitchenpos.menu.application;

import kitchenpos.menu.validator.MenuProductValidator;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static kitchenpos.menu.application.MenuService.MENU_GROUP_NOT_EXIST_EXCEPTION_MESSAGE;
import static kitchenpos.menu.domain.Menu.PRICE_NOT_NULL_EXCEPTION_MESSAGE;
import static kitchenpos.menu.domain.fixture.MenuFixture.menuA;
import static kitchenpos.menu.domain.fixture.MenuGroupFixture.menuGroupA;
import static kitchenpos.menu.domain.fixture.MenuProductsFixture.menuProducts;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@DisplayName("MenuService")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductValidator menuProductValidator;

    @DisplayName("가격을 필수값으로 갖는다.")
    @ParameterizedTest
    @NullSource
    void create_fail_MenuGroupNull(BigDecimal price) {
        Assertions.assertThatThrownBy(() -> menuService.create(new MenuCreateRequest(menuProducts(1L), 1L, price, "A")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("가격은 0원보다 작을 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"-1"})
    void create_fail_minimumPrice(BigDecimal price) {
        Assertions.assertThatThrownBy(() -> menuService.create(new MenuCreateRequest(menuProducts(1L), 1L, price, "A")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(Price.PRICE_MINIMUM_EXCEPTION_MESSAGE);
    }

    @DisplayName("메뉴 그룹이 없을 경우 메뉴를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"1"})
    void create_fail_menuGroup(BigDecimal price) {
        Assertions.assertThatThrownBy(() -> menuService.create(new MenuCreateRequest(menuProducts(1L), null, price, "menuA")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MENU_GROUP_NOT_EXIST_EXCEPTION_MESSAGE);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품의 합보다 클 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"3"})
    void create_fail_priceSum(BigDecimal price) {

        given(menuGroupRepository.findById(1L)).willReturn(Optional.of(menuGroupA()));

        doThrow(new IllegalArgumentException("메뉴의 가격이 메뉴 상품의 합보다 클 수 없다."))
                .when(menuProductValidator).validate(any());

        Assertions.assertThatThrownBy(() -> menuService.create(new MenuCreateRequest(menuProducts(1L), 1L, price, "menuA")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1"})
    void create_success(BigDecimal price) {

        given(menuGroupRepository.findById(1L)).willReturn(Optional.of(menuGroupA()));

        given(menuRepository.save(ArgumentMatchers.any())).willReturn(menuA(1L));

        MenuResponse response = menuService.create(new MenuCreateRequest(menuProducts(1L), 1L, price, menuGroupA().getName()));

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo(menuA(1L).getName().getName()),
                () -> assertEquals(0, response.getPrice().compareTo(menuA(1L).getPrice().getPrice())),
                () -> assertThat(response.getMenuProducts()).hasSize(2)
        );
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        given(menuRepository.findAll()).willReturn(Collections.singletonList(menuA(1L)));
        assertThat(menuService.list()).hasSize(1);
    }
}
