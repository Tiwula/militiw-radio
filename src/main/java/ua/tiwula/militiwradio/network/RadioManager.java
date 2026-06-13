package ua.tiwula.militiwradio.network;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RadioManager
{
    private static final Map<Short, Set<UUID>> FREQUENCY_MAP = new ConcurrentHashMap<>();
    private static final Map<UUID, Short> TRANSMITTING_MAP = new ConcurrentHashMap<>();

    public static void addListener(short freq, UUID player)
    {
        FREQUENCY_MAP.computeIfAbsent(freq, k -> new HashSet<>()).add(player);
    }

    public static void removeListener(short freq, UUID playerId)
    {
        Set<UUID> set = FREQUENCY_MAP.get(freq);
        if (set == null) return;

        set.remove(playerId);

        if (set.isEmpty())
            FREQUENCY_MAP.remove(freq);
    }

    public static void removeListener(UUID playerId)
    {
        for (Map.Entry<Short, Set<UUID>> entry : FREQUENCY_MAP.entrySet())
        {
            if (entry.getValue() == null) return;

            entry.getValue().remove(playerId);

            if (entry.getValue().isEmpty())
                FREQUENCY_MAP.remove(entry.getKey());
        }
    }

    public static Set<UUID> getListeners(short freq)
    {
        return FREQUENCY_MAP.getOrDefault(freq, Set.of());
    }

    public static void startTalking(UUID player, short freq)
    {
        TRANSMITTING_MAP.put(player, freq);
    }

    public static void stopTalking(UUID player)
    {
        if (isTalking(player)) TRANSMITTING_MAP.remove(player);
    }

    public static boolean isTalking(UUID player)
    {
        return TRANSMITTING_MAP.containsKey(player);
    }

    public static boolean isFreqBusy(short freq, ServerPlayer sender, PlayerList list, int maxDist)
    {
        for (Map.Entry<UUID, Short> entry : TRANSMITTING_MAP.entrySet())
        {
            if (entry.getValue() != freq) continue;
            ServerPlayer recPlayer = list.getPlayer(entry.getKey());
            if (recPlayer == null) continue;
            if (sender.distanceTo(recPlayer) <= maxDist)
                return true;
        }
        return false;
    }
}
