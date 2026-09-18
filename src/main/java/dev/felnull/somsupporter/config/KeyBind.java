package dev.felnull.somsupporter.config;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;


public enum KeyBind {
    OPEN_BACKPACK(
            new KeyMapping(
                    "key.somsupporter.open_backpack",          // 表示名（langファイルで翻訳可）
                    KeyConflictContext.IN_GAME,                // ゲーム内のみ
                    KeyModifier.NONE,
                    InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_B), // デフォルトBキー
                    "key.categories.somsupporter"              // 設定画面カテゴリ
            )
    ),
    OPEN_TRASH(
            new KeyMapping(
                    "key.somsupporter.open_trash",
                    KeyConflictContext.IN_GAME,
                    KeyModifier.NONE,
                    InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_DELETE), // デフォルトDeleteキー
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
}
