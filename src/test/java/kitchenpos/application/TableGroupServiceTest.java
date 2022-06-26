package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    private TableGroup tableGroup;
    private OrderTable orderTable_1;
    private OrderTable orderTable_2;
    private MenuProduct chicken_menuProduct;
    private MenuProduct ham_menuProduct;
    private OrderLineItem chickenOrder;
    private OrderLineItem hamOrder;


    @BeforeEach
    public void init() {
        setOrderTable();
        setMenu();
        setOrderLineItem();

        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
        tableGroup = new TableGroup();

    }

    private void setOrderTable() {
        orderTable_1 = new OrderTable();
        orderTable_1.setEmpty(true);
        orderTable_2 = new OrderTable();
        orderTable_2.setEmpty(true);
    }

    private void setMenu() {
        Product chicken = new Product();
        chicken.setPrice(BigDecimal.valueOf(5000));
        chicken_menuProduct = new MenuProduct();
        chicken_menuProduct.setProductId(1L);
        chicken_menuProduct.setQuantity(1);
        chicken_menuProduct.setMenuId(1L);

        Product ham = new Product();
        ham.setPrice(BigDecimal.valueOf(4000));
        ham_menuProduct = new MenuProduct();
        ham_menuProduct.setProductId(2L);
        ham_menuProduct.setQuantity(1);
        ham_menuProduct.setMenuId(1L);
    }

    private void setOrderLineItem() {
        chickenOrder = new OrderLineItem();
        chickenOrder.setMenuId(chicken_menuProduct.getMenuId());
        chickenOrder.setQuantity(1);

        hamOrder = new OrderLineItem();
        hamOrder.setMenuId(ham_menuProduct.getMenuId());
        hamOrder.setQuantity(2);
    }

    @Test
    @DisplayName("단체 테이블 생성 정상 로직")
    void createTableGroupHappyCase() {
        //given
        tableGroup.setOrderTables(Arrays.asList(orderTable_1, orderTable_2));

        when(orderTableDao.findAllByIdIn(Arrays.asList(orderTable_1.getId(), orderTable_2.getId())))
            .thenReturn(Arrays.asList(orderTable_1, orderTable_2));
        when(tableGroupDao.save(tableGroup)).thenReturn(tableGroup);

        //when
        TableGroup tableGroup_created = tableGroupService.create(tableGroup);

        //then
        assertAll(
            () -> assertThat(tableGroup_created.getCreatedDate()).isNotNull(),
            () -> assertThat(tableGroup_created.getOrderTables()).hasSize(2)
        );
    }

    @Test
    @DisplayName("1개 이하 테이블로 단체 테이블 생성시 에러 발생")
    void createWithUnderOneTableThrowError() {
        //when & then 0개의 테이블로 단체 테이블 생성시 에러 발생
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);

        //given
        tableGroup.setOrderTables(Arrays.asList(orderTable_1));

        //when & then 1개의 테이블로 단체 테이블 생성시 에러 발생
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테이블로 단체 테이블 생성시 에러 발생")
    void createWithNotSavedTableThrowError() {
        //given
        tableGroup.setOrderTables(Arrays.asList(orderTable_1, orderTable_2));

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있지 않은 테이블로 단체 테이블 생성시 에러 발생")
    void createWithEmptyTableThrowError() {
        //given
        tableGroup.setOrderTables(Arrays.asList(orderTable_1, orderTable_2));
        when(orderTableDao.findAllByIdIn(Arrays.asList(orderTable_1.getId(), orderTable_2.getId())))
            .thenReturn(Arrays.asList(orderTable_1, orderTable_2));
        orderTable_1.setEmpty(false);

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블 해체 정상로직")
    void ungroupHappyCase() {
        //given
        when(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
            .thenReturn(Arrays.asList(orderTable_1, orderTable_2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
            Arrays.asList(orderTable_1.getId(), orderTable_2.getId()),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .thenReturn(false);

        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
    }

    @Test
    @DisplayName("단체 테이블 해체시 요리중이거나 먹고있는 오더가 있으면 에러가 발생한다")
    void ungroupWithCookingMealOrderThrowError() {
        //given
        when(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
            .thenReturn(Arrays.asList(orderTable_1, orderTable_2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
            Arrays.asList(orderTable_1.getId(), orderTable_2.getId()),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .thenReturn(true);

        //when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}