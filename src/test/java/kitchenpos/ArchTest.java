package kitchenpos;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.*;

import org.junit.jupiter.api.*;

import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.*;
import com.tngtech.archunit.lang.*;

public class ArchTest {

    @DisplayName("패키지 순환 참조 검사하기")
    @Test
    void packageCycleTests() {
        JavaClasses classes = new ClassFileImporter()
            .withImportOption(
                new com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests())
            .importPackages("kitchenpos");

        ArchRule freeOfCycles = slices().matching("kitchenpos.(*)..")
            .should().beFreeOfCycles();
        freeOfCycles.check(classes);
    }
}
