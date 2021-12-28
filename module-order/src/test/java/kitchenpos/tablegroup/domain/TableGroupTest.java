package kitchenpos.tablegroup.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("테이블 그룹 테스트")
class TableGroupTest {

    @DisplayName("테이블 그룹 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // given
        LocalDateTime createdDate = LocalDateTime.now();

        // when
        TableGroup tableGroup = TableGroup.of(createdDate);

        // then
        assertAll(
                () -> assertThat(tableGroup).isNotNull()
                , () -> assertThat(tableGroup.getCreatedDate()).isEqualTo(createdDate)
        );
    }
}
