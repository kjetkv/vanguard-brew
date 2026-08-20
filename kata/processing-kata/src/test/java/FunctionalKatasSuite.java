import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("The Functional Kata Master Suite")
@SelectPackages("io.code.vanguard.brew")
@IncludeTags("Functional")
public class FunctionalKatasSuite {
}
