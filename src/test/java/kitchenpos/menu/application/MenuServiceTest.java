package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.exception.IllegalPriceException;
import kitchenpos.menu.exception.LimitPriceException;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.ProductNotFoundException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static kitchenpos.menu.fixtures.MenuFixtures.양념치킨두마리메뉴요청;
import static kitchenpos.menu.fixtures.MenuGroupFixtures.메뉴그룹;
import static kitchenpos.menu.fixtures.MenuProductFixtures.메뉴상품_두개요청;
import static kitchenpos.menu.fixtures.ProductFixtures.양념치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

/**
 * packageName : kitchenpos.application
 * fileName : MenuServiceTest
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
@DisplayName("메뉴 통합 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    private Menu 양념치킨두마리메뉴;
    private final Product 양념치킨 = 양념치킨();
    private final MenuGroup 두마리메뉴그룹 = 메뉴그룹("두마리메뉴그룹");

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        MenuProduct menuProduct = 메뉴상품_두개요청().toEntity(양념치킨);
        양념치킨두마리메뉴 = 양념치킨두마리메뉴요청().toEntity();
    }

    @Test
    @DisplayName("메뉴를 조회할 수 있다.")
    public void list() {
        // given
        given(menuRepository.findAllJoinFetch()).willReturn(Lists.newArrayList(양념치킨두마리메뉴));

        // when
        List<MenuResponse> menus = menuService.list();

        // then
        assertThat(menus).hasSize(1);
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    public void create() {
        // given
        given(menuGroupRepository.findById(any())).willReturn(Optional.of(두마리메뉴그룹));
        given(productRepository.findById(anyLong())).willReturn(Optional.of(양념치킨));
        given(menuRepository.save(any(Menu.class))).willReturn(양념치킨두마리메뉴);

        // when
        MenuResponse actual = menuService.create(양념치킨두마리메뉴요청());

        // then
        assertAll(
                () -> assertThat(actual.getMenuProducts()).hasSize(1)
        );
    }

    @ParameterizedTest(name = "value: " + ParameterizedTest.ARGUMENTS_PLACEHOLDER)
    @ValueSource(ints = {-10, -5, -1})
    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다: int")
    public void createFailByPrice(int candidate) {
        // then
        assertThatThrownBy(() -> new Menu(
                "두마리메뉴",
                new BigDecimal(candidate),
                두마리메뉴그룹.getId(),
                Lists.newArrayList(
                        new MenuProduct(양념치킨, 2L)
                )
        )).isInstanceOf(IllegalPriceException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다: null")
    public void createFailByPriceNull() {
        // then
        assertThatThrownBy(() -> new Menu(
                "두마리메뉴",
                null,
                null,
                null)
        ).isInstanceOf(IllegalPriceException.class);
    }

    @Test
    @DisplayName("메뉴의 가격은 메뉴상품들의 수량과 가격의 합과 일치하여야 한다.")
    public void createFailByMenusPrices() {
        // then
        assertThatThrownBy(() -> new Menu(
                "가격불일치메뉴",
                new BigDecimal(Long.MAX_VALUE),
                두마리메뉴그룹.getId(),
                Lists.newArrayList(
                    new MenuProduct(양념치킨, 2L)
                ))
        ).isInstanceOf(LimitPriceException.class);
    }

    @Test
    @DisplayName("메뉴그룹이 등록되어 있어야 한다.")
    public void createFail() {
        // given
        given(menuGroupRepository.findById(any())).willThrow(MenuGroupNotFoundException.class);

        // then
        assertThatThrownBy(() -> menuService.create(양념치킨두마리메뉴요청())).isInstanceOf(MenuGroupNotFoundException.class);
    }

    @Test
    @DisplayName("메뉴상품은 상품이 등록되어 있어야 한다.")
    public void createFailByMenuProduct() {
        // given
        given(menuGroupRepository.findById(any())).willReturn(Optional.of(두마리메뉴그룹));
        given(productRepository.findById(anyLong())).willThrow(ProductNotFoundException.class);

        // then
        assertThatThrownBy(() -> menuService.create(양념치킨두마리메뉴요청())).isInstanceOf(ProductNotFoundException.class);
    }
}
