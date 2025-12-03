package dev.felnull.somsupporter.feature.party;

import net.minecraft.client.Minecraft;

public class PartyActions {

    public static void sendCommand(String cmd) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        mc.player.chat(cmd);
    }

    public static void createParty(String name) {
        sendCommand("/party create " + name);
    }

    public static void leaveParty() {
        sendCommand("/party leave");
    }

    public static void invite(String target) {
        sendCommand("/party invite " + target);
    }

    // ショートカット用（JSONから読む想定）
    public static void quickCreateDefaultParty() {
        PartyConfig cfg = PartyConfigManager.get(); // JSONからロード済みとして扱う
        if (cfg.defaultPartyName != null && !cfg.defaultPartyName.isEmpty()) {
            createParty(cfg.defaultPartyName);
        }
    }

    public static void quickInviteDefaultFriend() {
        PartyConfig cfg = PartyConfigManager.get();
        if (cfg.defaultInviteTarget != null && !cfg.defaultInviteTarget.isEmpty()) {
            invite(cfg.defaultInviteTarget);
        }
    }
}
