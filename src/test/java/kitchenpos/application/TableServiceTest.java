package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("테이블 서비스")
public class TableServiceTest extends ServiceTestBase {
    private final TableService tableService;

    @Autowired
    public TableServiceTest(TableService tableService) {
        this.tableService = tableService;
    }

    @BeforeEach
    void setUp() {
        setUpProduct();
        setUpMenuGroup();
        setUpMenu();
        setUpOrderTable();
        setUpTableGroup();
        setUpOrder();
    }

    @DisplayName("테이블 생성")
    @Test
    void create() {
        OrderTable savedOrderTable = tableService.create(groupedTable1);
        assertThat(savedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("착석 상태 변경")
    @Test
    void changeEmpty() {
        emptyTable.setEmpty(false);

        OrderTable updatedOrderTable = tableService.changeEmpty(emptyTable.getId(), emptyTable);
        assertThat(updatedOrderTable.isEmpty()).isFalse();
    }

    @DisplayName("등록되지 않은 테이블의 착석 상태 변경")
    @Test
    void changeEmptyWithNotExists() {
        OrderTable newTable = new OrderTable();
        newTable.setId(99L);
        newTable.setEmpty(false);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(newTable.getId(), newTable));
    }

    @DisplayName("그룹이 있는 테이블의 착석 상태 변경")
    @Test
    void changeEmptyWithTableGroup() {
        groupedTable1.setEmpty(false);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(groupedTable1.getId(), groupedTable1));
    }

    @DisplayName("테이블을 이용중인 경우의 착석 상태 변경")
    @Test
    void changeEmptyWithUsing() {
        cookingTable.setEmpty(true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(cookingTable.getId(), cookingTable));
    }

    @DisplayName("고객 수 변경")
    @Test
    void changeNumberOfGuest() {
        emptyTable.setEmpty(false);
        emptyTable.setNumberOfGuests(4);
        OrderTable updatedOrderTable = tableService.changeNumberOfGuests(emptyTable.getId(), emptyTable);
        assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(4);
    }

    @DisplayName("고객 수를 음수로 변경")
    @Test
    void changeNumberOfGuestWithNegative() {
        emptyTable.setEmpty(false);
        emptyTable.setNumberOfGuests(-1);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(emptyTable.getId(), emptyTable));
    }

    @DisplayName("등록되지 않은 테이블의 고객 수 변경")
    @Test
    void changeNumberOfGuestWithNotExists() {
        OrderTable newTable = new OrderTable();
        newTable.setId(99L);
        newTable.setEmpty(false);
        newTable.setNumberOfGuests(4);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(newTable.getId(), newTable));
    }

}
