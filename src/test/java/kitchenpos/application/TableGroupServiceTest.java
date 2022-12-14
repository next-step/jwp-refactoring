package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class TableGroupServiceTest {
    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    TableService tableService;

    @Autowired
    MenuService menuService;

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderTableDao orderTableDao;

    @Test
    @DisplayName("테이블 단체 지정 성공")
    void createTest(){
        // given
        OrderTable orderTable1 = tableService.create(new OrderTable(0, true));
        OrderTable orderTable2 = tableService.create(new OrderTable(0, true));
        TableGroup tableGroup = new TableGroup(
                Arrays.asList(orderTable1, orderTable2)
        );

        // when
        TableGroup savedGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedGroup.getId()).isNotNull();
    }

    @Test
    @DisplayName("단체 지정할 테이블이 없으면, 단체 지정에 실패한다.")
    void createFailTest1(){
        // given

        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(new TableGroup())
        );

        // then
    }

    @Test
    @DisplayName("단체 지정할 테이블이 존재하지 않는 테이블이면, 단체 지정에 실패한다.")
    void createFailTest2(){
        // given
        TableGroup tableGroup = new TableGroup(
                Arrays.asList(new OrderTable(0, true))
        );

        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(tableGroup)
        );

        // then
    }

    @Test
    @DisplayName("비어있지 않은 테이블은 단체 지정할 수 없다.")
    void createFailTest3(){
        // given
        OrderTable orderTable1 = tableService.create(new OrderTable(0, false));
        OrderTable orderTable2 = tableService.create(new OrderTable(0, true));
        TableGroup tableGroup = new TableGroup(
                Arrays.asList(orderTable1, orderTable2)
        );

        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(tableGroup)
        );

        // then
    }

    @Test
    @DisplayName("이미 단체로 지정된 테이블은 단체 지정될 수 없다.")
    void createFailTest4(){
        // given
        OrderTable orderTable1 = tableService.create(new OrderTable(0, true));
        OrderTable orderTable2 = tableService.create(new OrderTable(0, true));
        tableGroupService.create(new TableGroup(
                Arrays.asList(orderTable1, orderTable2)
        ));
        OrderTable orderTable3 = tableService.create(new OrderTable(0, true));
        TableGroup tableGroup = new TableGroup(
                Arrays.asList(orderTable1, orderTable3)
        );

        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(tableGroup)
        );

        // then
    }

    @Test
    @DisplayName("테이블 단체 지정 해제 테스트")
    void unGroupTest(){
        // given
        OrderTable orderTable1 = tableService.create(new OrderTable(0, true));
        OrderTable orderTable2 = tableService.create(new OrderTable(0, true));
        TableGroup tableGroup = tableGroupService.create(new TableGroup(
                Arrays.asList(orderTable1, orderTable2)
        ));

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        OrderTable table1 = orderTableDao.findById(orderTable1.getId()).orElse(null);
        OrderTable table2 = orderTableDao.findById(orderTable1.getId()).orElse(null);
        assertThat(table1).isNotNull();
        assertThat(table1.getTableGroupId()).isNull();
        assertThat(table2).isNotNull();
        assertThat(table2.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("단체 지정 된 테이블 중 조리중인 주문이 있는 테이블이 있으면 그 단체를 해제할 수 없다.")
    void unGroupFailTest1(){
        // given
        OrderTable orderTable1 = tableService.create(new OrderTable(2, true));
        OrderTable orderTable2 = tableService.create(new OrderTable(2, true));
        TableGroup tableGroup = tableGroupService.create(new TableGroup(
                Arrays.asList(orderTable1, orderTable2)
        ));

        Product product1 = productService.create(new Product("상품1", new BigDecimal(1000)));
        Product product2 = productService.create(new Product("상품2", new BigDecimal(2000)));
        MenuGroup group1 = menuGroupService.create(new MenuGroup("그룹1"));
        Menu menu1 = menuService.create(new MenuRequest("메뉴1", new BigDecimal(1000), group1.getId(), Arrays.asList(
                new MenuProductRequest(product1.getId(), 1),
                new MenuProductRequest(product2.getId(), 1)
        )));
        orderService.create(new Order(orderTable1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()
                , Collections.singletonList(new OrderLineItem(null, menu1.getId(), 1))));


        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.ungroup(tableGroup.getId())
        );

        // then
    }

    @Test
    @DisplayName("단체 지정 된 테이블 중 식사중인 주문이 있는 테이블이 있으면 그 단체를 해제할 수 없다.")
    void unGroupFailTest2(){
        // given
        OrderTable orderTable1 = tableService.create(new OrderTable(2, true));
        OrderTable orderTable2 = tableService.create(new OrderTable(2, true));
        TableGroup tableGroup = tableGroupService.create(new TableGroup(
                Arrays.asList(orderTable1, orderTable2)
        ));

        Product product1 = productService.create(new Product("상품1", new BigDecimal(1000)));
        Product product2 = productService.create(new Product("상품2", new BigDecimal(2000)));
        MenuGroup group1 = menuGroupService.create(new MenuGroup("그룹1"));
        Menu menu1 = menuService.create(new MenuRequest("메뉴1", new BigDecimal(1000), group1.getId(), Arrays.asList(
                new MenuProductRequest(product1.getId(), 1),
                new MenuProductRequest(product2.getId(), 1)
        )));
        orderService.create(new Order(orderTable1.getId(), OrderStatus.MEAL.name(), LocalDateTime.now()
                , Collections.singletonList(new OrderLineItem(null, menu1.getId(), 1))));


        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.ungroup(tableGroup.getId())
        );

        // then
    }
}