package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import kitchenpos.ServiceTest;
import kitchenpos.menu.fixture.MenuGroupFixtureFactory;
import kitchenpos.menu.fixture.MenuProductFixtureFactory;
import kitchenpos.product.fixture.ProductFixtureFactory;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.exception.CannotMakeOrderException;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class OrderLineItemFactoryTest extends ServiceTest {

    private OrderLineItemFactory orderLineItemFactory;

    @Mock
    private MenuRepository menuRepository;

    private MenuGroup menuGroup;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        orderLineItemFactory = new OrderLineItemFactory(menuRepository);

        menuGroup = MenuGroupFixtureFactory.createMenuGroup("메뉴그룹1");
        product1 = ProductFixtureFactory.createProduct(1L, "상품1", 1000);
        product2 = ProductFixtureFactory.createProduct(2L, "상품2", 2000);
    }

    @Test
    @DisplayName("OrderLineItem 생성")
    void 주문항목_생성() {
        Long menuId = 1L;
        String menuName = "메뉴1";
        int menuPrice = 5000;
        long orderLineItemQuantity = 4L;
        when(menuRepository.findById(menuId)).thenReturn(
                Optional.of(테스트_메뉴_생성(menuId, menuGroup, menuName, menuPrice)));

        OrderLineItem orderLineItem = orderLineItemFactory.createOrderLineItem(menuId, orderLineItemQuantity);
        OrderLineMenu orderLineMenu = orderLineItem.getOrderLineMenu();

        Assertions.assertAll("주문항목에 복사된 메뉴정보를 확인한다."
                , () -> assertThat(orderLineMenu.getMenuId()).isEqualTo(menuId)
                , () -> assertThat(orderLineMenu.getMenuName()).isEqualTo(menuName)
                , () -> assertThat(orderLineMenu.getMenuPrice().intValue()).isEqualTo(menuPrice)
        );
        assertThat(orderLineItem.getQuantity()).isEqualTo(orderLineItemQuantity);
    }

    @Test
    @DisplayName("주문항목에 표시된 메뉴가 존재하지 않는 경우 주문항목 생성 실패")
    void 주문항목_생성_주문항목에_표시된_메뉴가_저장되지않은경우() {
        Long menuId = 1L;
        long orderLineItemQuantity = 4L;
        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderLineItemFactory.createOrderLineItem(menuId, orderLineItemQuantity))
                .isInstanceOf(CannotMakeOrderException.class);
    }

    private Menu 테스트_메뉴_생성(Long menuId, MenuGroup menuGroup, String menuName, int menuPrice) {
        MenuProduct menuProduct1 = MenuProductFixtureFactory.createMenuProduct(1L, product1.getId(), 4);
        MenuProduct menuProduct2 = MenuProductFixtureFactory.createMenuProduct(2L, product2.getId(), 1);
        Menu menu = new Menu(menuName, new BigDecimal(menuPrice), menuGroup,
                Lists.newArrayList(menuProduct1, menuProduct2));
        ReflectionTestUtils.setField(menu, "id", menuId);
        return menu;
    }
}
