package dev.felnull.somsupporter.config;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public enum KeyBind {
    OPEN_BACKPACK(
            new KeyMapping(
                    "key.somsupporter.open_backpack",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_B,
                    "key.categories.somsupporter"
            )
    ),
    OPEN_TRASH(
            new KeyMapping(
                    "key.somsupporter.open_trash",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_DELETE,
                    "key.categories.somsupporter"
            )
    );

    private final KeyMapping keyMapping;

    KeyBind(KeyMapping keyMapping) {
        this.keyMapping = keyMapping;
    }

    public KeyMapping getKeyBinding() {
        return keyMapping;
    }

    public static void register() {
        for (KeyBind keyBind : values()) {
            KeyBindingHelper.registerKeyBinding(keyBind.keyMapping);
        }
    }
}
