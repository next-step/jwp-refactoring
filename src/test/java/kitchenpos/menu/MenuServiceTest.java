package kitchenpos.menu;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.util.testFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @InjectMocks
    MenuService menuService;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    MenuRepository menuRepository;

    private MenuGroup 한마리메뉴;
    private Product 후라이드;
    private Product 양념;
    private MenuProduct 후라이드메뉴상품;
    private MenuProduct 양념메뉴상품;
    private Menu 후라이드치킨;
    private Menu 양념치킨;

    @BeforeEach
    void setUp() {
        한마리메뉴 = 한마리_메뉴_그룹_생성();

        후라이드 = 후라이드_상품_생성();
        양념 = 양념치킨_상품_생성();

        후라이드메뉴상품 = 후라이드_메뉴_상품_생성(후라이드.getId());
        양념메뉴상품 = 양념_메뉴_상품_생성(양념.getId());

        후라이드치킨 = 후라이드_치킨_메뉴_생성(한마리메뉴.getId(), Arrays.asList(후라이드메뉴상품));
        양념치킨 = 양념_치킨_메뉴_생성(한마리메뉴.getId(), Arrays.asList(양념메뉴상품));
    }

    @DisplayName("메뉴 등록")
    @Test
    void createMenu() {
        // given
        when(menuGroupRepository.findById(후라이드치킨.getMenuGroupId()))
                .thenReturn(Optional.of(한마리메뉴));
        when(productRepository.findByIdIn(Arrays.asList(후라이드.getId())))
                .thenReturn(Arrays.asList(후라이드));
        when(menuRepository.save(any()))
                .thenReturn(후라이드치킨);

        // when
        BigDecimal price = new BigDecimal(16000);
        MenuResponse result = menuService.create(new MenuRequest(후라이드치킨.getName(), price, 후라이드치킨.getMenuGroupId(),
                Arrays.asList(new MenuProductRequest(후라이드.getId(), 1L))));

        // then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo(후라이드치킨.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(price),
                () -> assertThat(result.getMenuGroupId()).isEqualTo(후라이드치킨.getMenuGroupId())
        );
    }

    @DisplayName("메뉴 등록 시 가격이 NULL인 경우 등록 불가")
    @Test
    void createMenuAndIsNullPrice() {
        // given
        MenuRequest 후라이드치킨_요청 = new MenuRequest(후라이드치킨.getName(), null, 후라이드치킨.getMenuGroupId(),
                Arrays.asList(new MenuProductRequest(후라이드.getId(), 1L)));

        // then
        assertThatThrownBy(() -> {
            menuService.create(후라이드치킨_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 가격이 `0`보다 작은 경우 등록 불가")
    @Test
    void createMenuAndIsNegativePrice() {
        // given
        MenuRequest 후라이드치킨_요청 = new MenuRequest(후라이드치킨.getName(), new BigDecimal(-16000), 후라이드치킨.getMenuGroupId(),
                Arrays.asList(new MenuProductRequest(후라이드.getId(), 1L)));

        // then
        assertThatThrownBy(() -> {
            menuService.create(후라이드치킨_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 등록되지 않은 경우 등록 불가")
    @Test
    void createMenuAndIsNotRegisterMenuGroup() {
        // given
        MenuRequest 후라이드치킨_요청 = new MenuRequest(후라이드치킨.getName(), new BigDecimal(-16000), 후라이드치킨.getMenuGroupId(),
                Arrays.asList(new MenuProductRequest(후라이드.getId(), 1L)));

        // then
        assertThatThrownBy(() -> {
            menuService.create(후라이드치킨_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 상품 리스트의 총 가격보다 작은 경우 등록 불가")
    @Test
    void createMenuAndIsBiggerTotalProductPrice() {
        // given
        MenuRequest 후라이드치킨_요청 = new MenuRequest(후라이드치킨.getName(), new BigDecimal(-16000), 후라이드치킨.getMenuGroupId(),
                Arrays.asList(new MenuProductRequest(후라이드.getId(), 1L)));

        // then
        assertThatThrownBy(() -> {
            menuService.create(후라이드치킨_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 전체 조회")
    @Test
    void findAllMenus() {
        // given
        when(menuRepository.findAll())
                .thenReturn(Arrays.asList(후라이드치킨, 양념치킨));

        // when
        List<MenuResponse> list = menuService.list();

        // then
        assertThat(list).hasSize(2);
    }
}
