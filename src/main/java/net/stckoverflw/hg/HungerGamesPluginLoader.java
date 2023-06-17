package net.stckoverflw.hg;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class HungerGamesPluginLoader implements PluginLoader {
    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addDependency(new Dependency(new DefaultArtifact("net.axay:kspigot:1.20.1"), null));
        var scoreboardLibraryVersion = "2.0.0-RC9";

        resolver.addDependency(new Dependency(new DefaultArtifact("com.github.megavexnetwork.scoreboard-library:scoreboard-library-api:" + scoreboardLibraryVersion), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("com.github.megavexnetwork.scoreboard-library:scoreboard-library-implementation:" + scoreboardLibraryVersion), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("com.github.megavexnetwork.scoreboard-library:scoreboard-library-extra-kotlin:" + scoreboardLibraryVersion), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("com.github.megavexnetwork.scoreboard-library:scoreboard-library-v1_20_R1:" + scoreboardLibraryVersion), null));

        resolver.addRepository(new RemoteRepository.Builder("maven central", "default", "https://repo.maven.apache.org/maven2/").build());
        resolver.addRepository(new RemoteRepository.Builder("stckoverflw.net", "default", "https://maven.stckoverflw.net/releases").build());
        resolver.addRepository(new RemoteRepository.Builder("jitpack", "default", "https://jitpack.io/").build());

        classpathBuilder.addLibrary(resolver);
    }
}
