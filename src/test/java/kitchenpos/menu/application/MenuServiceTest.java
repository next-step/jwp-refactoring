package kitchenpos.menu.application;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest {

    private static final long NOT_SAVED_ID = Long.MAX_VALUE;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private Product 후라이드;
    private Product 양념치킨;

    private MenuProduct mp후라이드;
    private MenuProduct mp양념치킨;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        후라이드 = new Product("후라이드", 16000L);
        양념치킨 = new Product("양념치킨", 16000L);

        mp후라이드 = new MenuProduct(후라이드, 1L);
        mp양념치킨 = new MenuProduct(양념치킨, 1L);

        menuGroup = new MenuGroup(1L, "후라이드양념두마리메뉴");
    }

    @DisplayName("메뉴 생성 성공")
    @Test
    void createMenuSuccess() {

        // given
        MenuDto menuDto = createMenuDto(30000L);

        given(menuGroupRepository.findById(menuDto.getMenuGroupId()))
            .willReturn(Optional.of(menuGroup));
        givenProducts();

        Menu menu = new Menu(menuDto.getName(), menuDto.getPrice(), menuGroup);

        given(menuRepository.save(any())).willReturn(menu);
        givenMenuProducts();

        // when
        MenuDto actual = menuService.create(menuDto);

        // then
        assertThat(actual.getMenuProducts()).hasSize(2);
        assertThat(actual.getName()).isEqualTo(menuDto.getName());
        assertThat(actual.getPrice()).isEqualTo(menuDto.getPrice());
    }

    @DisplayName("메뉴 생성 실패 - 메뉴 가격이 음수이거나 설정되지 않음")
    @NullSource
    @ValueSource(longs = { -1000, -10000 })
    @ParameterizedTest
    void createMenuFail01(Long price) {
        // given
        MenuDto menuDto = createMenuDto(price);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuDto));
    }

    @DisplayName("메뉴 생성 요청 실패 - 등록되지 않은 메뉴 그룹의 ID를 사용")
    @Test
    void createMenuFail02() {
        // given
        MenuDto menuDto = createMenuDto(30000L, NOT_SAVED_ID);

        given(menuGroupRepository.findById(NOT_SAVED_ID)).willReturn(Optional.empty());

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuDto));
    }

    @DisplayName("메뉴 생성 요청 실패 - 메뉴 가격은 메뉴 상품 가격의 합보다 작아야 함")
    @ValueSource(longs = { 33000, 35000 })
    @ParameterizedTest
    void createMenuFail03(Long price) {
        // given
        MenuDto menuDto = createMenuDto(price);
        given(menuGroupRepository.findById(menuDto.getMenuGroupId())).willReturn(Optional.of(menuGroup));

        Menu menu = new Menu(menuDto.getName(), menuDto.getPrice(), menuGroup);
        given(menuRepository.save(any())).willReturn(menu);

        givenProducts();
        givenMenuProducts();

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuDto));
    }

    public MenuDto createMenuDto(Long price) {
        return createMenuDto(price, 1L);
    }

    public MenuDto createMenuDto(Long price, Long menuGroupId) {
        // Product id 1: 후라이드, 2: 양념, 둘 다 16000원
        MenuProductDto 후라이드 = new MenuProductDto(null, 1L, 1L);
        MenuProductDto 양념 = new MenuProductDto(null, 2L, 1L);

        List<MenuProductDto> menuProductDtos = Arrays.asList(후라이드, 양념);

        return new MenuDto("후라이드양념두마리세트", price, menuGroupId, menuProductDtos);
    }

    private void givenProducts() {
        given(productRepository.findById(1L)).willReturn(Optional.of(후라이드));
        given(productRepository.findById(2L)).willReturn(Optional.of(양념치킨));
    }

    private void givenMenuProducts() {
        given(menuProductRepository.save(any())).willReturn(mp후라이드, mp양념치킨);
    }
}
