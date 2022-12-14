package kitchenpos;

import kitchenpos.utils.DatabaseCleanupByEntity;
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
@Import(DatabaseCleanupByEntity.class)
public class JpaEntityTest {

    @Autowired
    private DatabaseCleanupByEntity databaseCleanupByEntity;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        databaseCleanupByEntity.execute();
    }

    protected void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
