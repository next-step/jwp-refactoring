package kitchenpos.application;

import static kitchenpos.common.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.BaseTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

@DisplayName("TableGroupService 테스트")
class TableGroupServiceTest extends BaseTest {

	@Autowired
	private TableGroupService tableGroupService;

	@Autowired
	private TableGroupRepository tableGroupRepository;

	@Autowired
	private OrderTableRepository orderTableRepository;

	@DisplayName("테이블들을 단체 지정할 수 있다.")
	@Test
	void create() {

		TableGroupResponse tableGroup = tableGroupService.create(TableGroupRequest.of(Arrays.asList(예제테이블1_ID, 예제테이블2_ID)));

		TableGroup savedTableGroup = tableGroupRepository.findById(tableGroup.getId()).orElse(null);
		OrderTable targetTable1 = orderTableRepository.findById(예제테이블1_ID).orElse(null);
		OrderTable targetTable2 = orderTableRepository.findById(예제테이블2_ID).orElse(null);

		assertAll(
			() -> assertThat(savedTableGroup.getId()).isNotNull(),
			() -> assertThat(targetTable1.isEmpty()).isFalse(),
			() -> assertThat(targetTable2.isEmpty()).isFalse()
		);

	}

	@DisplayName("요청된 테이블 중 실제 주문테이블이 존재하지 않은 테이블이 포함되면 단체 지정할수 없다.")
	@Test
	void createThrow2() {

		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				tableGroupService.create(TableGroupRequest.of(Arrays.asList(존재하지않는_테이블ID, 예제테이블1_ID)));
			});

	}

	@DisplayName("요청된 테이블 중 빈테이블이 아닌 테이블이 있는 경우  단체 지정할 수 없다.")
	@Test
	void createThrow3() {

		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				tableGroupService.create(TableGroupRequest.of(Arrays.asList(비어있지않은테이블_ID, 예제테이블1_ID)));
			});
	}

	@DisplayName("이미 단체로 지정되어 있는 경우 단체 지정할 수 없다.")
	@Test
	void createThrow4() {

		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				tableGroupService.create(TableGroupRequest.of(Arrays.asList(단체지정되어있지만_주문없는_테이블_ID, 예제테이블1_ID)));
			});
	}

	@DisplayName("테이블들의 단체 지정을 해제할 수 있다.")
	@Test
	void ungroup() {

		tableGroupService.ungroup(예제_테이블_그룹_ID);

		OrderTable 단체지정되어있던_테이블 = orderTableRepository.findById(단체지정되어있지만_주문없는_테이블_ID).orElse(null);

		assertAll(
			() -> assertThat(단체지정되어있던_테이블).isNotNull(),
			() -> assertThat(단체지정되어있던_테이블.getTableGroup()).isNull()
		);
	}

	@DisplayName("단체지정된 테이블들 중 상태가 조리 또는 식사인 테이블이 있는 경우 단체 지정을 해제할 수 없다.")
	@Test
	void ungroupThrow() {

		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				tableGroupService.ungroup(조리상태인_테이블그룹_ID);
			});
	}

}