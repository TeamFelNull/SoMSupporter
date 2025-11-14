package dev.felnull.somsupporter.config;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public enum KeyBind {
    OPEN_BACKPACK(
            new KeyBinding(
            "key.somsupporter.open_backpack",          // 表示名（langファイルで翻訳可）
            KeyConflictContext.IN_GAME,              // ゲーム内のみ
            KeyModifier.NONE,
            InputMappings.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_B), // デフォルトBキー
            "key.categories.somsupporter")            // 設定画面カテゴリ
            ),
    OPEN_TRASH(
            new KeyBinding(
            "key.somsupporter.open_trash",          // 表示名（langファイルで翻訳可）
            KeyConflictContext.IN_GAME,              // ゲーム内のみ
            KeyModifier.NONE,
            InputMappings.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_DELETE), // デフォルトDeleteキー
            "key.categories.somsupporter"            // 設定画面カテゴリ
    ));

    private KeyBinding keyBinding;

    KeyBind(KeyBinding keyBinding) {
        this.keyBinding = keyBinding;
    }

    public KeyBinding getKeyBinding() {
        return keyBinding;
    }
}
