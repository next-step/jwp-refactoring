package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private Product 후라이드치킨;
    private Product 양념치킨;
    private Product 치즈볼;
    private Product 콜라;
    private MenuGroup 치킨세트;
    private MenuProduct 후라이드치킨상품;
    private MenuProduct 양념치킨상품;
    private MenuProduct 치즈볼상품;
    private MenuProduct 콜라상품;
    private Menu 후라이드치킨세트;
    private Menu 양념치킨세트;
    private List<MenuProductRequest> 후라이드요청 = new ArrayList<>();

    @BeforeEach
    void setUp() {
        후라이드치킨 = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(15000));
        콜라 = Product.of(2L, "콜라", BigDecimal.valueOf(2000));
        양념치킨 = Product.of(3L, "양념치킨", BigDecimal.valueOf(16000));
        치즈볼 = Product.of(4L, "치즈볼", BigDecimal.valueOf(5000));
        치킨세트 = MenuGroup.of(1L, "치킨세트");
        후라이드치킨상품 = MenuProduct.of(1L, null, 후라이드치킨, 1L);
        콜라상품 = MenuProduct.of(2L, null, 콜라, 1L);
        양념치킨상품 = MenuProduct.of(3L, null, 양념치킨, 1L);
        치즈볼상품 = MenuProduct.of(3L, null, 치즈볼, 1L);
        후라이드치킨세트 = Menu.of(1L, "후라이드치킨세트", BigDecimal.valueOf(17000), 치킨세트.getId(),
                Arrays.asList(후라이드치킨상품, 콜라상품, 양념치킨상품));
        양념치킨세트 = Menu.of(2L, "양념치킨세트", BigDecimal.valueOf(21000), 치킨세트.getId(),
                Arrays.asList(후라이드치킨상품, 콜라상품, 치즈볼상품));
        후라이드요청.add(new MenuProductRequest(후라이드치킨.getId(), 1L));
        후라이드요청.add(new MenuProductRequest(콜라.getId(), 1L));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성() {
        // when
        MenuRequest menuRequest = new MenuRequest(후라이드치킨세트.getName(), 후라이드치킨세트.getPrice(), 치킨세트.getId(), 후라이드요청);
        when(menuGroupRepository.findById(menuRequest.getMenuGroupId())).thenReturn(Optional.of(치킨세트));
        when(productRepository.findById(후라이드치킨.getId())).thenReturn(Optional.of(후라이드치킨));
        when(productRepository.findById(콜라.getId())).thenReturn(Optional.of(콜라));
        when(productRepository.findById(양념치킨.getId())).thenReturn(Optional.of(양념치킨));
        when(menuRepository.save(any(Menu.class))).thenReturn(후라이드치킨세트);

        // when
        MenuResponse menuResponse = menuService.create(menuRequest);

        // then
        assertAll(
                () -> assertThat(menuResponse.getId()).isNotNull(),
                () -> assertThat(menuResponse.getMenuProductResponses().stream().map(MenuProductResponse::getProductId))
                        .containsExactly(후라이드치킨.getId(), 콜라.getId(), 양념치킨.getId())
        );
    }

    @DisplayName("가격이 비어있는 메뉴는 생성할 수 없다.")
    @Test
    void 가격_빈_메뉴_생성() {
        // given
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(치킨세트));
        when(productRepository.findById(후라이드치킨.getId())).thenReturn(Optional.of(후라이드치킨));
        when(productRepository.findById(콜라.getId())).thenReturn(Optional.of(콜라));
        BigDecimal price = null;
        MenuRequest menuRequest = new MenuRequest(후라이드치킨세트.getName(), price, 치킨세트.getId(), 후라이드요청);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("가격이 0원 미만인 메뉴는 생성할 수 없다.")
    @ParameterizedTest(name = "등록하고자 하는 메뉴의 가격: {0}")
    @ValueSource(longs = {-1000, -2000})
    void 음수_가격_메뉴_생성(long price) {
        // given
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(치킨세트));
        when(productRepository.findById(후라이드치킨.getId())).thenReturn(Optional.of(후라이드치킨));
        when(productRepository.findById(콜라.getId())).thenReturn(Optional.of(콜라));

        // when
        MenuRequest menuRequest = new MenuRequest(후라이드치킨세트.getName(), BigDecimal.valueOf(price), 치킨세트.getId(), 후라이드요청);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("메뉴 그룹에 속하지 않는 메뉴는 생성할 수 없다.")
    @Test
    void 메뉴_그룹_없는_메뉴_생성() {
        // when
        Long notExistsMenuGroupId = 10L;
        MenuRequest menuRequest = new MenuRequest(후라이드치킨세트.getName(), BigDecimal.valueOf(15000), notExistsMenuGroupId, 후라이드요청);
        when(menuGroupRepository.findById(notExistsMenuGroupId)).thenReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("메뉴 상품이 존재하지 않으면 해당 메뉴를 생성할 수 없다.")
    @Test
    void 메뉴상품_없는_메뉴_생성() {
        // given
        List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        MenuRequest menuRequest = new MenuRequest(후라이드치킨세트.getName(), BigDecimal.valueOf(15000), 치킨세트.getId(), menuProductRequests);
        when(menuGroupRepository.findById(치킨세트.getId())).thenReturn(Optional.of(치킨세트));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("존재하지 않는 상품이 메뉴에 존재하면 해당 메뉴를 생성할 수 없다.")
    @Test
    void 존재하지_않는_상품_메뉴_생성() {
        // given
        Long notExistsMenuProductId = 10L;
        List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        menuProductRequests.add(new MenuProductRequest(notExistsMenuProductId, 1L));
        MenuRequest menuRequest = new MenuRequest(후라이드치킨세트.getName(), BigDecimal.valueOf(15000), 치킨세트.getId(), menuProductRequests);
        when(menuGroupRepository.findById(menuRequest.getMenuGroupId())).thenReturn(Optional.of(치킨세트));
        when(productRepository.findById(notExistsMenuProductId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("메뉴의 가격은 해당 메뉴에 존재하는 메뉴 상품들의 가격의 합보다 클 수 없다.")
    @Test
    void 메뉴상품_가격_합보다_큰_가격_메뉴_생성() {
        // when
        MenuRequest menuRequest = new MenuRequest(후라이드치킨세트.getName(), BigDecimal.valueOf(25000), 치킨세트.getId(), 후라이드요청);
        when(menuGroupRepository.findById(menuRequest.getMenuGroupId())).thenReturn(Optional.of(치킨세트));
        when(productRepository.findById(후라이드치킨.getId())).thenReturn(Optional.of(후라이드치킨));
        when(productRepository.findById(콜라.getId())).thenReturn(Optional.of(콜라));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("메뉴 전체 목록을 조회한다.")
    @Test
    void 메뉴_목록_조회() {
        // when
        List<Menu> menus = Arrays.asList(후라이드치킨세트, 양념치킨세트);
        when(menuRepository.findAll()).thenReturn(menus);

        // when
        List<MenuResponse> findMenus = menuService.list();

        // then
        assertAll(
                () -> assertThat(findMenus).hasSize(menus.size()),
                () -> assertThat(findMenus.stream().map(MenuResponse::getName))
                        .containsExactly(후라이드치킨세트.getName(), 양념치킨세트.getName())
        );
    }
}
