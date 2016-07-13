package org.apereo.cas.test.common
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.apereo.cas.test.login.*
import org.apereo.cas.test.validation.*

@RunWith(Suite.class)
@Suite.SuiteClasses([
    BadLoginSpec.class,
    GatewaySpec.class,
    LoginSpec.class,
    LoginSpecWithService.class,
    RenewSpec.class,
    ValidateSpec.class,
    MultiLevelProxySpec.class,
	PGTInvalidationByLogoutSpec.class,
	UnsuccessfulValidateServiceSpec.class,
	UnsuccessfulValidationAtServiceValidateSpec.class,
	UnsuccessfulValidationAtValidateSpec.class,
	ValidateFailureSpec.class,
	ValidateServiceSpec.class,
	ValidateServiceWithPGTSpec.class
])

public class TestSuite {
}
