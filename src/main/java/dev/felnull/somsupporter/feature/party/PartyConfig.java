package dev.felnull.somsupporter.feature.party;

import java.util.ArrayList;
import java.util.List;

public class PartyConfig {

    public String defaultPartyName = "";
    public String defaultInviteTarget = "";

    public List<PartyPreset> partyPresets = new ArrayList<>();
    public List<FriendEntry> friends = new ArrayList<>();

    // -------------------------
    // nested classes
    // -------------------------   

    public static class PartyPreset {
        public String name;
    }

    public static class FriendEntry {
        public String name;
    }
}
