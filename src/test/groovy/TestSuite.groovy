import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite.class)
@Suite.SuiteClasses([
    LoginSpec.class,
    LoginSpecWithService.class,
    BadLoginSpec.class,
    GatewaySpec.class
])

public class TestSuite {
	

}
