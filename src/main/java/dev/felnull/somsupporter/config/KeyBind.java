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
            "key.categories.somsupporter")            // 設定画面カテゴリ
    ),
    QUICK_PARTY_CREATE(
            new KeyBinding(
                    "key.somsupporter.quick_party_create",          // 表示名（langファイルで翻訳可）
                    KeyConflictContext.IN_GAME,              // ゲーム内のみ
                    KeyModifier.NONE,
                    InputMappings.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_O), // デフォルトDeleteキー
                    "key.categories.somsupporter")            // 設定画面カテゴリ
    ),
    QUICK_PARTY_INVITE(
            new KeyBinding(
                    "key.somsupporter.quick_party_invite",          // 表示名（langファイルで翻訳可）
                    KeyConflictContext.IN_GAME,              // ゲーム内のみ
                    KeyModifier.NONE,
                    InputMappings.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_I), // デフォルトDeleteキー
                    "key.categories.somsupporter")            // 設定画面カテゴリ
    ),
    OPEN_PARTY_SETTINGS(
            new KeyBinding(
                    "key.somsupporter.open_party_settings",
                    KeyConflictContext.IN_GAME,
                    KeyModifier.NONE,
                    InputMappings.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_P),
                    "key.categories.somsupporter"
            )
    );

    private KeyBinding keyBinding;

    KeyBind(KeyBinding keyBinding) {
        this.keyBinding = keyBinding;
    }

    public KeyBinding getKeyBinding() {
        return keyBinding;
    }
}
