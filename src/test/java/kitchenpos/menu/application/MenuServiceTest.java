package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository ProductRepository;

    @InjectMocks
    private MenuService menuService;

    private MenuGroup 추천메뉴;
    private Menu 강정치킨plus강정치킨;
    private Product 강정치킨;
    private MenuProduct 강정치킨양두배;

    @BeforeEach
    void setUp() {
        추천메뉴 = new MenuGroup(1L, "추천메뉴");
        강정치킨plus강정치킨 = new Menu(1L, "강정치킨+강정치킨", BigDecimal.valueOf(20000), 추천메뉴);
        강정치킨 = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
        강정치킨양두배 = new MenuProduct(1L, 강정치킨plus강정치킨, 강정치킨, 2);
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void createTest() {
        // given
        given(ProductRepository.findById(any())).willReturn(Optional.of(강정치킨));
        given(menuGroupRepository.findById(any())).willReturn(Optional.of(추천메뉴));
        given(menuProductRepository.save(any())).willReturn(강정치킨양두배);
        given(menuRepository.save(any())).willReturn(강정치킨plus강정치킨);
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 2);

        // when
        MenuResponse createdMenu = menuService.create(new MenuRequest("강정치킨plus강정치킨", BigDecimal.valueOf(20000), 1L, Arrays.asList(menuProductRequest)));

        // then
        assertThat(createdMenu.getId()).isEqualTo(강정치킨plus강정치킨.getId());
        assertThat(createdMenu.getName()).isEqualTo(강정치킨plus강정치킨.getName());
        assertThat(createdMenu.getPrice()).isEqualTo(강정치킨plus강정치킨.getPrice().getValue());
        assertThat(createdMenu.getMenuGroupId()).isEqualTo(강정치킨plus강정치킨.getMenuGroup().getId());
    }

    @DisplayName("메뉴의 메뉴그룹이 올바르지 않으면 등록할 수 없다 : 메뉴의 메뉴 그룹은 등록된 메뉴 그룹이어야 한다.")
    @Test
    void createTest_unregisteredMenuGroup() {
        // given
        given(ProductRepository.findById(any())).willReturn(Optional.of(강정치킨));
        given(menuGroupRepository.findById(any())).willReturn(Optional.empty());
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 2);

        // when & then
        assertThatThrownBy(() -> menuService.create(new MenuRequest("강정치킨plus강정치킨", BigDecimal.valueOf(2000), 100L, Arrays.asList(menuProductRequest))))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("메뉴의 메뉴 상품 목록이 올바르지 않으면 등록할 수 없다 : 메뉴의 메뉴 상품 목록의 상품은 등록된 상품이어야 한다.")
    @Test
    void createTest_unregisteredMenuProduct() {
        // given
        MenuProductRequest menuProductRequest = new MenuProductRequest(100L, 2);

        // when & then
        assertThatThrownBy(() -> menuService.create(new MenuRequest("강정치킨plus강정치킨", BigDecimal.valueOf(34001), 100L, Arrays.asList(menuProductRequest))))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다.")
    @Test
    void listTest() {
        // given
        given(menuRepository.findAll()).willReturn(Arrays.asList(강정치킨plus강정치킨));

        // when
        List<MenuResponse> menus = menuService.list();

        // then
        assertThat(menus).hasSize(1);
        assertThat(menus.get(0).getId()).isEqualTo(강정치킨plus강정치킨.getId());
        assertThat(menus.get(0).getName()).isEqualTo(강정치킨plus강정치킨.getName());
        assertThat(menus.get(0).getPrice()).isEqualTo(강정치킨plus강정치킨.getPrice().getValue());
        assertThat(menus.get(0).getMenuGroupId()).isEqualTo(강정치킨plus강정치킨.getMenuGroup().getId());
        assertThat(menus.get(0).getMenuProducts().size()).isEqualTo(강정치킨plus강정치킨.getMenuProducts().size());
    }
}
