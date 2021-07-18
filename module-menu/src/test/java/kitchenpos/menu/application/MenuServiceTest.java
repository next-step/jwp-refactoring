package kitchenpos.menu.application;

import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductRepository;

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

    @Mock
    private MenuProductService menuProductService;

    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    @Test
    void given_Menu_when_Create_then_SaveExecuted() {
        // given
        final List<MenuProductRequest> menuProductRequests = Collections.singletonList(new MenuProductRequest(1L, 1));
        final MenuRequest menuRequest = new MenuRequest("name", BigDecimal.ZERO, 1L, menuProductRequests);
        final MenuGroup menuGroup = new MenuGroup();
        final Menu savedMenu = new Menu("name", menuRequest.getPrice(), menuGroup.getId());
        given(menuRepository.save(any(Menu.class))).willReturn(savedMenu);

        // when
        menuService.create(menuRequest);

        // then
        verify(menuRepository).save(any(Menu.class));
    }

    @Test
    void list() {
        // when
        menuService.list();

        // then
        verify(menuRepository).findAll();
    }
}
