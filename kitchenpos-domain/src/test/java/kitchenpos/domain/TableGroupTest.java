package kitchenpos.domain;

import static kitchenpos.domain.DomainTestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import kitchenpos.exception.AlreadyTableGroupException;

@DisplayName("TableGroup 도메인 테스트")
class TableGroupTest {

	@DisplayName("create 메소드 비어있는 테이블 객체 리스트를 전달 받으면 테이블리스트와 생성시간이 설정된 TableGroup인스턴스를 생성한다.")
	@Test
	void create() {
		TableGroup result = TableGroup.create(Arrays.asList(빈_테이블_객체1(), 빈_테이블_객체2()));

		assertAll(
			() -> assertThat(result).isInstanceOf(TableGroup.class),
			() -> assertThat(result.getCreatedDate()).isNotNull(),
			() -> assertThat(result.getOrderTables()).hasSize(2)
		);
	}

	@DisplayName("create 메소드에 비어있지 않은 테이블 객체 또는 단체테이블이 지정된 테이블을 전달하면 AlreadyTableGroupException이 발생한다.")
	@ParameterizedTest
	@MethodSource("paramProductThrow")
	void createThrow(OrderTable orderTable) {
		assertThatExceptionOfType(AlreadyTableGroupException.class)
			.isThrownBy(() -> {
				TableGroup.create(Arrays.asList(orderTable, 빈_테이블_객체2()));
			});
	}

	public static Stream<Arguments> paramProductThrow() {
		return Stream.of(
			Arguments.of(비어있지않은_테이블_객체()),
			Arguments.of(그룹_지정된_테이블_객체())
		);
	}

	@DisplayName("ungroup 메소드 대상 테이블의 tableGroup정보가 null이 된다.")
	@Test
	void ungroup() {
		TableGroup tableGroup = TableGroup.create(Arrays.asList(빈_테이블_객체1(), 빈_테이블_객체2()));

		tableGroup.ungroup();

		assertThat(tableGroup.getOrderTables())
			.extracting("tableGroupId").containsExactly(null, null);

	}

}