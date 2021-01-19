package kitchenpos.utils;

import kitchenpos.domain.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TestHelper {
    public static final Long 등록된_menuGroup_id = 2L;
    public static final Long 등록되어_있지_않은_menuGroup_id = 5L;
    public static final Long 등록된_product_id = 1L;
    public static final Long 등록되어_있지_않은_product_id = 7L;
    public static final Long 등록된_menu_id = 1L;
    public static final Long 등록되어_있지_않은_menu_id = 7L;
    public static final Long 빈_orderTable_id1 = 1L;
    public static final Long 빈_orderTable_id2 = 2L;
    public static final Long 비어있지_않은_orderTable_id = 3L;
    public static final Long 등록되어_있지_않은_orderTable_id = 4L;

    public static final TableGroup init_tableGroup = tableGroup_생성(1L);
    public static final OrderTable empty_orderTable1 = 빈_orderTable_생성(1L);
    public static final OrderTable empty_orderTable2 = 빈_orderTable_생성(2L);
    public static final OrderTable not_empty_orderTable = orderTable_생성(empty_orderTable1.getId(), false);
    public static final TableGroup tableGroup = tableGroup_orderTables_추가(init_tableGroup, Arrays.asList(empty_orderTable1, empty_orderTable2));
    public static final OrderTable orderTable1 = orderTable_groupId_추가(empty_orderTable1, tableGroup.getId(), false);
    public static final OrderTable orderTable2 = orderTable_groupId_추가(empty_orderTable2, tableGroup.getId(), false);
    public static final List<OrderTable> 그룹으로_묶여있는_orderTables = Arrays.asList(orderTable1, orderTable2);

    public static Menu menu_생성(Long id, String name, BigDecimal price, Long menuGroupId) {
        return Menu.of(id, name, price, menuGroupId);
    }

    public static Product product_생성(Long id, String name, BigDecimal price) {
        return Product.of(id, name, price);
    }

    public static MenuProduct menuProduct_생성(Long productId, long quantity) {
        return MenuProduct.of(productId, quantity);
    }

    public static TableGroup tableGroup_생성(Long id) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        return tableGroup;
    }

    public static TableGroup tableGroup_orderTables_추가(TableGroup tableGroup, List<OrderTable> orderTables) {
        TableGroup newTableGroup = tableGroup_생성(tableGroup.getId());
        newTableGroup.setOrderTables(orderTables);
        return newTableGroup;
    }

    public static OrderTable orderTable_생성(Long id, boolean empty) {
        return OrderTable.of(id, 0, empty);
    }

    public static OrderTable 빈_orderTable_생성(Long id) {
        return orderTable_생성(id, true);
    }

    public static OrderTable orderTable_groupId_추가(OrderTable orderTable, Long tableGroupId, boolean empty) {
        OrderTable newOrderTable = orderTable_생성(orderTable.getId(), empty);
        newOrderTable.setTableGroupId(tableGroupId);
        return newOrderTable;
    }

    public static OrderTable orderTable_numberOfGuests_추가(OrderTable orderTable, int numberOfGuests) {
        OrderTable newOrderTable = orderTable_생성(orderTable.getId(), orderTable.isEmpty());
        newOrderTable.setNumberOfGuests(numberOfGuests);
        return newOrderTable;
    }
}
