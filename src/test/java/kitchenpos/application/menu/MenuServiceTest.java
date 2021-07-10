package kitchenpos.application.menu;

import kitchenpos.application.menugroup.MenuGroupService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menu.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.event.product.MenuCreatedEvent;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.exception.menu.MenuGroupAlreadyExistsException;
import kitchenpos.repository.menu.MenuRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private MenuGroupService menuGroupService;

    @InjectMocks
    private MenuService menuService;

    private final static long ANY_MENU_ID = 1L;
    private final static long ANY_MENU_GROUP_ID = 1L;
    private final static long ANY_PRODUCT_ID = 1L;

    private MenuRequest menuRequest;
    private MenuGroup menuGroup;
    private Menu menu;
    private Product dummyProduct;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroup.of("menuGroupName");
        ReflectionTestUtils.setField(menuGroup, "id", ANY_MENU_GROUP_ID);

        menuRequest = new MenuRequest("tomato pasta", Price.of(BigDecimal.ZERO), ANY_MENU_GROUP_ID, new ArrayList<>());
        menu = Menu.of("tomato pasta", Price.of(BigDecimal.ZERO), menuGroup);

        dummyProduct = Product.of("rice", kitchenpos.domain.product.Price.of(BigDecimal.valueOf(10)));
        ReflectionTestUtils.setField(dummyProduct, "id", ANY_PRODUCT_ID);
    }

    @Test
    @DisplayName("메뉴를 등록할 수 잇다.")
    void create_test() {
        given(menuGroupService.isExists(menuGroup.getId())).willReturn(false);
        given(menuGroupService.findById(ANY_MENU_GROUP_ID)).willReturn(menuGroup);
        given(menuRepository.save(menu)).willReturn(menu);

        menuService.create(menuRequest);

        verify(eventPublisher).publishEvent(any(MenuCreatedEvent.class));
        verify(menuRepository).save(menu);
    }

    @Test
    @DisplayName("메뉴를 등록하는 시점에 메뉴 그룹(MENU_GROUP)이 미리 등록되어 있어야 한다.")
    void menuGroup() {
        given(menuGroupService.isExists(menuGroup.getId())).willReturn(true);

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(MenuGroupAlreadyExistsException.class);
    }

    @Test
    @DisplayName("메뉴의 전체 가격이 메뉴그룹의 가격 전체 합보다 높을 경우 등록될 수 없다.")
    void price() {
        given(menuGroupService.isExists(menuGroup.getId())).willReturn(false);
        given(menuGroupService.findById(ANY_MENU_GROUP_ID)).willReturn(menuGroup);
        doThrow(InvalidPriceException.class).when(eventPublisher).publishEvent(any(MenuCreatedEvent.class));

        menuRequest = new MenuRequest("tomato pasta", Price.of(BigDecimal.valueOf(100L)), ANY_MENU_GROUP_ID,
                Lists.list(new MenuProductRequest(ANY_PRODUCT_ID, 1)));

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(InvalidPriceException.class);
    }
}