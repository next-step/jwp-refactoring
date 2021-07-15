package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuProduct;

@DisplayName("메뉴 서비스")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private ProductMenuService productMenuService;
    @Mock
    private MenuGroupMenuValidator menuGroupMenuValidator;
    @Mock
    private MenuValidator menuValidator;
    @InjectMocks
    private MenuService menuService;

    @TestFactory
    @DisplayName("전체 메뉴 조회")
    List<DynamicTest> find_allMenu() {
        // given
        Menu menu = new Menu("AB", BigDecimal.valueOf(3000), 1L);
        menu.addMenuProduct(new MenuProduct(menu, 1L, 1L));
        menu.addMenuProduct(new MenuProduct(menu, 2L, 1L));
        given(menuRepository.findAll()).willReturn(Arrays.asList(menu));

        // when
        List<MenuResponse> menuResponses = menuService.findAllMenu();

        // then
        return Arrays.asList(
                dynamicTest("결과 메뉴 목록 크기 확인.", () -> assertThat(menuResponses.size()).isOne()),
                dynamicTest("결과 메뉴 목록의 이름 확인", () -> assertThat(menuResponses).extracting("name").contains(menu.getName().toString())),
                dynamicTest("메뉴에 포함된 메뉴상품 목록 확인.", () -> {
                    List<MenuProductResponse> collect = menuResponses.stream()
                            .flatMap(menuResponse -> menuResponse.getMenuProductResponses().stream())
                            .collect(Collectors.toList());
                    assertThat(collect).size().isEqualTo(2);
                })
        );
    }

    @TestFactory
    @DisplayName("메뉴를 등록한다.")
    List<DynamicTest> create_menu() {
        // given
        MenuProductRequest menuProductRequest1 = new MenuProductRequest(1L, 1L);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(2L, 1L);
        MenuRequest menuRequest = new MenuRequest("Aa", BigDecimal.valueOf(12000.00), 1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2));
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), 1L);
        // and
        given(menuRepository.save(any(Menu.class))).willReturn(menu);
        given(productMenuService.calculateProductsPrice(1L, 1L)).willReturn(BigDecimal.valueOf(8000.00));
        given(productMenuService.calculateProductsPrice(2L, 1L)).willReturn(BigDecimal.valueOf(4000.00));

        // when
        MenuResponse menuResponse = menuService.create(menuRequest);

        // then
        verify(menuValidator).validateMenuPrice(any(Menu.class), any(BigDecimal.class));
        verify(menuGroupMenuValidator).validateExistsMenuGroupById(anyLong());
        return Arrays.asList(
                dynamicTest("메뉴 ID 확인됨.", () -> assertThat(menuResponse.getName()).isEqualTo(menu.getName().toString())),
                dynamicTest("메뉴상품의 상품 ID 확인됨.", () -> assertThat(menuResponse.getMenuProductResponses()).size().isEqualTo(2))
        );
    }

    @TestFactory
    @DisplayName("메뉴 등록 시 예외발생 상황")
    List<DynamicTest> exceptions() {
        return Arrays.asList(
                dynamicTest("메뉴금액 미입력 오류 발생함.", () -> {
                    // given
                    MenuRequest menuRequest = new MenuRequest("Aa", null, 1L, new ArrayList<>());

                    // then
                    assertThatThrownBy(() -> menuService.create(menuRequest))
                            .isInstanceOf(IllegalArgumentException.class);
                    verify(menuGroupMenuValidator).validateExistsMenuGroupById(1L);
                }),
                dynamicTest("메뉴금액 음수 입력 오류 발생함.", () -> {
                    // given
                    MenuRequest menuRequest = new MenuRequest("Aa", BigDecimal.valueOf(-1), 2L, new ArrayList<>());

                    // then
                    assertThatThrownBy(() -> menuService.create(menuRequest))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("금액은 Null이거나 0보다 작을 수 없습니다.");
                }),
                dynamicTest("등록되지 않은 메뉴그룹으로 등록 시도 시 오류 발생함.", () -> {
                    // given
                    MenuRequest menuRequest = new MenuRequest("Aa", BigDecimal.valueOf(1000), 100L, new ArrayList<>());
                    doThrow(RuntimeException.class).when(menuGroupMenuValidator).validateExistsMenuGroupById(100L);
                    // then
                    assertThatThrownBy(() -> menuService.create(menuRequest))
                            .isInstanceOf(RuntimeException.class);
                })
        );
    }
}
