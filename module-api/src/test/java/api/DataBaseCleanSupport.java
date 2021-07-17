package api;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class DataBaseCleanSupport {

    @Autowired
    private EntityManager entityManager;

    @AfterEach
    void tearDown() {
        this.entityManager
                .createNativeQuery("ALTER TABLE order_table ALTER COLUMN `id` RESTART WITH 9")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE menu_product ALTER COLUMN `seq` RESTART WITH 7")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE menu ALTER COLUMN `id` RESTART WITH 7")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE product ALTER COLUMN `id` RESTART WITH 7")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE menu_group ALTER COLUMN `id` RESTART WITH 5")
                .executeUpdate();
    }
}
