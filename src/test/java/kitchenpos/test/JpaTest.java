package kitchenpos.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dao.OrderTableRepository;
import kitchenpos.table.dao.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

@DisplayName("임시테스트")
@DataJpaTest
public class JpaTest {
    @Autowired
    private MenuRepository menus;
    
    @Autowired
    private MenuGroupRepository menugroups;
    
    @Autowired
    private OrderRepository orders;
    
    @Autowired
    private OrderTableRepository tables;
    
    @Autowired
    private TableGroupRepository tableGroups;

    @Test
    void 메뉴() {
        // Given
        MenuGroup 메뉴그룹 = MenuGroup.from("치킨");
        menugroups.save(메뉴그룹);
        MenuProduct menuProduct = MenuProduct.of(1L, 15000);
        MenuProduct menuProduct2 = MenuProduct.of(2L, 15000);
        Menu expected = Menu.of("치킨", 30000, 메뉴그룹);
        expected.addMenuProducts(Arrays.asList(menuProduct, menuProduct2));
        
        // When
        Menu actual = menus.save(expected);
        
        for (MenuProduct mp : actual.getMenuProducts()) {
            System.out.println(mp.getSeq());
        }
        // Then
        assertAll(() -> assertThat(actual).isEqualTo(expected), 
                () -> assertThat(expected.getId()).isNotNull(), 
                () -> assertThat(expected.getId()).isEqualTo(actual.getId()));
    }
    
    @Test
    void 주문() {
        // Given
        Order expected = Order.of(1L, OrderStatus.MEAL, Arrays.asList(OrderLineItem.of(1L, 1000), OrderLineItem.of(2L, 1000)));
        
        // When
        Order actual = orders.save(expected);
        
        for (OrderLineItem item : actual.getOrderLineItems()) {
            System.out.println(item.getSeq());
        }
        // Then
        assertAll(() -> assertThat(actual).isEqualTo(expected), 
                () -> assertThat(expected.getId()).isNotNull(), 
                () -> assertThat(expected.getId()).isEqualTo(actual.getId()));
    }
    
    @Test
    void 테이블() {
        // Given
        OrderTable table1 = OrderTable.of(0, true);
        OrderTable table2 = OrderTable.of(0, true);
        tables.save(table1);
        tables.save(table2);
        TableGroup expected = TableGroup.from(Arrays.asList(table1, table2));
        
        // When
        TableGroup actual = tableGroups.save(expected);
        
        for (OrderTable table : actual.getOrderTables()) {
            System.out.println("group?");
            System.out.println(table.getTableGroupId());
        }
        
        actual.ungroup();
        
        System.out.println(actual.getOrderTables().size());
        System.out.println(table1.getTableGroupId());
        System.out.println(table2.getTableGroupId());
        
        // Then
        assertAll(() -> assertThat(actual).isEqualTo(expected), 
                () -> assertThat(expected.getId()).isNotNull(), 
                () -> assertThat(expected.getId()).isEqualTo(actual.getId()));
    }
}
