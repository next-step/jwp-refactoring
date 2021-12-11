package kitchenpos.menu.domain.menu;

import kitchenpos.menu.aplication.MenuGroupService;
import kitchenpos.menu.aplication.MenuService;
import kitchenpos.menu.aplication.ProductService;
import kitchenpos.menu.domain.menuproduct.MenuProductRepository;
import kitchenpos.menu.dto.MenuResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kitchenpos.fixture.MenuDomainFixture.후라이드_치킨;
import static kitchenpos.fixture.MenuDomainFixture.후라이드_치킨_요청;
import static kitchenpos.fixture.MenuGroupDomainFixture.일인_세트;
import static kitchenpos.fixture.ProductDomainFixture.사이다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@DisplayName("Mockito - 메뉴 관리")
class MenuMockitoTest {

    private MenuRepository menuRepository;
    private MenuGroupService menuGroupService;
    private MenuProductRepository menuProductRepository;
    private ProductService productService;
    private MenuService menuService;

    private void setUpMock() {
        menuRepository = mock(MenuRepository.class);
        menuGroupService = mock(MenuGroupService.class);
        menuProductRepository = mock(MenuProductRepository.class);
        productService = mock(ProductService.class);
        menuService = new MenuService(menuRepository, menuProductRepository, menuGroupService, productService);
    }

    @Test
    @DisplayName("메뉴 생성 성공")
    void create() {
        // given
        setUpMock();
        when(menuGroupService.findById(anyLong())).thenReturn(일인_세트);
        when(productService.findById(anyLong())).thenReturn(사이다);
        when(menuRepository.save(any(Menu.class))).thenReturn(후라이드_치킨);

        // when
        MenuResponse actual = menuService.saveMenu(후라이드_치킨_요청);

        // then
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(후라이드_치킨_요청.getName()),
                () -> assertThat(actual.getMenuPrice().getPrice()).isEqualTo(후라이드_치킨_요청.getPrice())
        );
    }

    @Test
    @DisplayName("메뉴 조회 성공")
    void findAllMenu() {
        // given
        setUpMock();
        when(menuRepository.findAll()).thenReturn(Lists.newArrayList(후라이드_치킨));

        List<MenuResponse> actual = menuService.findMenus();

        assertAll(
                () -> assertThat(actual.get(0).getName()).contains(후라이드_치킨.getName()),
                () -> assertThat(actual.get(0).getMenuPrice()).isEqualTo(후라이드_치킨.getMenuPrice())
        );
    }
}
