package kitchenpos.menu.application;

import static kitchenpos.menu.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProductRequest;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenu;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenuRequest;
import static kitchenpos.product.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private Product 감자튀김;
    private Product 불고기버거;
    private Product 치킨버거;
    private Product 콜라;
    private MenuGroup 햄버거세트;
    private MenuProduct 감자튀김상품;
    private MenuProduct 불고기버거상품;
    private MenuProduct 치킨버거상품;
    private MenuProduct 콜라상품;
    private Menu 불고기버거세트;
    private Menu 치킨버거세트;
    private List<MenuProductRequest> 불고기버거상품요청 = new ArrayList<>();

    @BeforeEach
    void setUp() {
        감자튀김 = generateProduct(1L, "감자튀김", BigDecimal.valueOf(3000L));
        콜라 = generateProduct(2L, "콜라", BigDecimal.valueOf(1500L));
        불고기버거 = generateProduct(3L, "불고기버거", BigDecimal.valueOf(4000L));
        치킨버거 = generateProduct(4L, "치킨버거", BigDecimal.valueOf(4500L));
        햄버거세트 = generateMenuGroup(1L, "햄버거세트");
        감자튀김상품 = generateMenuProduct(1L, null, 감자튀김, 1L);
        콜라상품 = generateMenuProduct(2L, null, 콜라, 1L);
        불고기버거상품 = generateMenuProduct(3L, null, 불고기버거, 1L);
        치킨버거상품 = generateMenuProduct(3L, null, 치킨버거, 1L);
        불고기버거세트 = generateMenu(1L, "불고기버거세트", BigDecimal.valueOf(8500L), 햄버거세트,
                Arrays.asList(감자튀김상품, 콜라상품, 불고기버거상품));
        치킨버거세트 = generateMenu(2L, "치킨버거세트", BigDecimal.valueOf(9000L), 햄버거세트,
                Arrays.asList(감자튀김상품, 콜라상품, 치킨버거상품));
        불고기버거상품요청.add(generateMenuProductRequest(감자튀김.getId(), 1L));
        불고기버거상품요청.add(generateMenuProductRequest(콜라.getId(), 1L));
        불고기버거상품요청.add(generateMenuProductRequest(불고기버거.getId(), 1L));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // given
        MenuRequest menuRequest = generateMenuRequest(불고기버거세트.getName(), 불고기버거세트.getPrice(), 햄버거세트.getId(), 불고기버거상품요청);
        given(menuGroupRepository.findById(menuRequest.getMenuGroupId())).willReturn(Optional.of(햄버거세트));
        given(productRepository.findById(감자튀김.getId())).willReturn(Optional.of(감자튀김));
        given(productRepository.findById(콜라.getId())).willReturn(Optional.of(콜라));
        given(productRepository.findById(불고기버거.getId())).willReturn(Optional.of(불고기버거));
        given(menuRepository.save(menuRequest.toMenu(햄버거세트, MenuProducts.from(Arrays.asList(감자튀김상품, 콜라상품, 불고기버거상품))))).willReturn(불고기버거세트);

        // when
        MenuResponse menuResponse = menuService.create(menuRequest);

        // then
        assertAll(
                () -> assertThat(menuResponse.getId()).isNotNull(),
                () -> assertThat(menuResponse.getMenuProductResponses().stream().map(MenuProductResponse::getProductId)).containsExactly(감자튀김.getId(), 콜라.getId(), 불고기버거.getId())
        );
    }

    @DisplayName("가격이 비어있는 메뉴는 생성할 수 없다.")
    @Test
    void createMenuThrowErrorWhenPriceIsNull() {
        // given
        BigDecimal price = null;
        MenuRequest menuRequest = generateMenuRequest(불고기버거세트.getName(), price, 햄버거세트.getId(), 불고기버거상품요청);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("가격이 0원 미만인 메뉴는 생성할 수 없다.")
    @ParameterizedTest(name = "등록하고자 하는 메뉴의 가격: {0}")
    @ValueSource(longs = {-1000, -2000})
    void createMenuThrowErrorWhenPriceIsSmallerThenZero(long price) {
        // given
        MenuRequest menuRequest = generateMenuRequest(불고기버거세트.getName(), BigDecimal.valueOf(price), 햄버거세트.getId(), 불고기버거상품요청);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("존재하는 메뉴 그룹에 속하지 않는 메뉴는 생성할 수 없다.")
    @Test
    void createMenuThrowErrorWhenMenuGroupIsNotExists() {
        // given
        Long notExistsMenuGroupId = 10L;
        MenuRequest menuRequest = generateMenuRequest(불고기버거세트.getName(), BigDecimal.valueOf(8500L), notExistsMenuGroupId, 불고기버거상품요청);
        given(menuGroupRepository.findById(notExistsMenuGroupId)).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest))
                .withMessage(ErrorCode.존재하지_않는_메뉴_그룹.getErrorMessage());
    }

    @DisplayName("메뉴에 메뉴 상품이 존재하지 않으면 해당 메뉴를 생성할 수 없다.")
    @Test
    void createMenuThrowErrorWhenMenuProductIsEmpty() {
        // given
        List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        MenuRequest menuRequest = generateMenuRequest(불고기버거세트.getName(), BigDecimal.valueOf(3000L), 햄버거세트.getId(), menuProductRequests);
        given(menuGroupRepository.findById(햄버거세트.getId())).willReturn(Optional.of(햄버거세트));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest))
                .withMessage(ErrorCode.메뉴_상품은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("존재하지 않는 상품이 메뉴에 존재하면 해당 메뉴를 생성할 수 없다.")
    @Test
    void createMenuThrowErrorWhenMenuProductIsNotExists() {
        // given
        Long notExistsMenuProductId = 10L;
        List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        menuProductRequests.add(generateMenuProductRequest(notExistsMenuProductId, 1L));
        MenuRequest menuRequest = generateMenuRequest(불고기버거세트.getName(), BigDecimal.valueOf(3000L), 햄버거세트.getId(), menuProductRequests);
        given(menuGroupRepository.findById(menuRequest.getMenuGroupId())).willReturn(Optional.of(햄버거세트));
        given(productRepository.findById(notExistsMenuProductId)).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest))
                .withMessage(ErrorCode.존재하지_않는_상품.getErrorMessage());
    }

    @DisplayName("메뉴의 가격은 해당 메뉴에 존재하는 메뉴 상품들의 가격의 합보다 클 수 없다.")
    @Test
    void createMenuThrowErrorWhenMenuPriceIsBiggerThanMenuProductsPriceSum() {
        // given
        MenuRequest menuRequest = generateMenuRequest(불고기버거세트.getName(), BigDecimal.valueOf(9500L), 햄버거세트.getId(), 불고기버거상품요청);
        given(menuGroupRepository.findById(menuRequest.getMenuGroupId())).willReturn(Optional.of(햄버거세트));
        given(productRepository.findById(감자튀김.getId())).willReturn(Optional.of(감자튀김));
        given(productRepository.findById(콜라.getId())).willReturn(Optional.of(콜라));
        given(productRepository.findById(불고기버거.getId())).willReturn(Optional.of(불고기버거));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest))
                .withMessage(ErrorCode.메뉴의_가격은_메뉴상품들의_가격의_합보다_클_수_없음.getErrorMessage());
    }

    @DisplayName("메뉴 전체 목록을 조회한다.")
    @Test
    void findAllMenus() {
        // given
        List<Menu> menus = Arrays.asList(불고기버거세트, 치킨버거세트);
        given(menuRepository.findAll()).willReturn(menus);

        // when
        List<MenuResponse> findMenus = menuService.list();

        // then
        assertAll(
                () -> assertThat(findMenus).hasSize(menus.size()),
                () -> assertThat(findMenus.stream().map(MenuResponse::getName)).containsExactly(불고기버거세트.getName().value(), 치킨버거세트.getName().value())
        );
    }
}
