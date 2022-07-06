package kitchenpos.domain.table;

import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있는_주문_테이블_생성;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있지_않은_주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Domain:TableGroup")
class TableGroupTest {

    private OrderTable 첫번째_빈_테이블, 두번째_빈_테이블;

    @BeforeEach
    void setUp() {
        첫번째_빈_테이블 = 비어있는_주문_테이블_생성();
        두번째_빈_테이블 = 비어있는_주문_테이블_생성();

    }

    private List<Long> 테이블_번호_목록_추출(final OrderTable... orderTables) {
        return Arrays.stream(orderTables)
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    @Test
    @DisplayName("단체석 지정")
    public void allocateAll() {
        // Given
        final List<Long> 테이블_번호_목록 = 테이블_번호_목록_추출(첫번째_빈_테이블, 두번째_빈_테이블);
        final List<OrderTable> 테이블_목록 = Arrays.asList(첫번째_빈_테이블, 두번째_빈_테이블);

        // When
        TableGroup.of(테이블_번호_목록, 테이블_목록);

        // Then
        assertThat(테이블_목록)
            .extracting(OrderTable::isGroupTable)
            .containsOnly(true);
    }

    @Test
    @DisplayName("빈좌석이 아닌 테이블이 포함되있는 경우 단체석 지정 예외 발생 검증")
    public void throwException_WhenContainsIsNotEmptyOrderTable() {
        final OrderTable 비어있지_않은_테이블 = 비어있지_않은_주문_테이블_생성();
        final List<Long> 테이블_번호_목록 = 테이블_번호_목록_추출(첫번째_빈_테이블, 비어있지_않은_테이블);
        final List<OrderTable> 테이블_목록 = Arrays.asList(첫번째_빈_테이블, 비어있지_않은_테이블);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> TableGroup.of(테이블_번호_목록, 테이블_목록));
    }

    @Test
    @DisplayName("이미 단체 지정된 주문 테이블이 포함된 경우 예외 발생 검증")
    public void throwException_WhenContainsAlreadyGroupedOrderTable() {
        // Given
        final OrderTable 단체_지정된_주문_테이블 = 비어있는_주문_테이블_생성();
        TableGroup.of(
            테이블_번호_목록_추출(단체_지정된_주문_테이블),
            Collections.singletonList(단체_지정된_주문_테이블)
        );

        final List<Long> 테이블_번호_목록 = 테이블_번호_목록_추출(첫번째_빈_테이블, 단체_지정된_주문_테이블);
        final List<OrderTable> 테이블_목록 = Arrays.asList(첫번째_빈_테이블, 단체_지정된_주문_테이블);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> TableGroup.of(테이블_번호_목록, 테이블_목록));
    }

    @Test
    @DisplayName("단체석 해제")
    public void deallocateAll() {
        // Given
        final List<Long> 테이블_번호_목록 = 테이블_번호_목록_추출(첫번째_빈_테이블, 두번째_빈_테이블);
        final List<OrderTable> 테이블_목록 = Arrays.asList(첫번째_빈_테이블, 두번째_빈_테이블);
        final TableGroup 단체석 = TableGroup.of(테이블_번호_목록, 테이블_목록);

        // When
        단체석.deallocateOrderTable();

        // Then
        assertThat(테이블_목록)
            .extracting(OrderTable::isGroupTable)
            .containsOnly(false);
    }
}
