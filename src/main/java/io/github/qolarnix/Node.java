package io.github.qolarnix;

import net.hollowcube.polar.AnvilPolar;
import net.hollowcube.polar.PolarLoader;
import net.hollowcube.polar.PolarWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.lan.OpenToLAN;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;

import java.io.IOException;
import java.nio.file.Path;

public class Node {
    public static void main(String[] args) {
        MinecraftServer.setCompressionThreshold(0);
        MinecraftServer minecraftServer = MinecraftServer.init();

        MojangAuth.init();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        PolarWorld defaultNodeWorld = null;
        try {
            defaultNodeWorld = AnvilPolar.anvilToPolar(Path.of("worlds/DefaultNodeWorld"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        instanceContainer.setChunkLoader(new PolarLoader(defaultNodeWorld));
        instanceContainer.setChunkSupplier(LightingChunk::new);

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            event.setSpawningInstance(instanceContainer);

            Player player = event.getPlayer();
            player.setRespawnPoint(new Pos(0.5, 0, 0.5));
            player.setGameMode(GameMode.SURVIVAL);
        });

        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            Player player = event.getPlayer();
           if(event.isFirstSpawn()) {
               Audiences.all().sendMessage(Component.text(
                       player.getUsername() + " has joined the world",
                       NamedTextColor.GREEN
               ));
           }
        });

        minecraftServer.start("0.0.0.0", 25565);
        OpenToLAN.open();
    }
}
