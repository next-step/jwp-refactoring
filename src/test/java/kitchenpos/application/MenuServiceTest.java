package kitchenpos.application;

import static kitchenpos.fixture.DomainFactory.createEmptyPriceMenu;
import static kitchenpos.fixture.DomainFactory.createMenu;
import static kitchenpos.fixture.DomainFactory.createMenuGroup;
import static kitchenpos.fixture.DomainFactory.createMenuProduct;
import static kitchenpos.fixture.DomainFactory.createProduct;
import static kitchenpos.fixture.MenuFactory.createMenuRequest;
import static kitchenpos.fixture.MenuProductFactory.createMenuProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
    private MenuGroup 빅맥세트;
    private Product 감자;
    private Product 토마토;
    private Product 양상추;
    private Menu 빅맥버거;
    private Menu 감자튀김;

    @BeforeEach
    void setUp() {
        빅맥세트 = createMenuGroup(1L, "빅맥세트");
        감자 = createProduct(1L, "감자", 1000);
        토마토 = createProduct(2L, "토마토", 1000);
        양상추 = createProduct(3L, "양상추", 500);

        빅맥버거 = createMenu(1L, "빅맥버거", 3000, 1L,
                Arrays.asList(createMenuProduct(1L, null, 토마토, 1), createMenuProduct(2L, null, 양상추, 4)));
        감자튀김 = createMenu(2L, "감자튀김", 2000, 1L,
                Arrays.asList(createMenuProduct(1L, null, 감자, 2)));
    }

    @Test
    void 메뉴_생성_가격_없음_예외() {
        // given
        given(menuGroupRepository.existsById(빅맥세트.getId())).willReturn(true);

        // when
        MenuRequest 가격없는메뉴 = createMenuRequest("가격없는메뉴", null, 1L,
                Arrays.asList(createMenuProductRequest(토마토.getId(), 1), createMenuProductRequest(양상추.getId(), 4)));

        // then
        assertThatThrownBy(
                () -> menuService.create(가격없는메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성_가격_0_미만_예외() {
        // given
        given(menuGroupRepository.existsById(빅맥세트.getId())).willReturn(true);

        // when
        MenuRequest 가격음수메뉴 = createMenuRequest("가격없는메뉴", BigDecimal.valueOf(-100), 1L,
                Arrays.asList(createMenuProductRequest(토마토.getId(), 1), createMenuProductRequest(양상추.getId(), 4)));

        // then
        assertThatThrownBy(
                () -> menuService.create(가격음수메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성_존재하지_않는_메뉴그룹_예외() {
        // when
        MenuRequest 메뉴그룹없음 = createMenuRequest("메뉴그룹없는메뉴", BigDecimal.valueOf(1000), 100L,
                Arrays.asList(createMenuProductRequest(토마토.getId(), 1), createMenuProductRequest(양상추.getId(), 4)));

        // then
        assertThatThrownBy(
                () -> menuService.create(메뉴그룹없음)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성_존재하지_않는_상품_예외() {
        // given
        given(menuGroupRepository.existsById(빅맥세트.getId())).willReturn(true);
        given(productRepository.findById(토마토.getId())).willReturn(Optional.ofNullable(토마토));
        given(productRepository.findById(양상추.getId())).willThrow(IllegalArgumentException.class);

        // when
        MenuRequest 상품없음 = createMenuRequest("상품없음", BigDecimal.valueOf(1000), 빅맥세트.getId(),
                Arrays.asList(createMenuProductRequest(토마토.getId(), 1), createMenuProductRequest(양상추.getId(), 4)));

        assertThatThrownBy(
                () -> menuService.create(상품없음)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성_메뉴_가격이_상품_가격_합_보다_큼_예외() {
        // given
        given(menuGroupRepository.existsById(빅맥세트.getId())).willReturn(true);
        given(productRepository.findById(토마토.getId())).willReturn(Optional.ofNullable(토마토));
        given(productRepository.findById(양상추.getId())).willReturn(Optional.ofNullable(양상추));

        // when
        MenuRequest 메뉴가격큼 = createMenuRequest("메뉴가격큼", BigDecimal.valueOf(5000), 빅맥세트.getId(),
                Arrays.asList(createMenuProductRequest(토마토.getId(), 1), createMenuProductRequest(양상추.getId(), 4)));

        // then
        assertThatThrownBy(
                () -> menuService.create(메뉴가격큼)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성() {
        // given
        given(menuGroupRepository.existsById(빅맥세트.getId())).willReturn(true);
        given(productRepository.findById(토마토.getId())).willReturn(Optional.ofNullable(토마토));
        given(productRepository.findById(양상추.getId())).willReturn(Optional.ofNullable(양상추));
        when(menuRepository.save(any(Menu.class))).thenReturn(빅맥버거);

        // when
        MenuRequest 빅맥버거 = createMenuRequest("빅맥버거", BigDecimal.valueOf(3000), 빅맥세트.getId(),
                Arrays.asList(createMenuProductRequest(토마토.getId(), 1), createMenuProductRequest(양상추.getId(), 4)));
        MenuResponse result = menuService.create(빅맥버거);

        // then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void 메뉴_목록() {
        when(menuRepository.findAll()).thenReturn(Arrays.asList(빅맥버거, 감자튀김));

        List<MenuResponse> result = menuService.list();
        assertThat(toIdList(result)).containsExactlyElementsOf(Arrays.asList(빅맥버거.getId(), 감자튀김.getId()));
    }

    private List<Long> toIdList(List<MenuResponse> menus) {
        return menus.stream()
                .map(MenuResponse::getId)
                .collect(Collectors.toList());
    }
}
