package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.fixture.TestMenuFactory;
import kitchenpos.fixture.TestMenuGroupFactory;
import kitchenpos.fixture.TestProductFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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

    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 등록한다.")
    @Test
    void saveMenu() {
        final MenuGroup 메뉴그룹 = TestMenuGroupFactory.메뉴그룹_조회됨(1L, "메뉴그룹");
        final Product 상품 = TestProductFactory.상품_조회됨(1L, "상품", 5000);
        final Menu 메뉴 = TestMenuFactory.메뉴_생성됨(1L, "메뉴", 4000, 메뉴그룹.getName());

        final List<MenuProductRequest> 메뉴_상품_목록_요청 = TestMenuFactory.메뉴_상품_목록_요청(TestMenuFactory.메뉴_상품_요청(1L, 2));

        given(menuGroupService.findMenuGroupById(anyLong())).willReturn(메뉴그룹);
        given(productService.getById(any())).willReturn(상품);
        given(menuRepository.save(any())).willReturn(메뉴);

        final MenuResponse actual = menuService.create(TestMenuFactory.메뉴_요청("메뉴", 4000, 1L, 메뉴_상품_목록_요청));

        TestMenuFactory.메뉴_생성_확인됨(actual, 메뉴);
    }

    @DisplayName("메뉴들을 조회한다.")
    @Test
    void findMenus() {
        final List<Menu> 메뉴_목록 = TestMenuFactory.메뉴_목록_조회됨(10);

        given(menuRepository.findAll()).willReturn(메뉴_목록);

        final List<MenuResponse> actual = menuService.list();

        TestMenuFactory.메뉴_목록_조회_확인됨(actual, 메뉴_목록);
    }
}
