package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ServiceTestBase {
    @MockBean
    protected ProductDao productDao;
    protected Product product;
    @MockBean
    protected MenuGroupDao menuGroupDao;
    protected MenuGroup menuGroup;
    @MockBean
    protected MenuDao menuDao;
    protected Menu menu;
    @MockBean
    protected MenuProductDao menuProductDao;
    protected MenuProduct menuProduct;
    @MockBean
    protected OrderDao orderDao;
    protected Order mealOrder;
    protected Order cookingOrder;
    protected Order completionOrder;

    @MockBean
    protected OrderTableDao orderTableDao;
    protected OrderTable groupedTable1;
    protected OrderTable groupedTable2;
    protected OrderTable emptyTable;
    protected OrderTable cookingTable;
    protected OrderTable mealTable;
    protected OrderTable completionTable;
    @MockBean
    protected TableGroupDao tableGroupDao;
    protected TableGroup tableGroup;

    protected void setUpTableGroup() {
        tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(Arrays.asList(groupedTable1, groupedTable2));

        groupedTable1.setTableGroupId(tableGroup.getId());
        groupedTable2.setTableGroupId(tableGroup.getId());

        when(tableGroupDao.save(tableGroup))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(tableGroupDao.findById(tableGroup.getId())).thenReturn(Optional.of(tableGroup));
        when(tableGroupDao.findAll()).thenReturn(Collections.singletonList(tableGroup));

        when(orderTableDao.findAllByTableGroupId(tableGroup.getId())).thenReturn(Arrays.asList(groupedTable1, groupedTable2));
    }

    protected void setUpOrderTable() {
        groupedTable1 = createTable(1L);
        groupedTable2 = createTable(2L);
        emptyTable = createTable(3L);
        cookingTable = createTable(4L);
        mealTable = createTable(5L);
        completionTable = createTable(6L);

        cookingTable.setEmpty(false);
        cookingTable.setNumberOfGuests(1);

        mealTable.setEmpty(false);
        mealTable.setNumberOfGuests(1);

        completionTable.setEmpty(false);
        completionTable.setNumberOfGuests(1);

        when(orderTableDao.save(any(OrderTable.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(orderTableDao.findById(groupedTable1.getId())).thenReturn(Optional.of(groupedTable1));
        when(orderTableDao.findById(groupedTable2.getId())).thenReturn(Optional.of(groupedTable2));
        when(orderTableDao.findById(emptyTable.getId())).thenReturn(Optional.of(emptyTable));
        when(orderTableDao.findById(cookingTable.getId())).thenReturn(Optional.of(cookingTable));
        when(orderTableDao.findById(mealTable.getId())).thenReturn(Optional.of(mealTable));
        when(orderTableDao.findById(completionTable.getId())).thenReturn(Optional.of(completionTable));

        when(orderTableDao.findAll()).thenReturn(Arrays.asList(groupedTable1, groupedTable2, emptyTable, cookingTable, mealTable, completionTable));
        when(orderTableDao.findAllByIdIn(any())).thenAnswer(invocation -> {
           List<Long> argument = invocation.getArgument(0);
           List<OrderTable> result = new ArrayList<>();
           for (Long id : argument) {
               result.add(null);
               addEqualsTable(id, result);
           }
           return result;
        });
    }

    private OrderTable createTable(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);

        return orderTable;
    }

    private void addEqualsTable(Long id, List<OrderTable> result) {
        if (id.equals(groupedTable1.getId())) {
            result.add(groupedTable1);
        }
        if (id.equals(groupedTable2.getId())) {
            result.add(groupedTable2);
        }
        if (id.equals(emptyTable.getId())) {
            result.add(emptyTable);
        }
        if (id.equals(cookingTable.getId())) {
            result.add(cookingTable);
        }
        if (id.equals(mealTable.getId())) {
            result.add(mealTable);
        }
        if (id.equals(completionTable.getId())) {
            result.add(completionTable);
        }
    }

    protected void setUpOrder() {
        mealOrder = createOrder(1L, mealTable, OrderStatus.MEAL);
        cookingOrder = createOrder(2L, cookingTable, OrderStatus.COOKING);
        completionOrder = createOrder(3L, completionTable, OrderStatus.COMPLETION);

        when(orderDao.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(orderDao.findById(mealOrder.getId())).thenReturn(Optional.of(mealOrder));
        when(orderDao.findById(cookingOrder.getId())).thenReturn(Optional.of(cookingOrder));
        when(orderDao.findById(completionOrder.getId())).thenReturn(Optional.of(completionOrder));

        when(orderDao.findAll()).thenReturn(Arrays.asList(mealOrder, cookingOrder, completionOrder));

        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).thenAnswer(invocation -> {
            Long orderTableId = invocation.getArgument(0);
            List<String> orderStatuses = invocation.getArgument(1);

            if (mealTable.getId().equals(orderTableId) && orderStatuses.contains(OrderStatus.MEAL.name())) {
                return true;
            }
            if (cookingTable.getId().equals(orderTableId) && orderStatuses.contains(OrderStatus.COOKING.name())) {
                return true;
            }
            return completionTable.getId().equals(orderTableId) && orderStatuses.contains(OrderStatus.COMPLETION.name());
        });

        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenAnswer(invocation -> {
            List<Long> orderTableIds = invocation.getArgument(0);
            List<String> orderStatuses = invocation.getArgument(1);

            if (orderTableIds.contains(mealTable.getId()) && orderStatuses.contains(OrderStatus.MEAL.name())) {
                return true;
            }
            if (orderTableIds.contains(cookingTable.getId()) && orderStatuses.contains(OrderStatus.COOKING.name())) {
                return true;
            }
            return orderTableIds.contains(completionTable.getId()) && orderStatuses.contains(OrderStatus.COMPLETION.name());
        });

    }

    private Order createOrder(Long id, OrderTable orderTable, OrderStatus orderStatus) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(Collections.singletonList(createOrderLineItem(order)));

        return order;
    }

    private OrderLineItem createOrderLineItem(Order order) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setOrderId(order.getId());
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(1);

        return orderLineItem;
    }

    protected void setUpProduct() {
        product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal(17_000));
        product.setName("양념치킨");

        when(productDao.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(productDao.findById(product.getId())).thenReturn(Optional.of(product));
        when(productDao.findAll()).thenReturn(Collections.singletonList(product));
    }

    protected void setUpMenuGroup() {
        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("추천메뉴");

        when(menuGroupDao.save(any(MenuGroup.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(menuGroupDao.findById(menuGroup.getId())).thenReturn(Optional.of(menuGroup));
        when(menuGroupDao.existsById(menuGroup.getId())).thenReturn(true);
        when(menuGroupDao.findAll()).thenReturn(Collections.singletonList(menuGroup));
    }

    protected void setUpMenu() {
        menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드+후라이드");
        menu.setPrice(new BigDecimal(30_000));
        menu.setMenuGroupId(menuGroup.getId());

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);

        menu.setMenuProducts(Collections.singletonList(menuProduct));

        when(menuDao.save(menu))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(menuDao.findById(menu.getId())).thenReturn(Optional.of(menu));
        when(menuDao.countByIdIn(any())).thenReturn(1L);
        when(menuDao.findAll()).thenReturn(Collections.singletonList(menu));
    }

    protected void setUpMenuProduct() {
        menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1L);

        when(menuProductDao.save(menuProduct))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(menuProductDao.findById(menuProduct.getSeq())).thenReturn(Optional.of(menuProduct));
        when(menuProductDao.findAll()).thenReturn(Collections.singletonList(menuProduct));
        when(menuProductDao.findAllByMenuId(menu.getId())).thenReturn(Collections.singletonList(menuProduct));
    }
}
