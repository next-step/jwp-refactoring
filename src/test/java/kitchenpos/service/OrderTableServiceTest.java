package kitchenpos.service;

import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.MenuGroupRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.order.OrderLineMenuRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.dto.table.TableGroupResponse;
import kitchenpos.dto.table.TableRequest;
import kitchenpos.dto.table.TableResponse;
import kitchenpos.service.menu.MenuGroupService;
import kitchenpos.service.menu.MenuService;
import kitchenpos.service.order.OrderService;
import kitchenpos.service.product.ProductService;
import kitchenpos.service.table.TableGroupService;
import kitchenpos.service.table.TableService;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("주문 테이블 관련 테스트")
@Transactional
@SpringBootTest
public class OrderTableServiceTest {
    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    @PersistenceContext
    private EntityManager em;

    @DisplayName("주문 테이블 생성 테스트")
    @Test
    void saveOrderTable() {
        TableResponse tableResponse = tableService.save(new TableRequest(0, true));

        assertThat(tableResponse.getId()).isNotNull();
    }

    @DisplayName("주문 테이블 조회 테스트")
    @Test
    void findOrderTable() {
        tableService.save(new TableRequest(0, true));
        List<TableResponse> tableResponses = tableService.findAll();

        assertThat(tableResponses).hasSize(1);
        assertThat(tableResponses).extracting("guestNumber").containsExactly(0);
        assertThat(tableResponses).extracting("empty").containsExactly(true);
    }

    @DisplayName("주문 테이블 변경 테스트(Empty)")
    @Test
    void changeOrderTable1() {
        TableResponse savedTableResponse = tableService.save(new TableRequest(0, true));
        TableResponse changedTableResponse = tableService.changeEmpty(savedTableResponse.getId(), new TableRequest(false));

        em.flush();
        assertThat(changedTableResponse.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블 변경 테스트(Guest)")
    @Test
    void changeOrderTable2() {
        TableResponse savedTableResponse = tableService.save(new TableRequest(0, true));
        tableService.changeEmpty(savedTableResponse.getId(), new TableRequest(false));
        doOrder(savedTableResponse);
        TableResponse changedTableResponse = tableService.changeGuestNumber(savedTableResponse.getId(), new TableRequest(4));

        em.flush();
        assertThat(changedTableResponse.getGuestNumber()).isEqualTo(4);
    }
    @DisplayName("주문 테이블 그룹화 테스트")
    @Test
    void group() {
        TableResponse table1 = tableService.save(new TableRequest(0, true));
        TableResponse table2 = tableService.save(new TableRequest(0, true));
        TableGroupResponse tableGroupResponse = tableGroupService.applyGroup(new TableGroupRequest(asList(new TableRequest(table1.getId()), new TableRequest(table2.getId()))));

        em.flush();
        assertThat(tableGroupResponse.getTableResponses()).extracting("id").containsExactly(table1.getId(), table2.getId());
    }

    @DisplayName("주문 테이블 그룹화 실패 테스트")
    @Test
    void groupFail() {
        TableResponse table1 = tableService.save(new TableRequest(0, true));
        TableResponse table2 = tableService.save(new TableRequest(0, true));
        tableService.changeEmpty(table1.getId(), new TableRequest(false));

        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.applyGroup(new TableGroupRequest(asList(new TableRequest(table1.getId()), new TableRequest(table2.getId()))))
        );
    }

    @DisplayName("주문 테이블 그룹화 해제 테스트")
    @Test
    void unGroup() {
        TableResponse table1 = tableService.save(new TableRequest(0, true));
        TableResponse table2 = tableService.save(new TableRequest(0, true));
        TableGroupResponse tableGroupResponse = tableGroupService.applyGroup(new TableGroupRequest(asList(new TableRequest(table1.getId()), new TableRequest(table2.getId()))));
        em.flush();
        tableGroupService.applyUnGroup(tableGroupResponse.getId());
        em.flush();

        assertThat(tableService.findOrderTable(table1.getId())).extracting("orderTableGroup").isNull();
        assertThat(tableService.findOrderTable(table2.getId())).extracting("orderTableGroup").isNull();
    }

    private void doOrder(TableResponse tableResponse) {
        MenuResponse menu1 = createMenu("후라이드 치킨", "양념 치킨", "두마리 치킨");
        MenuResponse menu2 = createMenu("라면", "김밥", "김밥이 라면");
        orderService.save(new OrderRequest(tableResponse.getId(),
                asList(new OrderLineMenuRequest(menu1.getId(), 2), new OrderLineMenuRequest(menu2.getId(), 1))));
    }

    private MenuResponse createMenu(String productName1, String productName2, String menuName) {
        MenuGroupResponse menuGroup = menuGroupService.save(new MenuGroupRequest("인기 메뉴"));
        return create(menuGroup, productName1, productName2, menuName);
    }

    private MenuResponse create(MenuGroupResponse menuGroup, String productName1, String productName2, String menuName) {
        ProductResponse product1 = productService.save(new Product(productName1, new Price(18000)));
        ProductResponse product2 = productService.save(new Product(productName2, new Price(20000)));
        MenuRequest menuRequest = new MenuRequest(menuName, 20000, menuGroup.getId(),
                asList(new MenuProductRequest(product1.getId(), 1), new MenuProductRequest(product2.getId(), 1)));
        return menuService.save(menuRequest);
    }
}
