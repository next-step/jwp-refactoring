package kitchenpos.table.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@DisplayName("주문 테이블 통합테스트")
public class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("테이블 관리")
    void tableManage() {
        // given
        // 빈 테이블 생성
        OrderTableRequest 빈_테이블 = new OrderTableRequest(0, true);
        // when
        // 빈 테이블을 등록한다.
        OrderTableResponse savedTable = tableService.create(빈_테이블);
        // then
        // 빈 테이블이 정상적으로 등록된다.
        assertAll(
                () -> assertThat(savedTable.getTableGroupId()).isNull(),
                () -> assertThat(savedTable.isEmpty()).isTrue()
        );

        // when
        // 테이블 리스트를 조회한다.
        List<OrderTableResponse> savedTables = tableService.list();
        // then
        // 테이블 리스트가 정상적으로 조회된다.
        assertAll(
                () -> assertThat(savedTables).hasSize(10),
                () -> assertThat(savedTables).contains(savedTable)
        );

        // when
        // 테이블 상태를 변경한다.
        OrderTableResponse changedEmptyTable = tableService.changeEmpty(
                savedTable.getId(), new ChangeEmptyRequest(false)
        );
        // then
        // 테이블 상태가 변경된다.
        assertThat(changedEmptyTable.isEmpty()).isFalse();

        // when
        // 테이블 인원을 변경한다.
        OrderTableResponse changedNumberTable = tableService.changeNumberOfGuests(
                changedEmptyTable.getId(), new ChangeGuestsRequest(1)
        );
        // then
        // 테이블 인원이 변경된다.
        assertThat(changedNumberTable.getNumberOfGuests()).isEqualTo(1);
    }
}
