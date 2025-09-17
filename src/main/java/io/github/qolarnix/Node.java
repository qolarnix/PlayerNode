package io.github.qolarnix;

import net.hollowcube.polar.AnvilPolar;
import net.hollowcube.polar.PolarLoader;
import net.hollowcube.polar.PolarWorld;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
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
            player.setRespawnPoint(new Pos(9, 0, 9));
            player.setGameMode(GameMode.SURVIVAL);
        });

        minecraftServer.start("0.0.0.0", 25565);
        OpenToLAN.open();
    }
}
