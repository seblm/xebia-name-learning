package fr.xebia;

import net.codestory.simplelenium.SeleniumTest;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.By;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.tagName;

public class ChallengePageTest extends SeleniumTest {
    @ClassRule
    public static TomcatRule tomcat = new TomcatRule("0 1 true 2 3 false");

    @Override
    protected String getDefaultBaseUrl() {
        return "http://localhost:8080";
    }

    @Before
    public void restartUI() {
        goTo("/");

        find(".guess").should().match(Pattern.compile("Où est (?:Alexandre Dergham|Anne Beauchart) \\?"));

        if ("Où est Anne Beauchart ?".equals(find(".guess").getText())) {
            restartUI(); // servlet is statefull : we need to reset UI as it was when servlet was started for the first time
        }
    }

    @Test
    public void should_have_a_title() throws Exception {
        assertThat(title()).isEqualTo("Xebia Name Learning");
    }

    @Test
    public void should_display_images_name_and_initial_score() {
        assertThat(getDriver().findElement(className("firstImage")).findElement(tagName("img")).getAttribute("src")).endsWith("/images/Alexandre%20Dergham.jpg");
        assertThat(getDriver().findElement(className("secondImage")).findElement(tagName("img")).getAttribute("src")).endsWith("/images/Alexandre%20Dutra.jpg");
        assertThat(find(".guess").getText()).isEqualTo("Où est Alexandre Dergham ?");
        assertThat(getDriver().findElement(className("score")).findElement(tagName("span")).getText()).isEqualTo("0");
    }

    @Test
    public void should_click_on_first_image_and_display_next_challenge() throws InterruptedException {
        getDriver().findElement(className("firstImage")).click();

        assertThat(getDriver().findElement(className("score")).findElement(tagName("span")).getText()).isEqualTo("1");
        Thread.sleep(2000); // TODO ugly
        assertThat(getDriver().findElement(className("firstImage")).findElement(tagName("img")).getAttribute("src")).endsWith("/images/Alexis%20Kinsella.jpg");
        assertThat(getDriver().findElement(className("secondImage")).findElement(tagName("img")).getAttribute("src")).endsWith("/images/Anne%20Beauchart.jpg");
        assertThat(find(".guess").getText()).isEqualTo("Où est Anne Beauchart ?");
    }

    @Test
    public void should_click_on_good_image_and_win() {
        getDriver().findElement(className("firstImage")).click();

        assertThat(getDriver().findElement(className("score")).findElement(tagName("span")).getText()).isEqualTo("1");
    }

    @Test
    public void should_click_on_bad_image_and_loose() {
        getDriver().findElement(className("secondImage")).click();

        assertThat(getDriver().findElement(className("score")).findElement(tagName("span")).getText()).isEqualTo("-1");
    }
}
