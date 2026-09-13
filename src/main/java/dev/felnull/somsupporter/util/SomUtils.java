package dev.felnull.somsupporter.util;

import com.google.common.collect.ImmutableList;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public final class SomUtils {

    /*
    アイテムの説明文サンプル

    洞窟にとどまっていた探検家の亡霊の魂から作られた杖
    ====[[| Information |]]====
    ・レアリティ: ★★★★★★
    ・カテゴリ: 武器, ・売値: 100
    ====[[| Equipment |]]====
    ・二つ名: 普通の 手に馴染む 頑丈な
    ・装備種: ロッド, ・魔法力: 70~80
    ・命中力: 185, ・魂力: 35
    ・移動速度: 15, ・クリティカル確率: 3%
    ・クリティカル倍率: 3%
    ・キャスト時間短縮: 10%
    ・クールタイム短縮: 3%
    ・耐久値: 97/97
    ====[[| Requirement |]]====
    ・必要レベル: 44
     */

    private static final Pattern DESC_SEPARATE_REGEX = Pattern.compile("====\\[\\[\\| .+ \\|]]====");
    private static final String EQUIPMENT_SEPARATE_TEXT = "====[[| Equipment |]]====";

    private SomUtils() {
    }

    /**
     * アイテムスタックから説明文を取得
     *
     * @param stack アイテムスタック
     * @return 説明文を改行ごとに区切った文字列のリスト
     */
    public static List<String> getLore(ItemStack stack) {
        // hasTag() -> hasTag() (1.20.4 でもそのまま利用可能)
        if (!stack.hasTag()) {
            return ImmutableList.of();
        }

        // CompoundNBT -> CompoundTag
        CompoundTag itemTag = Objects.requireNonNull(stack.getTag());

        // Constants.NBT.TAG_COMPOUND -> Tag.TAG_COMPOUND
        if (!itemTag.contains("display", Tag.TAG_COMPOUND)) {
            return ImmutableList.of();
        }

        CompoundTag displayTag = itemTag.getCompound("display");

        // ListNBT -> ListTag, Constants.NBT.TAG_STRING -> Tag.TAG_STRING
        ListTag nbtList = displayTag.getList("Lore", Tag.TAG_STRING);

        List<String> ret = new ArrayList<>();
        for (int i = 0; i < nbtList.size(); i++) {
            // ITextComponent -> Component
            // Component.Serializer.fromJsonString(...) を使用
            Component textComp = Component.Serializer.fromJson(nbtList.getString(i));
            if (textComp != null) {
                ret.add(textComp.getString());
            }
        }
        return ret;
    }

    /**
     * 説明文から残り耐久値と最大耐久値を取得
     *
     * @param loreTextList 説明文のリスト
     * @return 残り耐久値と最大耐久値のペア
     */
    public static Pair<Integer, Integer> getDurability(List<String> loreTextList) {

        // 装備情報の区切り位置を特定
        int equipmentSeparateIdx = loreTextList.indexOf(EQUIPMENT_SEPARATE_TEXT);
        if (equipmentSeparateIdx == -1 || equipmentSeparateIdx == (loreTextList.size() - 1)) {
            return null;
        }

        // 装備情報の区切り位置から、次の区切り位置までの文字列を検索
        String durabilityText = null;
        for (int i = (equipmentSeparateIdx + 1); i < loreTextList.size(); i++) {
            String loreText = loreTextList.get(i);
            if (DESC_SEPARATE_REGEX.matcher(loreText).matches()) {
                break;
            }
            if (loreText.startsWith("・耐久値: ")) {
                durabilityText = loreText;
                break;
            }
        }

        if (durabilityText == null) {
            return null;
        }

        // 耐久値テキストから値を取得
        String[] numTexts = durabilityText.substring("・耐久値: ".length()).split("/");
        if (numTexts.length < 2) {
            return null;
        }

        int remain = NumberUtils.toInt(numTexts[0], -1);
        int max = NumberUtils.toInt(numTexts[1], -1);

        if (remain == -1 || max == -1) {
            return null;
        }

        return Pair.of(remain, max);
    }
}
