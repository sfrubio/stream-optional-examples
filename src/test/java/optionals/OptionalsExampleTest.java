package optionals;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wkda.commons.testautomation.api.clients.RulesetFetchApi;
import wkda.commons.testautomation.api.dto.datimport.RuleDTO;
import wkda.commons.testautomation.base.BaseApiJUnit4SpringTest;

import java.util.List;
import java.util.Optional;

public class OptionalsExampleTest extends BaseApiJUnit4SpringTest {

    @Autowired
    private RulesetFetchApi rulesetFetchApi;

    private List<RuleDTO> ruleset;

    @Before
    public void setUp() {
        ruleset = getRuleset();
    }

    @Test
    @SneakyThrows
    public void optionalOfExample() {
        String manufacturer = Optional.of(ruleset.get(0))
                .map(rule -> rule.getApiKey().getManufacturer())
                .orElseThrow(() -> new Exception("No rule found"));
        System.out.println(manufacturer);
    }

    @Test
    @SneakyThrows
    public void optionalIfPresentExample() {
        Optional.of(ruleset.get(0)).ifPresent(rule ->
                System.out.println(rule.getApiKey().getManufacturer()));
    }

    @Test
    @SneakyThrows
    public void optionalIsPresentExample() {
        boolean subtypeExtraExist = Optional.ofNullable(ruleset.get(0).getApiKey().getSubtypeExtra()).isPresent();
        System.out.println(subtypeExtraExist);
    }

    private List<RuleDTO> getRuleset() {
        return rulesetFetchApi.getMappingRuleItems("Abarth",null,null,100,0).getEntities();
    }
}
