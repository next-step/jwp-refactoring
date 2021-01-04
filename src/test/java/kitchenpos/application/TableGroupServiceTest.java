package kitchenpos.application;

import static kitchenpos.common.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.BaseTest;
import kitchenpos.common.TestDataUtil;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("TableGroupService 테스트")
class TableGroupServiceTest extends BaseTest {

	@Autowired
	private TableGroupService tableGroupService;

	@Autowired
	private TableGroupDao tableGroupDao;

	@Autowired
	private OrderTableDao orderTableDao;

	@DisplayName("테이블들을 단체 지정할 수 있다.")
	@Test
	void create() {

		TableGroup tableGroup = tableGroupService.create(TestDataUtil.createTableGroup(Arrays.asList(예제테이블1, 예제테이블2)));

		TableGroup savedTableGroup = tableGroupDao.findById(tableGroup.getId()).orElse(null);
		OrderTable targetTable1 = orderTableDao.findById(예제테이블1.getId()).orElse(null);
		OrderTable targetTable2 = orderTableDao.findById(예제테이블2.getId()).orElse(null);

		assertAll(
			() -> assertThat(savedTableGroup.getId()).isNotNull(),
			() -> assertThat(targetTable1.isEmpty()).isFalse(),
			() -> assertThat(targetTable2.isEmpty()).isFalse()
		);

	}

	@DisplayName("단체테이블로 지정할 테이블의 요청 갯수가 2개 미만이면 단체지정할 수 없다.")
	@Test
	void createThrow1() {

		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				tableGroupService.create(TestDataUtil.createTableGroup(Arrays.asList(예제테이블1)));
			});

	}

	@DisplayName("요청된 테이블 중 실제 주문테이블이 존재하지 않은 테이블이 포함되면 단체 지정할수 없다.")
	@Test
	void createThrow2() {

		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				tableGroupService.create(TestDataUtil.createTableGroup(Arrays.asList(존재하지않는테이블, 예제테이블1)));
			});

	}

	@DisplayName("요청된 테이블 중 빈테이블이 아닌 테이블이 있는 경우  단체 지정할 수 없다.")
	@Test
	void createThrow3() {

		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				tableGroupService.create(TestDataUtil.createTableGroup(Arrays.asList(비어있지않은테이블, 예제테이블1)));
			});
	}

	@DisplayName("이미 단체로 지정되어 있는 경우 단체 지정할 수 없다.")
	@Test
	void createThrow4() {

		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				tableGroupService.create(TestDataUtil.createTableGroup(Arrays.asList(단체지정되어있지만_주문없는_테이블, 예제테이블1)));
			});
	}

	@DisplayName("테이블들의 단체 지정을 해제할 수 있다.")
	@Test
	void ungroup() {

		tableGroupService.ungroup(예제_테이블_그룹_ID);

		OrderTable 단체지정되어있던_테이블 = orderTableDao.findById(단체지정되어있지만_주문없는_테이블.getId()).orElse(null);

		assertAll(
			() -> assertThat(단체지정되어있던_테이블).isNotNull(),
			() -> assertThat(단체지정되어있던_테이블.getTableGroupId()).isNull()
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