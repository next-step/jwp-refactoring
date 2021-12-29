package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupService menuGroupService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private MenuService menuService;

    private MenuGroup 메뉴그룹;
    private Product 상품1;
    private Menu 메뉴;
    private List<Menu> 메뉴_목록;
    private MenuProduct 메뉴상품;
    private List<MenuProductRequest> 메뉴_상품_목록_요청;
    private MenuRequest 메뉴_요청;

    @BeforeEach
    void setUp() {
        메뉴그룹 = MenuGroup.of("메뉴그룹");
        상품1 = Product.of(1L,"상품",new BigDecimal("10000"));
        메뉴 = Menu.of(1L,Name.from("메뉴"),Price.from(new BigDecimal("50000")),MenuGroup.of("메뉴그룹"));
        메뉴_목록 = Collections.singletonList(메뉴);
        메뉴상품 = MenuProduct.of(1L, 메뉴, 상품1,2L);
        메뉴_상품_목록_요청 = Lists.newArrayList(new MenuProductRequest(1L, 2L));
        메뉴_요청 = MenuRequest.of("메뉴", new BigDecimal("10000"), 1L, 메뉴_상품_목록_요청);
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void saveMenu() {
        given(menuGroupService.findMenuGroupById(anyLong())).willReturn(메뉴그룹);
        given(productService.getById(any())).willReturn(상품1);
        given(menuRepository.save(any())).willReturn(메뉴);

        final MenuResponse actual = menuService.create(메뉴_요청);

        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(메뉴.getId()),
                () -> assertThat(actual.getName()).isEqualTo(메뉴.getName().toName()),
                () -> assertThat(actual.getPrice()).isEqualTo(메뉴.getPrice().toBigDecimal()),
                () -> assertThat(actual.getMenuGroupResponse().getName()).isEqualTo(메뉴.getMenuGroup().getName()),
                () -> assertThat(actual.getMenuProductResponses()).hasSize(메뉴.getMenuProducts().toList().size())

        );
        assertThat(actual).isNotNull();
    }

    @DisplayName("메뉴들을 조회한다.")
    @Test
    void findMenus() {
        given(menuRepository.findAll()).willReturn(Collections.singletonList(메뉴));

        final List<MenuResponse> actual = menuService.list();

        assertAll(
                () -> assertThat(actual).hasSize(메뉴_목록.size()),
                () -> assertThat(actual.get(0)).isNotNull(),
                () -> assertThat(actual.get(0).getId()).isEqualTo(메뉴_목록.get(0).getId()),
                () -> assertThat(actual.get(0).getName()).isEqualTo(메뉴_목록.get(0).getName().toName()),
                () -> assertThat(actual.get(0).getPrice()).isEqualTo(메뉴_목록.get(0).getPrice().toBigDecimal()),
                () -> assertThat(actual.get(0).getMenuGroupResponse().getName()).isEqualTo(메뉴_목록.get(0).getMenuGroup().getName()),
                () -> assertThat(actual.get(0).getMenuProductResponses()).hasSize(메뉴_목록.get(0).getMenuProducts().toList().size())
        );
    }
}
