package kitchenpos.application;

import static kitchenpos.utils.generator.MenuFixtureGenerator.메뉴_구성_상품_생성;
import static kitchenpos.utils.generator.MenuFixtureGenerator.메뉴_생성;
import static kitchenpos.utils.generator.MenuFixtureGenerator.메뉴_생성_요청_생성;
import static kitchenpos.utils.generator.MenuGroupFixtureGenerator.메뉴_그룹_생성;
import static kitchenpos.utils.generator.ProductFixtureGenerator.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.application.menu.MenuProductService;
import kitchenpos.application.menu.MenuService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.CreateMenuRequest;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:Menu")
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductService menuProductService;

    @InjectMocks
    private MenuService menuService;

    public static Product 물냉면, 비빔냉면, 삼겹살, 항정살, 고추장_불고기;
    public static MenuGroup 고기랑_같이, 고기만_듬뿍;
    public static Menu 커플_냉삼_메뉴, 고기_더블_더블_메뉴;
    public static List<MenuProduct> 커플_냉삼_메뉴_구성_상품, 고기_더블_더블_메뉴_구성_상품;

    public static CreateMenuRequest 커플_냉삼_메뉴_생성_요청, 고기_더블_더블_메뉴_생성_요청;
    public static List<MenuProductRequest> 커플_냉삼_메뉴_구성_상품_생성_요청, 고기_더블_더블_메뉴_구성_상품_생성_요청;

    @BeforeEach
    void setUp() {
        커플_냉삼_메뉴_상품_생성();
        커플_냉삼_메뉴_생성();
        커플_냉삼_메뉴_생성_요청_생성();

        고기_더블_더블_메뉴_상품_생성();
        고기_더블_더블_메뉴_생성();
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    public void createMenu() {
        // Given
        given(menuGroupRepository.findById(any())).willReturn(Optional.of(고기랑_같이));
        given(menuProductService.findMenuProductByMenuProductRequest(커플_냉삼_메뉴_구성_상품_생성_요청)).willReturn(커플_냉삼_메뉴_구성_상품);
        given(menuRepository.save(any(Menu.class))).will(AdditionalAnswers.returnsFirstArg());

        // When
        menuService.create(커플_냉삼_메뉴_생성_요청);

        // Then
        verify(menuGroupRepository).findById(any());
        verify(menuProductService).findMenuProductByMenuProductRequest(커플_냉삼_메뉴_구성_상품_생성_요청);
        verify(menuRepository).save(any(Menu.class));
    }

    @ParameterizedTest(name = "case[{index}] : ''{0}'' => {1}")
    @MethodSource
    @DisplayName("메뉴 가격이 유효하지 않은 경우 예외가 발생한다.")
    public void throwException_WhenMenuPriceIsInvalid(final BigDecimal givenPrice, final String givenDescription) {
        // Given
        CreateMenuRequest 가격이_유효하지_않은_메뉴_생성_요청 = new CreateMenuRequest(
            "커플_냉삼_메뉴",
            givenPrice,
            고기랑_같이.getId(),
            커플_냉삼_메뉴_구성_상품_생성_요청
        );

        // When
        assertThatExceptionOfType(IllegalArgumentException.class)
            .as(givenDescription)
            .isThrownBy(() -> menuService.create(가격이_유효하지_않은_메뉴_생성_요청));
    }

    private static Stream<Arguments> throwException_WhenMenuPriceIsInvalid() {
        final BigDecimal nullBigDecimal = null;
        return Stream.of(
            Arguments.of(nullBigDecimal, "메뉴 가격이 null인 경우"),
            Arguments.of(BigDecimal.valueOf(Integer.MIN_VALUE), "메뉴 가격이 음수인 경우")
        );
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹 정보가 포함된 메뉴를 생성하는 경우 예외가 발생한다.")
    public void throwException_WhenMenuGroupIsNotExist() {
        // Given
        CreateMenuRequest 메뉴_그룹_정보가_존재하지_않는_메뉴_생성_요청 = new CreateMenuRequest(
            "커플_냉삼_메뉴",
            new BigDecimal(25_000),
            null,
            커플_냉삼_메뉴_구성_상품_생성_요청
        );

        given(menuGroupRepository.findById(any())).willThrow(IllegalArgumentException.class);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(메뉴_그룹_정보가_존재하지_않는_메뉴_생성_요청));

        verify(menuGroupRepository).findById(any());
    }

    @Test
    @DisplayName("메뉴 구성 상품이 존재하지 않는 경우 예외가 발생한다.")
    public void throwException_WhenMenuProductIsNotExist() {
        // Given
        CreateMenuRequest 메뉴_구성_상품이_존재하지_않는_메뉴_생성_요청 = new CreateMenuRequest(
            "커플_냉삼_메뉴",
            new BigDecimal(25_000),
            고기랑_같이.getId(),
            null
        );

        given(menuGroupRepository.findById(any())).willReturn(Optional.of(고기랑_같이));
        given(menuProductService.findMenuProductByMenuProductRequest(메뉴_구성_상품이_존재하지_않는_메뉴_생성_요청.getMenuProductRequests()))
            .willThrow(IllegalArgumentException.class);

        // When
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(메뉴_구성_상품이_존재하지_않는_메뉴_생성_요청));

        // Then
        verify(menuGroupRepository).findById(any());
        verify(menuProductService).findMenuProductByMenuProductRequest(메뉴_구성_상품이_존재하지_않는_메뉴_생성_요청.getMenuProductRequests());
    }

    @Test
    @DisplayName("메뉴에 포함된 상품의 가격의 총 합이 메뉴의 가격보다 큰 경우 예외가 발생한다.")
    public void throwException_WhenMenuPriceIsOverThanSumOfEachMenuProductsPrice() {
        // Given
        CreateMenuRequest 메뉴_구성_상품_가격의_총합이_메뉴의_가격보다_큰_메뉴_생성_요청 = 메뉴_생성_요청_생성("커플_냉삼_메뉴", 25_000, 고기랑_같이, 메뉴_구성_상품_생성(물냉면, 1));
        List<MenuProduct> 메뉴_상품_목록 = Collections.singletonList(메뉴_구성_상품_생성(물냉면, 1));

        given(menuGroupRepository.findById(any())).willReturn(Optional.of(고기랑_같이));
        given(
            menuProductService.findMenuProductByMenuProductRequest(메뉴_구성_상품_가격의_총합이_메뉴의_가격보다_큰_메뉴_생성_요청.getMenuProductRequests()))
            .willReturn(메뉴_상품_목록);

        // When
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(메뉴_구성_상품_가격의_총합이_메뉴의_가격보다_큰_메뉴_생성_요청));

        // Then
        verify(menuGroupRepository).findById(any());
        verify(menuProductService)
            .findMenuProductByMenuProductRequest(메뉴_구성_상품_가격의_총합이_메뉴의_가격보다_큰_메뉴_생성_요청.getMenuProductRequests());
    }

    @Disabled
    @Test
    @DisplayName("메뉴 목록을 조회한다. : 조회된 메뉴의 수만큼 메뉴 상품을 조회하는 과정에서 `N+1` 문제가 발생한다.")
    public void getAllMenus() {
        // Given
        List<Menu> 메뉴_목록 = Arrays.asList(고기_더블_더블_메뉴, 커플_냉삼_메뉴);
        given(menuRepository.findAll()).willReturn(메뉴_목록);

        // When
        List<MenuResponse> 조회된_메뉴_목록 = menuService.list();

        // Then
        verify(menuRepository).findAll();

        List<String> givenMenuNames = 메뉴_목록.stream()
            .map(Menu::getName)
            .collect(Collectors.toList());
        assertThat(조회된_메뉴_목록).extracting(MenuResponse::getName).isEqualTo(givenMenuNames);
    }

    public static void 커플_냉삼_메뉴_상품_생성() {
        물냉면 = 상품_생성("물냉면", 8_000);
        비빔냉면 = 상품_생성("비빔냉면", 8_000);
        삼겹살 = 상품_생성("삼겹살", 15_000);
    }

    public static void 커플_냉삼_메뉴_생성() {
        고기랑_같이 = 메뉴_그룹_생성("고기랑 같이");
        커플_냉삼_메뉴 = 메뉴_생성(
            "커플_냉삼",
            25_000,
            고기랑_같이,
            메뉴_구성_상품_생성(물냉면, 1),
            메뉴_구성_상품_생성(비빔냉면, 1),
            메뉴_구성_상품_생성(삼겹살, 1)
        );
        커플_냉삼_메뉴_구성_상품 = 커플_냉삼_메뉴.getMenuProducts();
    }

    public static void 커플_냉삼_메뉴_생성_요청_생성() {
        커플_냉삼_메뉴_생성_요청 = 메뉴_생성_요청_생성(
            "커플_냉삼",
            25_000,
            고기랑_같이,
            메뉴_구성_상품_생성(물냉면, 1),
            메뉴_구성_상품_생성(비빔냉면, 1),
            메뉴_구성_상품_생성(삼겹살, 1)
        );
        커플_냉삼_메뉴_구성_상품_생성_요청 = 커플_냉삼_메뉴_생성_요청.getMenuProductRequests();
    }

    public static void 고기_더블_더블_메뉴_상품_생성() {
        항정살 = 상품_생성("항정살", 20_000);
        고추장_불고기 = 상품_생성("고추장_불고기", 15_000);
    }

    public static void 고기_더블_더블_메뉴_생성() {
        고기만_듬뿍 = 메뉴_그룹_생성("고기만_듬뿍");
        고기_더블_더블_메뉴 = 메뉴_생성(
            "고기_더블_더블",
            30_000,
            고기만_듬뿍,
            메뉴_구성_상품_생성(항정살, 1),
            메뉴_구성_상품_생성(고추장_불고기, 1)
        );
        고기_더블_더블_메뉴_구성_상품 = 고기_더블_더블_메뉴.getMenuProducts();
    }

    public static void 고기_더블_더블_메뉴_생성_요청_생성() {
        고기_더블_더블_메뉴_생성_요청 = 메뉴_생성_요청_생성(
            "고기_더블_더블",
            30_000,
            고기만_듬뿍,
            메뉴_구성_상품_생성(항정살, 1),
            메뉴_구성_상품_생성(고추장_불고기, 1)
        );
        고기_더블_더블_메뉴_구성_상품_생성_요청 = 커플_냉삼_메뉴_생성_요청.getMenuProductRequests();
    }
}
