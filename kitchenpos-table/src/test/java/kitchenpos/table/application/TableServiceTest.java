package kitchenpos.table.application;

import kitchenpos.ServiceTestBase;
import kitchenpos.exception.TableInUseException;
import kitchenpos.support.OrderSupport;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.application.TableGroupServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@DisplayName("테이블 서비스")
public class TableServiceTest extends ServiceTestBase {
    private final TableService tableService;
    private final TableGroupService tableGroupService;
    @MockBean
    private OrderSupport orderSupport;

    @Autowired
    public TableServiceTest(TableService tableService, TableGroupService tableGroupService) {
        this.tableService = tableService;
        this.tableGroupService = tableGroupService;
    }

    @DisplayName("테이블 생성")
    @Test
    void create() {
        TableResponse savedOrderTable = tableService.create();

        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @DisplayName("고객 수 변경")
    @Test
    void changeNumberOfGuest() {
        TableResponse savedTable = tableService.create();
        TableResponse updatedTable = tableService.update(savedTable.getId(), createRequest(4));

        assertThat(updatedTable.getNumberOfGuests()).isEqualTo(4);
    }

    @DisplayName("그룹이 지어진 테이블의 고객 수를 0으로 변경")
    @Test
    void changeNumberOfGuestWithGroup() {
        TableResponse savedTable = tableService.create();
        TableResponse savedTable2 = tableService.create();

        tableGroupService.create(TableGroupServiceTest.createTableGroup(savedTable, savedTable2));

        assertThatExceptionOfType(TableInUseException.class)
                .isThrownBy(() -> tableService.update(savedTable.getId(), createRequest(0)));
    }

    @DisplayName("이용중인 테이블의 고객 수를 0으로 변경")
    @Test
    void changeEmptyWithUsing() {
        TableResponse orderTable = tableService.create();

        when(orderSupport.isUsingTable(any())).thenReturn(true);

        assertThatExceptionOfType(TableInUseException.class)
                .isThrownBy(() -> tableService.update(orderTable.getId(), createRequest(0)));
    }

    @DisplayName("등록되지 않은 테이블의 고객 수 변경")
    @Test
    void changeNumberOfGuestWithNotExists() {
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> tableService.update(99L, createRequest(4)));
    }

    public static TableRequest createRequest(int numberOfGuests) {
        return new TableRequest(numberOfGuests);
    }
}
