package dev.felnull.somsupporter.config;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public final class KeyBindings {
    private KeyBindings() {}

    public static KeyBinding RUN_COMMAND;

    public static void register() {
        RUN_COMMAND = new KeyBinding(
                "key.somsupporter.run_command",          // 表示名（langファイルで翻訳可）
                KeyConflictContext.IN_GAME,              // ゲーム内のみ
                KeyModifier.NONE,
                InputMappings.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_B), // デフォルトBキー
                "key.categories.somsupporter"            // 設定画面カテゴリ
        );
        ClientRegistry.registerKeyBinding(RUN_COMMAND);
    }
}