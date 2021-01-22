package kitchenpos.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void create1() {
        //given
        ArgumentCaptor<Menu> argumentCaptor = ArgumentCaptor.forClass(Menu.class);

        Product product = new Product("후라이드치킨", new BigDecimal("16000"));
        ReflectionTestUtils.setField(product, "id", 2L);

        MenuGroup menuGroup = new MenuGroup("메뉴그롭2");
        ReflectionTestUtils.setField(menuGroup, "id", 2L);

        Menu menu = new Menu("후라이드치킨", new BigDecimal("16000"), menuGroup);
        ReflectionTestUtils.setField(menu, "id", 1L);

        given(menuGroupRepository.findById(2L))
                .willReturn(Optional.of(menuGroup));
        given(productRepository.findAllByIdIn(Collections.singletonList(2L)))
                .willReturn(Collections.singletonList(product));
        given(menuRepository.save(any()))
                .willReturn(menu);

        //when
        List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        menuProductRequests.add(new MenuProductRequest(2L, 1));

        MenuRequest menuRequest = new MenuRequest("후라이드치킨", new BigDecimal("16000"), 2L);
        menuRequest.setMenuProducts(menuProductRequests);
        MenuResponse createMenu = menuService.create(menuRequest);

        //then
        verify(menuRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getName()).isEqualTo("후라이드치킨");
        assertThat(argumentCaptor.getValue().getPrice()).isEqualTo(new BigDecimal(16000));
        assertThat(argumentCaptor.getValue().getMenuGroup().getName()).isEqualTo("메뉴그롭2");

        assertThat(createMenu.getId()).isEqualTo(1L);
        assertThat(createMenu.getName()).isEqualTo("후라이드치킨");
        assertThat(createMenu.getPrice()).isEqualTo(new BigDecimal(16000));
        assertThat(createMenu.getMenuGroupId()).isEqualTo(2L);
    }

    @DisplayName("메뉴를 등록할 수 있다 - 메뉴의 가격은 0 원 이상이어야 한다.")
    @Test
    void create2() {
        MenuRequest newMenu = new MenuRequest("후라이드치킨", new BigDecimal("-1"), 2L);
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록할 수 있다 - 등록된 메뉴그룹이 선택되어 있어야 한다.")
    @Test
    void create3() {
        // given
        MenuRequest menuRequest = new MenuRequest("후라이드치킨", new BigDecimal("16000"), 2L);

        // when
        // then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴그룹이 없습니다.");
    }

    @DisplayName("메뉴를 등록할 수 있다 - 상품목록의 각 상품이 이미 등록되어 있어야 한다.")
    @Test
    void create4() {
        //given
        List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        menuProductRequests.add(new MenuProductRequest(1L, 1));

        MenuRequest menuRequest = new MenuRequest( "후라이드치킨", new BigDecimal("16000"), 2L);
        menuRequest.setMenuProducts(menuProductRequests);

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록할 수 있다 - 메뉴의 가격이 상품목록 총합 가격보다 더 크면 안됨")
    @Test
    void create5() {
        //given
        Product product = new Product("후라이드치킨", new BigDecimal("14000"));
        ReflectionTestUtils.setField(product, "id", 1L);
        MenuGroup menuGroup = new MenuGroup("후라이드치킨");
        ReflectionTestUtils.setField(menuGroup, "id", 2L);

        given(menuGroupRepository.findById(2L))
                .willReturn(Optional.of(menuGroup));
        given(productRepository.findAllByIdIn(Collections.singletonList(1L)))
                .willReturn(Collections.singletonList(product));

        List<MenuProductRequest> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProductRequest(1L, 1));

        MenuRequest menuRequest = new MenuRequest("후라이드치킨", new BigDecimal("16000"), 2L);
        menuRequest.setMenuProducts(menuProducts);

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격이 상품목록 총합 가격보다 더 큽니다.");
    }

    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void list() {
        //given
        Product 후라이드치킨 = new Product("후라이드치킨", new BigDecimal("16000"));
        ReflectionTestUtils.setField(후라이드치킨, "id", 2L);
        Product 양념치킨 = new Product("양념치킨", new BigDecimal("16000"));
        ReflectionTestUtils.setField(양념치킨, "id", 1L);

        MenuGroup menuGroup = new MenuGroup("메뉴그룹1");
        ReflectionTestUtils.setField(menuGroup, "id", 1L);

        Menu 후라이드치킨_메뉴 = new Menu("후라이드치킨", new BigDecimal("16000"), menuGroup);
        ReflectionTestUtils.setField(후라이드치킨_메뉴, "id", 1L);
        Menu 양념치킨_메뉴 = new Menu("양념치킨", new BigDecimal("16000"), menuGroup);
        ReflectionTestUtils.setField(양념치킨_메뉴, "id", 2L);

        given(menuRepository.findAll())
                .willReturn(Arrays.asList(후라이드치킨_메뉴, 양념치킨_메뉴));

        given(menuProductRepository.findAllById(any()))
                .willReturn(Arrays.asList(
                        new MenuProduct(후라이드치킨_메뉴, 후라이드치킨, 1),
                        new MenuProduct(양념치킨_메뉴, 양념치킨, 1)
                ));

        //when
        List<MenuResponse> menus = menuService.list();

        //then
        assertThat(menus.size()).isEqualTo(2);
        assertThat(menus.get(0).getName()).isEqualTo("후라이드치킨");
        assertThat(menus.get(0).getMenuProducts().size()).isEqualTo(1);
        assertThat(menus.get(0).getMenuProducts().get(0).getProductId()).isEqualTo(2L);
        assertThat(menus.get(1).getName()).isEqualTo("양념치킨");
        assertThat(menus.get(1).getMenuProducts().size()).isEqualTo(1);
        assertThat(menus.get(1).getMenuProducts().get(0).getProductId()).isEqualTo(1L);
    }
}
