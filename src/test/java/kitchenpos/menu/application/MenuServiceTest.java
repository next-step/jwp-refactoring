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

import org.junit.jupiter.api.*;
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
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.exception.MenuGroupNotFoundException;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductNotFoundException;

@DisplayName("메뉴 서비스")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MenuGroupService menuGroupService;
    @Mock
    private ProductService productService;
    @InjectMocks
    private MenuService menuService;

    @BeforeEach
    void setUp() {

    }

    @TestFactory
    @DisplayName("전체 메뉴 조회")
    List<DynamicTest> find_allMenu() {
        // given
        Product product1 = new Product("A", BigDecimal.valueOf(1000));
        Product product2 = new Product("B", BigDecimal.valueOf(2000));
        MenuGroup menuGroup = new MenuGroup("1");
        Menu menu = new Menu("AB", BigDecimal.valueOf(3000), menuGroup);
        menu.addMenuProduct(new MenuProduct(menu, product1, 1L));
        menu.addMenuProduct(new MenuProduct(menu, product2, 1L));
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
        MenuGroup menuGroup = new MenuGroup("A");
        MenuProductRequest menuProductRequest1 = new MenuProductRequest(1L, 1L);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(2L, 1L);
        MenuRequest menuRequest = new MenuRequest("Aa", BigDecimal.valueOf(12000.00), 1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2));
        // and
        Product product1 = new Product("a", BigDecimal.valueOf(8000.00));
        Product product2 = new Product("b", BigDecimal.valueOf(4000.00));
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        // and
        given(menuGroupService.findById(anyLong())).willReturn(menuGroup);
        given(productService.findById(1L)).willReturn(product1);
        given(productService.findById(2L)).willReturn(product2);
        given(menuRepository.save(any(Menu.class))).willReturn(menu);

        // when
        MenuResponse menuResponse = menuService.create(menuRequest);

        // then
        return Arrays.asList(
                dynamicTest("메뉴 ID 확인됨.", () -> assertThat(menuResponse.getName()).isEqualTo(menu.getName().toString())),
                dynamicTest("메뉴 그룹 ID 확인됨.", () -> assertThat(menuResponse.getMenuGroupResponse().getName()).isEqualTo(menuGroup.getMenuGroupName().toString())),
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
                    given(menuGroupService.findById(1L)).willReturn(new MenuGroup("A"));

                    // then
                    assertThatThrownBy(() -> menuService.create(menuRequest))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("메뉴금액 음수 입력 오류 발생함.", () -> {
                    // given
                    MenuRequest menuRequest = new MenuRequest("Aa", BigDecimal.valueOf(-1), 2L, new ArrayList<>());
                    given(menuGroupService.findById(2L)).willReturn(new MenuGroup("A"));

                    // then
                    assertThatThrownBy(() -> menuService.create(menuRequest))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("금액은 Null이거나 0보다 작을 수 없습니다.");
                }),
                dynamicTest("등록되지 않은 메뉴그룹으로 등록 시도 시 오류 발생함.", () -> {
                    // given
                    MenuRequest menuRequest = new MenuRequest("Aa", BigDecimal.valueOf(1000), 100L, new ArrayList<>());
                    given(menuGroupService.findById(100L)).willThrow(MenuGroupNotFoundException.class);

                    // then
                    assertThatThrownBy(() -> menuService.create(menuRequest))
                            .isInstanceOf(MenuGroupNotFoundException.class);
                }),
                dynamicTest("메뉴상품에 등록된 상품 ID로 상품조회 실패 시 오류 발생함.", () -> {
                    // given
                    MenuGroup menuGroup = new MenuGroup("A");
                    MenuRequest menuRequest = new MenuRequest("Aa", BigDecimal.valueOf(1000), 3L,
                            Arrays.asList(new MenuProductRequest(1L, 1L)));
                    given(menuGroupService.findById(3L)).willReturn(menuGroup);
                    given(productService.findById(1L)).willThrow(ProductNotFoundException.class);

                    // then
                    assertThatThrownBy(() -> menuService.create(menuRequest))
                            .isInstanceOf(ProductNotFoundException.class);
                }),
                dynamicTest("메뉴에 포함된 상품의 가격 X 개수의 합계 금액이 메뉴의 가격보다 작을 경우 오류 발생함.", () -> {
                    // given
                    // given
                    MenuGroup menuGroup = new MenuGroup("A");
                    MenuProductRequest menuProductRequest1 = new MenuProductRequest(2L, 1L);
                    MenuProductRequest menuProductRequest2 = new MenuProductRequest(3L, 1L);
                    MenuRequest menuRequest = new MenuRequest("Aa", BigDecimal.valueOf(13000.00), 4L,
                            Arrays.asList(menuProductRequest1, menuProductRequest2));
                    // and
                    Product product1 = new Product("a", BigDecimal.valueOf(8000.00));
                    Product product2 = new Product("b", BigDecimal.valueOf(4000.00));
                    Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
                    // and
                    given(menuGroupService.findById(4L)).willReturn(menuGroup);
                    given(menuRepository.save(any(Menu.class))).willReturn(menu);
                    given(productService.findById(2L)).willReturn(product1);
                    given(productService.findById(3L)).willReturn(product2);

                    // then
                    assertThatThrownBy(() -> menuService.create(menuRequest))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("메뉴 급액이 제품 합계금액보다 클 수 없습니다.");
                })
        );
    }
}
