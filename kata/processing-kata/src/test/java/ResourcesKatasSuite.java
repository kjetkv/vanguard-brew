import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("The Resources Kata Master Suite")
@SelectPackages("io.code.vanguard.brew")
@IncludeTags("Resources")
public class ResourcesKatasSuite {
}
