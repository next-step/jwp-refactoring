package kitchenpos;

import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.table.domain.TableGroupValidator;
import kitchenpos.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({DatabaseCleanup.class, OrderTableValidator.class, TableGroupValidator.class})
public abstract class JpaEntityTest {
    @Autowired
    private OrderTableValidator orderTableValidator;
    @Autowired
    private TableGroupValidator tableGroupValidator;
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
    }

    protected void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    protected OrderTableValidator getOrderTableValidator() {
        return orderTableValidator;
    }

    protected TableGroupValidator getTableGroupValidator() {
        return tableGroupValidator;
    }
}
