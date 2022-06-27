package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.util.dto.SaveMenuDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderTableTestFixture orderTableTestFixture;

    private OrderTable orderTable;

    @BeforeEach
    public void setUp() {
        super.setUp();

        orderTable = this.orderTableRepository.save(new OrderTable(4, false));
    }

    @Test
    @DisplayName("테이블을 생성한다.")
    void create() {
        OrderTableResponse response = crateTable(0, false);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("모든 테이블을 조회한다.")
    void list() {
        crateTable(0, false);

        List<OrderTableResponse> orderTableResponses = this.tableService.list();

        assertThat(orderTableResponses).hasSize(2);
    }

    @Test
    @DisplayName("테이블의 빈 상태가 변경된다.")
    void changeEmpty() {
        OrderTableRequest emptyRequest = new OrderTableRequest(true);

        OrderTableResponse response = this.tableService.changeEmpty(orderTable.getId(), emptyRequest);

        assertThat(response.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("테이블 그룹에 포함될 경우 테이블의 빈 상태를 바꿀 수 없다.")
    void changeEmptyFail_existTableGroup() {
        TableGroup tableGroup = this.tableGroupRepository.save(new TableGroup());
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(tableGroup.getId(), 4, false),
            new OrderTable(tableGroup.getId(), 4, false));
        orderTableRepository.saveAll(orderTables);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableService.changeEmpty(orderTables.get(0).getId(), new OrderTableRequest(true)));
    }

    @Test
    @DisplayName("주문 상태가 식사중이거나 조리중인 테이블이 있다면 빈 상태를 바꿀 수 없다.")
    void changeEmptyFail_existOrderCookingOrMeal() {
        Product product = this.productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        MenuProduct menuProduct1 = new MenuProduct(product.getId(), 1);
        MenuProduct menuProduct2 = new MenuProduct(product.getId(), 1);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);

        SaveMenuDto saveMenuDto = new SaveMenuDto(menuProducts, new MenuGroup("메뉴 그룹"), "메뉴", 32000);
        orderTableTestFixture.메뉴_만들고_주문하기(saveMenuDto, 1, orderTable);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableService.changeEmpty(this.orderTable.getId(), new OrderTableRequest(true)));
    }

    @Test
    @DisplayName("테이블의 인원수를 변경한다.")
    void changeNumberOfGuests() {
        OrderTableResponse response = this.tableService.changeNumberOfGuests(orderTable.getId(), new OrderTableRequest(3));

        assertThat(response.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    @DisplayName("변경하고자 하는 손님의 숫자가 음수일 경우 에러를 던진다.")
    void changeNumberOfGuestsFail_numberOfGuests() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableService.changeNumberOfGuests(orderTable.getId(), new OrderTableRequest(-1)));
    }

    @Test
    @DisplayName("변경하고자 하는 테이블이 없을 경우 에러를 던진다.")
    void changeNumberOfGuestsFail_orderTable() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableService.changeNumberOfGuests(Long.MAX_VALUE, new OrderTableRequest(3)));
    }

    private OrderTableResponse crateTable(int numberOfGuests, boolean empty) {
        return this.tableService.create(new OrderTableRequest(numberOfGuests, empty));
    }

}
