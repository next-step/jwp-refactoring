package kitchenpos.menugroup.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuExistMenuGroupMenuCreateValidator;
import kitchenpos.menu.domain.validator.MenuPriceMenuCreateValidator;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.product.application.ProductServiceTest.getProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;


@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuExistMenuGroupMenuCreateValidator notFoundMenuGroupValidator;
    @Mock
    private MenuPriceMenuCreateValidator menuPriceMenuCreateValidator;
    @InjectMocks
    private MenuService menuService;

    private Product 양지쌀국수;
    private Product 분짜;

    @BeforeEach
    void setUp() {
        양지쌀국수 = getProduct(1L, "양지쌀국수", 7_500);
        분짜 = getProduct(2L, "분짜", 9_500);
    }

    @DisplayName("메뉴의 이름, 가격과 메뉴 그룹의 아이디, 메뉴상품그룹을 통해 메뉴를 생성할 수 있다.")
    @Test
    void create() {
        // given
        MenuRequest createRequest = getCreateRequest(
                "대표메뉴",
                17_000,
                1L,
                Arrays.asList(
                        new MenuProductRequest(양지쌀국수.getId(), 10),
                        new MenuProductRequest(분짜.getId(), 6)
                )
        );

        Menu expected = getMenu(1L, "추천메뉴",
                17_000,
                1L,
                Arrays.asList(
                        getMenuProduct(1L, 양지쌀국수, 10),
                        getMenuProduct(2L, 분짜, 6)
                ));


        doNothing().when(notFoundMenuGroupValidator).validate(any());
        given(menuRepository.save(any(Menu.class))).willReturn(expected);

        // when
        MenuResponse actual = menuService.create(createRequest);
        // then
        assertThat(actual).isEqualTo(MenuResponse.of(expected));
    }


    @DisplayName("메뉴를 생성할 수 없는 경우")
    @Nested
    class CreateFailTest {

        @DisplayName("메뉴 가격은 0보다 작을 경우")
        @Test
        void createByZeroMoreLessPrice() {
            // given
            MenuRequest createRequest = getCreateRequest(
                    "대표메뉴",
                    -10,
                    1L,
                    Arrays.asList(
                            new MenuProductRequest(양지쌀국수.getId(), 10),
                            new MenuProductRequest(분짜.getId(), 6)
                    )
            );
            // when
            ThrowableAssert.ThrowingCallable createCall = () -> menuService.create(createRequest);
            // then
            assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 그룹의 아이디에 따른 메뉴 그룹이 존재하지 않을 경우")
        @Test
        void createByNotExistMenuGroup() {
            // given
            MenuRequest createRequest = getCreateRequest(
                    "대표메뉴",
                    19_000,
                    1L,
                    Arrays.asList(
                            new MenuProductRequest(양지쌀국수.getId(), 10),
                            new MenuProductRequest(분짜.getId(), 6)
                    )
            );
            doThrow(new IllegalArgumentException()).when(notFoundMenuGroupValidator).validate(any());
            // when
            ThrowableAssert.ThrowingCallable createCall = () -> menuService.create(createRequest);
            // then
            assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 가격의 유효성 검사가 통과하지 않았을 경우")
        @Test
        void createByIllegalPrice() {
            // given
            MenuRequest createRequest = getCreateRequest(
                    "대표메뉴",
                    1212,
                    1L,
                    Arrays.asList(
                            new MenuProductRequest(1L, 10),
                            new MenuProductRequest(2L, 13)
                    )
            );

            // given
            doThrow(new IllegalArgumentException()).when(menuPriceMenuCreateValidator).validate(any());
            // when
            ThrowableAssert.ThrowingCallable createCall = () -> menuService.create(createRequest);
            // then
            assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴의 목록을 조회 할 수 있다.")
    @Test
    void list() {
        // given
        Menu 대표메뉴 = getMenu(1L, "대표메뉴",
                17_000,
                1L,
                Arrays.asList(
                        getMenuProduct(1L, 양지쌀국수, 10),
                        getMenuProduct(2L, 분짜, 6)
                ));

        Menu 추천메뉴 = getMenu(1L, "추천메뉴",
                17_000,
                1L,
                Arrays.asList(
                        getMenuProduct(1L, 양지쌀국수, 10),
                        getMenuProduct(2L, 분짜, 6)
                ));

        final List<Menu> expected = Arrays.asList(대표메뉴, 추천메뉴);
        given(menuRepository.findAll()).willReturn(expected);
        // when
        List<MenuResponse> list = menuService.list();
        // then
        assertThat(list).containsExactlyElementsOf(Arrays.asList(MenuResponse.of(대표메뉴), MenuResponse.of(추천메뉴)));

    }


    public static MenuProduct getMenuProduct(Long id, Product product, int quantity) {
        return MenuProduct.generate(id, product.getId(), quantity);
    }


    public static Menu getMenu(long id, String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.generate(id, name, menuProducts, menuGroupId, BigDecimal.valueOf(price));

    }

    private MenuRequest getCreateRequest(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

}
