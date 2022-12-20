package kitchenpos.table.tablegroup.domain;

import kitchenpos.table.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("단체 지정 테스트")
public class TableGroupTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        assertThatNoException().isThrownBy(() -> TableGroup.from(LocalDateTime.now()));
    }
}
