package kitchenpos.ordertable.validator;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.exception.IllegalOrderLineItemException;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.exception.IllegalOrderTableException;
import kitchenpos.ordertable.exception.NoSuchOrderTableException;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static kitchenpos.utils.fixture.MenuFixtureFactory.createMenu;
import static kitchenpos.utils.fixture.MenuGroupFixtureFactory.createMenuGroup;
import static kitchenpos.utils.fixture.MenuProductFixtureFactory.createMenuProduct;
import static kitchenpos.utils.fixture.OrderTableFixtureFactory.createOrderTable;
import static kitchenpos.utils.fixture.ProductFixtureFactory.createProduct;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderValidatorImplTest {
    @Mock
    private MenuService menuService;
    @Mock
    private TableService tableService;
    @InjectMocks
    private OrderValidatorImpl orderValidatorImpl;

    private MenuGroup 메뉴그룹_한식;
    private Product 김치찌개;
    private Menu 메뉴_김치찌개세트;
    private MenuProduct 김치찌개세트_김치찌개;
    private OrderTable 테이블_1;

    @BeforeEach
    void setUp() {
        메뉴그룹_한식 = createMenuGroup(1L, "한식메뉴");
        김치찌개 = createProduct(1L, "김치찌개", 8000);
        김치찌개세트_김치찌개 = createMenuProduct(1L, 김치찌개.getId(), 2);
        메뉴_김치찌개세트 = createMenu(1L, "김치찌개세트", 15000, 메뉴그룹_한식.getId(),
                Arrays.asList(김치찌개세트_김치찌개));

        테이블_1 = createOrderTable(1L, null, 4, false);
    }

    @DisplayName("등록하려는 주문테이블이 존재해야 한다")
    @Test
    void 주문_주문테이블_검증(){
        //given
        given(menuService.findMenusByIdList(anyList())).willReturn(Arrays.asList(메뉴_김치찌개세트));
        given(tableService.findOrderTableById(anyLong())).willThrow(NoSuchOrderTableException.class);

        //when
        OrderRequest orderRequest = OrderRequest.of(테이블_1.getId(), Arrays.asList(
                OrderLineItemRequest.of(메뉴_김치찌개세트.getId(), 1)
        ));

        //then
        assertThrows(NoSuchOrderTableException.class, () -> orderValidatorImpl.validate(orderRequest.toOrder()));
    }

    @DisplayName("등록하려는 주문테이블은 비어있을 수 없다")
    @Test
    void 주문_주문테이블_Empty_검증(){
        //given
        OrderTable 테이블_Empty = createOrderTable(2L, null, 0, true);
        given(menuService.findMenusByIdList(anyList())).willReturn(Arrays.asList(메뉴_김치찌개세트));
        given(tableService.findOrderTableById(anyLong())).willReturn(테이블_Empty);

        //when
        OrderRequest orderRequest = OrderRequest.of(테이블_Empty.getId(), Arrays.asList(
                OrderLineItemRequest.of(메뉴_김치찌개세트.getId(), 1)
        ));

        //then
        assertThrows(IllegalOrderTableException.class, () -> orderValidatorImpl.validate(orderRequest.toOrder()));
    }

    @DisplayName("주문항목은 비어있을 수 없다")
    @Test
    void 주문_주문항목_검증(){
        //given
        OrderRequest invalidOrderRequest = OrderRequest.of(테이블_1.getId(), new ArrayList<>());

        //then
        assertThrows(IllegalOrderLineItemException.class, () -> orderValidatorImpl.validate(invalidOrderRequest.toOrder()));
    }

    @DisplayName("주문항목간 메뉴는 중복될 수 없다")
    @Test
    void 주문_주문항목_메뉴_중복_검증(){
        //given
        given(menuService.findMenusByIdList(anyList())).willReturn(Arrays.asList(메뉴_김치찌개세트));
        OrderRequest invalidOrderRequest = OrderRequest.of(테이블_1.getId(), Arrays.asList(
                OrderLineItemRequest.of(메뉴_김치찌개세트.getId(), 1),
                OrderLineItemRequest.of(메뉴_김치찌개세트.getId(), 2)
        ));

        //then
        assertThrows(IllegalOrderLineItemException.class, () -> orderValidatorImpl.validate(invalidOrderRequest.toOrder()));
    }

    @DisplayName("등록하려는 주문항목의 메뉴가 존재해야 한다")
    @Test
    void 주문_주문항목_메뉴_검증(){
        //given
        given(menuService.findMenusByIdList(anyList())).willReturn(Arrays.asList(메뉴_김치찌개세트));

        //when
        OrderRequest invalidOrderRequest = OrderRequest.of(테이블_1.getId(), Arrays.asList(
                OrderLineItemRequest.of(메뉴_김치찌개세트.getId(), 1),
                OrderLineItemRequest.of(0L, 1)
        ));

        //then
        assertThrows(IllegalOrderLineItemException.class, () -> orderValidatorImpl.validate(invalidOrderRequest.toOrder()));
    }
}
