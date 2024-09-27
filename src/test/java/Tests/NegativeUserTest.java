package Tests;

import PODJO.User;
import Utils.ApiWrapper;
import Utils.ConfigurationReader;
import Utils.DataHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static Utils.GetDataHelper.getVolume;
import static org.hamcrest.Matchers.equalTo;

public class NegativeUserTest extends BaseTestCase {

    @ParameterizedTest
//    @CsvFileSource (resources = "src/test/resources/paramGoRestTest.csv", numLinesToSkip = 1)
    @ValueSource(strings = {"  ", ""})
    public void incorrectUserNameTest(String input) {
        User newUser = DataHelper.createUser();
        newUser.setName(input);
        ApiWrapper.sendPostNegativeRequest(ConfigurationReader.get("userPath"),
                newUser)
                .body("[0].field", equalTo("name"))
                .body("[0].message", equalTo(ConfigurationReader.get("failName")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"  ", "", "frau"})
    public void incorrectUserGenderTest(String input) {
        User newUser = DataHelper.createUser();
        newUser.setGender(input);
        ApiWrapper.sendPostNegativeRequest(ConfigurationReader.get("userPath"),
                        newUser)
                .body("[0].field", equalTo("gender"))
                .body("[0].message", equalTo(ConfigurationReader.get("failGender")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"john_doe.var.com", "john_doe@var.com ", " john_doe@var.com", "@gmail.com"})
    public void incorrectUserEmailTest(String input) {
        User newUser = DataHelper.createUser();
        newUser.setEmail(input);
        ApiWrapper.sendPostNegativeRequest(ConfigurationReader.get("userPath"),
                        newUser)
                .body("[0].field", equalTo("email"))
                .body("[0].message", equalTo(ConfigurationReader.get("failEmail")));
    }

    @Test
    public void creatingUserWithExistingEmailTest() {
        String existingEmail = getVolume("userPath", "email");
        User newUser = DataHelper.createUser();
        newUser.setEmail(existingEmail);
        ApiWrapper.sendPostNegativeRequest(ConfigurationReader.get("userPath"),
                        newUser)
                .body("[0].field", equalTo("email"))
                .body("[0].message", equalTo(ConfigurationReader.get("failEmailExist")));
    }


    @Test
    public void createUserLessToken() {
        User newUser = DataHelper.createUser();

        ApiWrapper.sendPostWithoutAuthRequest(ConfigurationReader.get("userPath"),newUser)
                .body("message", equalTo(ConfigurationReader.get("failAuthentication")));
    }
}