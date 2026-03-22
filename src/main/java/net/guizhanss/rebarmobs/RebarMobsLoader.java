package net.guizhanss.rebarmobs;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class RebarMobsLoader implements PluginLoader {

    private static final String KOTLIN_VERSION = "2.3.10";

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        // Use Paper's recommended mirror to avoid Maven Central rate limits
        resolver.addRepository(
                new RemoteRepository.Builder(
                                "central",
                                "default",
                                MavenLibraryResolver.MAVEN_CENTRAL_DEFAULT_MIRROR)
                        .build());

        resolver.addDependency(
                new Dependency(
                        new DefaultArtifact("org.jetbrains.kotlin:kotlin-stdlib:" + KOTLIN_VERSION),
                        null));

        resolver.addDependency(
                new Dependency(
                        new DefaultArtifact(
                                "org.jetbrains.kotlin:kotlin-reflect:" + KOTLIN_VERSION),
                        null));

        classpathBuilder.addLibrary(resolver);
    }
}
