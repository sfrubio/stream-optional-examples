package streams;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wkda.common.dto.datimport.ruleset.RuleItemDTO;
import wkda.commons.testautomation.api.clients.RulesetFetchApi;
import wkda.commons.testautomation.api.dto.datimport.RuleDTO;
import wkda.commons.testautomation.base.BaseApiJUnit4SpringTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class StreamExamplesTest extends BaseApiJUnit4SpringTest {

    @Autowired
    private RulesetFetchApi rulesetFetchApi;

    private List<RuleDTO> ruleset;

    @Before
    public void setUp() {
        ruleset = getRuleset();
    }

    @Test
    public void filterExample() {
        List<RuleDTO> filteredRuleset = ruleset.stream()
                .filter(rule -> rule.getApiKey().getSubtype().equals("1.4 Turbo")
                        && rule.getApiKey().getMainType().equals("595C"))
                .collect(Collectors.toList());
        assertThat(filteredRuleset, hasSize(greaterThan(0)));

    }

    @Test
    public void anyMatchExample() {
        boolean filteredRuleset = ruleset.stream()
                .anyMatch(rule -> rule.getApiKey().getSubtype().equals("1.4 Turbo")
                        && rule.getApiKey().getMainType().equals("595C"));
        assertThat(filteredRuleset, is(true));

    }

    @Test
    @SneakyThrows
    public void findAnyExample() {
        RuleDTO filteredRuleset = ruleset.stream()
                .findAny().orElseThrow(() -> new Exception("No rule found"));
        assertThat(filteredRuleset, not(nullValue()));
    }

    @Test
    @SneakyThrows
    public void findFirstExample() {
        RuleDTO filteredRuleset = ruleset.stream()
                .findFirst().orElseThrow(() -> new Exception("No rule found"));
        assertThat(filteredRuleset, not(nullValue()));
    }

    @Test
    public void mapExample() {
        List<String> subtypes = ruleset.stream()
                .map(rule -> rule.getApiKey().getSubtype())
                .collect(Collectors.toList());
        assertThat(subtypes, hasSize(greaterThan(0)));
    }

    @Test
    public void collectorsToMapExample() {
        AtomicInteger index = new AtomicInteger(0);
        Map<Integer, String> subtypes = ruleset.stream()
                .map(rule -> rule.getApiKey().getSubtype())
                .collect(Collectors.toMap(
                        subtype -> index.incrementAndGet(),
                        subtype -> Optional.ofNullable(subtype).orElse("")));
        System.out.println(subtypes);
    }

    @Test
    @SneakyThrows
    public void flatMapExample() {
        RuleItemDTO ruleItem = ruleset.stream()
                .flatMap(rule -> rule.getItems().stream())
                .filter(item -> item.getEcode().equals("010200600050001"))
                .findFirst().orElseThrow(() -> new Exception("No item found"));
        assertThat(ruleItem.getEcode(), is("010200600050001"));
    }

    @Test
    public void intStreamExample() {
        List<String> threeSubtypes = IntStream.range(0, 3)
                .mapToObj(i -> ruleset.get(i).getApiKey().getSubtype())
                .collect(Collectors.toList());
        System.out.println(threeSubtypes);
    }

    @Test
    public void arraysStreamExample() {
        List<String> dictionaryNames = Arrays.stream(DictionaryName.values())
                .map(name -> name.toString())
                .collect(Collectors.toList());
        System.out.println(dictionaryNames);
    }

    private List<RuleDTO> getRuleset() {
        return rulesetFetchApi.getMappingRuleItems("Abarth", null, null, 100, 0).getEntities();
    }

    private enum DictionaryName {
        driveType, gearType, bodyType, bodyForm, fuelType
    }
}
