package fr.xebia;

import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class ChallengePageTest extends FluentTest {
    @ClassRule
    public static TomcatRule tomcat = new TomcatRule("0 1 true 2 3 false");

    @Override
    public WebDriver getDefaultDriver() {
        return new SafariDriver();
    }

    @Test
    public void should_have_a_title() throws Exception {
        goTo("http://localhost:8080/");

        assertThat(title()).isEqualTo("Xebia Face Match");
        assertThat($("h1").first().getText()).isEqualTo("Xebia Face Match");
    }

    @Test
    public void should_display_images_and_name() throws Exception {
        goTo("http://localhost:8080/");

        awaitUntilPageLoaded();

        assertThat($("#firstImage").first().html()).isEqualTo("<img src=\"/images/Christophe%20Heub%C3%A8s.jpg\"/>");
        assertThat($("#secondImage").first().html()).isEqualTo("<img src=\"/images/Florent%20Le%20Gall.jpg\"/>");
        assertThat($("#name").first().getText()).isEqualTo("Christophe Heubès");
    }

    @Test
    public void should_click_on_first_image() throws Exception {
        goTo("http://localhost:8080/");
        awaitUntilPageLoaded();
        String firstName = $("#name").first().getText();

        click("#firstImage");

        await().until("#name").withText().notContains(firstName);
    }

    private void awaitUntilPageLoaded() {
        await().until("#name").withText().contains(Pattern.compile(".+"));
    }

}
