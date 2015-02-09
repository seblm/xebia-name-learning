package fr.xebia;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;
import org.junit.rules.ExternalResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;
import static org.apache.catalina.WebResourceRoot.ResourceSetType.POST;
import static org.assertj.core.api.Assertions.assertThat;

class TomcatRule extends ExternalResource {
    private final String questions;

    private Tomcat tomcat;

    TomcatRule(String questions) {
        this.questions = questions;
    }

    @Override
    protected void before() throws Throwable {
        tomcat = new Tomcat();
        tomcat.setPort(8080);

        Context ctx = tomcat.addWebapp("/", new File("src/main/webapp").getAbsolutePath());
        addChallengeServletInitParameter(ctx, "questions", questions);
        StandardRoot resources = new StandardRoot(ctx);
        resources.createWebResourceSet(POST, "/WEB-INF/classes", new File("target/classes").toURI().toURL(), "/");
        ctx.setResources(resources);

        tomcat.start();
    }

    private void addChallengeServletInitParameter(Context ctx, String name, String value) {
        ctx.addContainerListener(event -> {
            if ("addChild".equals(event.getType()) && event.getData() instanceof Wrapper) {
                Wrapper wrapper = (Wrapper) event.getData();
                if ("challenge".equals(wrapper.getName())) {
                    wrapper.addInitParameter(name, value);
                }
            }
        });
    }

    @Override
    protected void after() {
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            e.printStackTrace();
        } finally {
            File directoryToDelete = new File("tomcat.8080");
            try {
                Files.walkFileTree(directoryToDelete.toPath(), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        return deleteAndContinue(file);
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        return deleteAndContinue(dir);
                    }

                    private FileVisitResult deleteAndContinue(Path file) throws IOException {
                        Files.delete(file);
                        return CONTINUE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            assertThat(directoryToDelete.delete()).isTrue();
        }
    }
}
