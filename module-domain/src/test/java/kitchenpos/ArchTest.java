package kitchenpos;

import org.junit.jupiter.api.*;

import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.*;
import com.tngtech.archunit.lang.*;
import com.tngtech.archunit.library.dependencies.*;

public class ArchTest {

    @DisplayName("패키지 순환 참조 검사하기")
    @Test
    void packageCycleTests() {
        JavaClasses classes = new ClassFileImporter()
            .withImportOption(
                new com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests())
            .importPackages("kitchenpos");

        ArchRule freeOfCycles = SlicesRuleDefinition.slices().matching("kitchenpos.(*)..")
            .should().beFreeOfCycles();
        freeOfCycles.check(classes);
    }
}
