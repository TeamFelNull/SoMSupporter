package dev.felnull.somsupporter.feature.party;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;

import java.util.Collection;

public class PartyOnlineChecker {

    public static boolean isOnline(String name) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getConnection() == null) return false;

        Collection<NetworkPlayerInfo> infos = mc.getConnection().getOnlinePlayers();
        for (NetworkPlayerInfo info : infos) {
            if (info.getProfile().getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
