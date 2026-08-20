import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("The Stream Kata Master Suite")
@SelectPackages("io.code.vanguard.brew")
@IncludeTags("Stream")
public class StreamKatasSuite {
}
