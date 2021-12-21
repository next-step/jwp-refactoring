package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.sql.DataSource;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import kitchenpos.domain.OrderTable;

class JdbcTemplateOrderTableDaoTest {

	private static DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
		.addScript("classpath:db/migration/V1__Initialize_project_tables.sql")
		.addScript("classpath:db/migration/V2__Insert_default_data.sql")
		.build();

	private static JdbcTemplateOrderTableDao templateOrderTableDao = new JdbcTemplateOrderTableDao(dataSource);

	@Test
	@DisplayName("주문테이블 저장 테스트")
	public void saveOrderTableTest() {
		//given
		OrderTable orderTable = new OrderTable(null, 1L, 0, true);

		//when
		OrderTable save = templateOrderTableDao.save(orderTable);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(9L);
	}

	@Test
	@DisplayName("주문테이블 목록 조회 테스트")
	public void findAllOrderTableTest() {
		//given

		//when
		List<OrderTable> orderTables = templateOrderTableDao.findAll();

		//then
		assertThat(orderTables).hasSizeGreaterThanOrEqualTo(8);
	}

	@Test
	@DisplayName("주문테이블 id로 조회 테스트")
	public void findByIdOrderTableTest() {
		//given

		//when
		OrderTable orderTable = templateOrderTableDao.findById(1L).orElse(new OrderTable());

		//then
		assertThat(orderTable.getId()).isEqualTo(1);
	}

	@Test
	@DisplayName("없는 주문테이블 id로 조회 테스트")
	public void findByIdOrderTableFailTest() {
		//given

		//when
		OrderTable orderTable = templateOrderTableDao.findById(10L).orElse(new OrderTable());

		//then
		assertThat(orderTable.getId()).isNull();
	}

	@Test
	@DisplayName("주문테이블 ID여러개로 조회")
	public void findAllByIdInTest() {
		//given

		//when
		List<OrderTable> orderTables = templateOrderTableDao.findAllByIdIn(Lists.newArrayList(1L,2L,3L));

		//then
		assertThat(orderTables).hasSize(3);
	}

	@Test
	@DisplayName("테이블그룹 ID로 주문테이블 조회")
	public void findAllByTableGroupIdTest() {
		//given

		//when
		List<OrderTable> orderTables = templateOrderTableDao.findAllByTableGroupId(2L);

		//then
		assertThat(orderTables).hasSize(0);
	}

}
