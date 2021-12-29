package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TableGroupRepositoryTest {

	@Autowired
	private TableGroupRepository tableGroupRepository;

	@Test
	@DisplayName("테이블그룹 저장 테스트")
	public void saveTableGroupTest() {
		//given
		TableGroup tableGroup = new TableGroup();

		//when
		TableGroup save = tableGroupRepository.save(tableGroup);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(3L);
	}

	@Test
	@DisplayName("테이블그룹 목록 조회 테스트")
	public void findAllTableGroupTest() {
		//when
		List<TableGroup> tableGroups = tableGroupRepository.findAll();

		//then
		assertThat(tableGroups).hasSizeGreaterThanOrEqualTo(2);
	}

	@Test
	@DisplayName("테이블그룹 id로 조회 테스트")
	public void findByIdTableGroupTest() {
		//when
		TableGroup tableGroup = tableGroupRepository.findById(1L).orElse(new TableGroup());

		//then
		assertThat(tableGroup.getId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("테이블그룹 없는 id로 조회 테스트")
	public void findByIdTableGroupFailTest() {
		//when
		TableGroup tableGroup = tableGroupRepository.findById(99L).orElse(new TableGroup());

		//then
		assertThat(tableGroup.getId()).isNull();
	}
}
