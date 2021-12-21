package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

class JdbcTemplateOrderDaoTest {

	private static DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
		.addScript("classpath:db/migration/V1__Initialize_project_tables.sql")
		.addScript("classpath:db/migration/V2__Insert_default_data.sql")
		.build();

	private static JdbcTemplateOrderDao jdbcTemplateOrderDao = new JdbcTemplateOrderDao(dataSource);

	@Test
	@DisplayName("주문 저장 테스트")
	public void saveOrderTest() {
		//given
		Order order = new Order(null, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);

		//when
		Order save = jdbcTemplateOrderDao.save(order);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(8L);
		assertThat(save.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
	}

	@Test
	@DisplayName("주문 목록 조회 테스트")
	public void findAllOrderTest() {
		//given

		//when
		List<Order> orders = jdbcTemplateOrderDao.findAll();

		//then
		assertThat(orders).hasSizeGreaterThanOrEqualTo(2);
	}

	@Test
	@DisplayName("주문 id로 조회 테스트")
	public void findByIdOrderTest() {
		//given

		//when
		Order order = jdbcTemplateOrderDao.findById(1L).orElse(new Order());

		//then
		assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
	}

	@Test
	@DisplayName("없는 주문 id로 조회 테스트")
	public void findByIdMenuFailTest() {
		//given

		//when
		Order order = jdbcTemplateOrderDao.findById(8L).orElse(new Order());

		//then
		assertThat(order.getId()).isNull();
	}

	@ParameterizedTest
	@CsvSource(value = {"4:true", "5:false"}, delimiter = ':')
	@DisplayName("주문테이블 ID 1개, 주문상태 1개이상으로 주문 존재 여부 테스트")
	public void existsByOrderTableIdAndOrderStatusInTest(long id, boolean expected) {
		//given

		//when
		boolean result = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(id, Lists.newArrayList(OrderStatus.MEAL.name()));

		//then
		assertThat(result).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource(value = {"4,6:false", "5,7:true"}, delimiter = ':')
	@DisplayName("주문테이블 ID, 주문상태 1개 이상으로 주문 존재 여부 테스트")
	public void existsByOrderTableIdInAndOrderStatusInTest(String ids, boolean expected) {
		//given
		String[] split = ids.split(",");
		ArrayList<Long> orderTableIds = Lists.newArrayList(Long.valueOf(split[0]),Long.valueOf(split[1]));
		ArrayList<String> orderStatus = Lists.newArrayList(OrderStatus.COOKING.name());

		//when
		boolean result = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,orderStatus);

		//then
		assertThat(result).isEqualTo(expected);
	}

}
