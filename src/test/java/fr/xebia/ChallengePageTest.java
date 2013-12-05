package fr.xebia;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.naming.resources.VirtualDirContext;
import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class ChallengePageTest extends FluentTest {
    @ClassRule
    public static ExternalResource resource = new ExternalResource() {
        private Tomcat tomcat;

        @Override
        protected void before() throws Throwable {
            tomcat = new Tomcat();
            tomcat.setPort(8080);
            Context ctx = tomcat.addWebapp("/", new File("src/main/webapp").getAbsolutePath());

            VirtualDirContext resources = new VirtualDirContext();
            resources.setExtraResourcePaths("/WEB-INF/classes=target/classes");
            ctx.setResources(resources);

            tomcat.start();
        }

        @Override
        protected void after() {
            try {
                tomcat.stop();
            } catch (LifecycleException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public WebDriver getDefaultDriver() {
        return new SafariDriver();
    }

    @Test
    public void should_have_a_title() throws Exception {
        goTo("http://localhost:8080");

        assertThat(title()).isEqualTo("Xebia Face Match");
        assertThat($("h1").first().getText()).isEqualTo("Xebia Face Match");
    }

    @Test
    public void should_click_on_first_image() throws Exception {
        goTo("http://localhost:8080");
        await().until("#name").withText().contains(Pattern.compile(".+"));
        String firstName = $("#name").first().getText();

        click("#firstImage");

        await().until("#name").withText().notContains(firstName);
    }
}
