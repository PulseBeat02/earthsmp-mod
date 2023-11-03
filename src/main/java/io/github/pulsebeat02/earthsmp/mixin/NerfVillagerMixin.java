package io.github.pulsebeat02.earthsmp.mixin;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(TradeOffers.class)
public final class NerfVillagerMixin {
  @Final @Shadow
  public static Map<VillagerProfession, Int2ObjectMap<TradeOffers.Factory[]>>
      PROFESSION_TO_LEVELED_TRADE;

  static {
    final Int2ObjectMap<TradeOffers.Factory[]> farmer =
        copyToFastUtilMap(
            ImmutableMap.of(
                1,
                new TradeOffers.Factory[] {
                  new TradeOffers.BuyItemFactory(Items.WHEAT, 20, 16, 2),
                  new TradeOffers.BuyItemFactory(Items.POTATO, 26, 16, 2),
                  new TradeOffers.BuyItemFactory(Items.CARROT, 22, 16, 2),
                  new TradeOffers.BuyItemFactory(Items.BEETROOT, 15, 16, 2),
                  new TradeOffers.SellItemFactory(Items.BREAD, 1, 6, 16, 1)
                },
                2,
                new TradeOffers.Factory[] {
                  new TradeOffers.BuyItemFactory(Blocks.PUMPKIN, 12, 12, 10),
                  new TradeOffers.SellItemFactory(Items.PUMPKIN_PIE, 1, 4, 5),
                  new TradeOffers.SellItemFactory(Items.APPLE, 1, 4, 16, 5)
                },
                3,
                new TradeOffers.Factory[] {
                  new TradeOffers.SellItemFactory(Items.COOKIE, 3, 18, 10),
                  new TradeOffers.BuyItemFactory(Blocks.MELON, 8, 12, 20)
                },
                4,
                new TradeOffers.Factory[] {
                  new TradeOffers.SellItemFactory(Blocks.CAKE, 1, 1, 12, 15),
                  new TradeOffers.SellSuspiciousStewFactory(StatusEffects.NIGHT_VISION, 100, 15),
                  new TradeOffers.SellSuspiciousStewFactory(StatusEffects.JUMP_BOOST, 160, 15),
                  new TradeOffers.SellSuspiciousStewFactory(StatusEffects.WEAKNESS, 140, 15),
                  new TradeOffers.SellSuspiciousStewFactory(StatusEffects.BLINDNESS, 120, 15),
                  new TradeOffers.SellSuspiciousStewFactory(StatusEffects.POISON, 280, 15),
                  new TradeOffers.SellSuspiciousStewFactory(StatusEffects.SATURATION, 7, 15)
                },
                5,
                new TradeOffers.Factory[] {
                  new TradeOffers.SellItemFactory(Items.GOLDEN_CARROT, 6, 3, 30),
                  new TradeOffers.SellItemFactory(Items.GLISTERING_MELON_SLICE, 4, 3, 30)
                }));
    PROFESSION_TO_LEVELED_TRADE.put(VillagerProfession.FARMER, farmer);

    final Int2ObjectMap<TradeOffers.Factory[]> librarian =
        copyToFastUtilMap(
            ImmutableMap.<Integer, TradeOffers.Factory[]>builder()
                .put(
                    1,
                    new TradeOffers.Factory[] {
                      new TradeOffers.BuyItemFactory(Items.PAPER, 48, 16, 2),
                      new TradeOffers.EnchantBookFactory(1),
                      new TradeOffers.SellItemFactory(Blocks.BOOKSHELF, 18, 1, 12, 1)
                    })
                .put(
                    2,
                    new TradeOffers.Factory[] {
                      new TradeOffers.BuyItemFactory(Items.BOOK, 8, 12, 10),
                      new TradeOffers.EnchantBookFactory(5),
                      new TradeOffers.SellItemFactory(Items.LANTERN, 1, 1, 5)
                    })
                .put(
                    3,
                    new TradeOffers.Factory[] {
                      new TradeOffers.BuyItemFactory(Items.INK_SAC, 5, 12, 20),
                      new TradeOffers.EnchantBookFactory(10),
                      new TradeOffers.SellItemFactory(Items.GLASS, 1, 4, 10)
                    })
                .put(
                    4,
                    new TradeOffers.Factory[] {
                      new TradeOffers.BuyItemFactory(Items.WRITABLE_BOOK, 2, 12, 30),
                      new TradeOffers.EnchantBookFactory(15),
                      new TradeOffers.SellItemFactory(Items.CLOCK, 5, 1, 15),
                      new TradeOffers.SellItemFactory(Items.COMPASS, 4, 1, 15)
                    })
                .put(
                    5,
                    new TradeOffers.Factory[] {
                      new TradeOffers.SellItemFactory(Items.NAME_TAG, 20, 1, 30)
                    })
                .build());
    PROFESSION_TO_LEVELED_TRADE.put(VillagerProfession.LIBRARIAN, librarian);

    final Int2ObjectMap<TradeOffers.Factory[]> armorer =
        copyToFastUtilMap(
            ImmutableMap.of(
                1,
                new TradeOffers.Factory[] {
                  new TradeOffers.BuyItemFactory(Items.COAL, 30, 16, 2),
                  new TradeOffers.SellItemFactory(
                      new ItemStack(Items.IRON_LEGGINGS), 7, 1, 12, 1, 0.2F),
                  new TradeOffers.SellItemFactory(
                      new ItemStack(Items.IRON_BOOTS), 4, 1, 12, 1, 0.2F),
                  new TradeOffers.SellItemFactory(
                      new ItemStack(Items.IRON_HELMET), 5, 1, 12, 1, 0.2F),
                  new TradeOffers.SellItemFactory(
                      new ItemStack(Items.IRON_CHESTPLATE), 9, 1, 12, 1, 0.2F)
                },
                2,
                new TradeOffers.Factory[] {
                  new TradeOffers.BuyItemFactory(Items.IRON_INGOT, 8, 12, 10),
                  new TradeOffers.SellItemFactory(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2F),
                  new TradeOffers.SellItemFactory(
                      new ItemStack(Items.CHAINMAIL_BOOTS), 1, 1, 12, 5, 0.2F),
                  new TradeOffers.SellItemFactory(
                      new ItemStack(Items.CHAINMAIL_LEGGINGS), 3, 1, 12, 5, 0.2F)
                },
                3,
                new TradeOffers.Factory[] {
                  new TradeOffers.BuyItemFactory(Items.LAVA_BUCKET, 1, 12, 20),
                  new TradeOffers.BuyItemFactory(Items.DIAMOND, 1, 12, 20),
                  new TradeOffers.SellItemFactory(
                      new ItemStack(Items.CHAINMAIL_HELMET), 1, 1, 12, 10, 0.2F),
                  new TradeOffers.SellItemFactory(
                      new ItemStack(Items.CHAINMAIL_CHESTPLATE), 4, 1, 12, 10, 0.2F),
                  new TradeOffers.SellItemFactory(new ItemStack(Items.SHIELD), 5, 1, 12, 10, 0.2F)
                },
                4,
                new TradeOffers.Factory[] {
                  new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_LEGGINGS, 64, 3, 15, 0.2F),
                  new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_BOOTS, 64, 3, 15, 0.2F)
                },
                5,
                new TradeOffers.Factory[] {
                  new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_HELMET, 64, 3, 30, 0.2F),
                  new TradeOffers.SellEnchantedToolFactory(
                      Items.DIAMOND_CHESTPLATE, 64, 3, 30, 0.2F)
                }));
    PROFESSION_TO_LEVELED_TRADE.put(VillagerProfession.ARMORER, armorer);

    final Int2ObjectMap<TradeOffers.Factory[]> weaponsmith =
        copyToFastUtilMap(
            ImmutableMap.of(
                1,
                new TradeOffers.Factory[] {
                  new TradeOffers.BuyItemFactory(Items.COAL, 30, 16, 2),
                  new TradeOffers.SellItemFactory(new ItemStack(Items.IRON_AXE), 3, 1, 12, 1, 0.2F),
                  new TradeOffers.SellEnchantedToolFactory(Items.IRON_SWORD, 2, 3, 1)
                },
                2,
                new TradeOffers.Factory[] {
                  new TradeOffers.BuyItemFactory(Items.IRON_INGOT, 8, 12, 10),
                  new TradeOffers.SellItemFactory(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2F)
                },
                3,
                new TradeOffers.Factory[] {new TradeOffers.BuyItemFactory(Items.FLINT, 24, 12, 20)},
                4,
                new TradeOffers.Factory[] {
                  new TradeOffers.BuyItemFactory(Items.DIAMOND, 1, 12, 30),
                  new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_AXE, 64, 3, 15, 0.2F)
                },
                5,
                new TradeOffers.Factory[] {
                  new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_SWORD, 64, 3, 30, 0.2F)
                }));
    PROFESSION_TO_LEVELED_TRADE.put(VillagerProfession.WEAPONSMITH, weaponsmith);

    final Int2ObjectMap<TradeOffers.Factory[]> toolsmith =
        copyToFastUtilMap(
            ImmutableMap.of(
                1,
                new TradeOffers.Factory[] {
                  new TradeOffers.BuyItemFactory(Items.COAL, 30, 16, 2),
                  new TradeOffers.SellItemFactory(
                      new ItemStack(Items.STONE_AXE), 1, 1, 12, 1, 0.2F),
                  new TradeOffers.SellItemFactory(
                      new ItemStack(Items.STONE_SHOVEL), 1, 1, 12, 1, 0.2F),
                  new TradeOffers.SellItemFactory(
                      new ItemStack(Items.STONE_PICKAXE), 1, 1, 12, 1, 0.2F),
                  new TradeOffers.SellItemFactory(new ItemStack(Items.STONE_HOE), 1, 1, 12, 1, 0.2F)
                },
                2,
                new TradeOffers.Factory[] {
                  new TradeOffers.BuyItemFactory(Items.IRON_INGOT, 8, 12, 10),
                  new TradeOffers.SellItemFactory(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2F)
                },
                3,
                new TradeOffers.Factory[] {
                  new TradeOffers.BuyItemFactory(Items.FLINT, 30, 12, 20),
                  new TradeOffers.SellEnchantedToolFactory(Items.IRON_AXE, 1, 3, 10, 0.2F),
                  new TradeOffers.SellEnchantedToolFactory(Items.IRON_SHOVEL, 2, 3, 10, 0.2F),
                  new TradeOffers.SellEnchantedToolFactory(Items.IRON_PICKAXE, 3, 3, 10, 0.2F),
                  new TradeOffers.SellItemFactory(
                      new ItemStack(Items.DIAMOND_HOE), 4, 1, 3, 10, 0.2F)
                },
                4,
                new TradeOffers.Factory[] {
                  new TradeOffers.BuyItemFactory(Items.DIAMOND, 1, 12, 30),
                  new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_AXE, 64, 3, 15, 0.2F),
                  new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_SHOVEL, 64, 3, 15, 0.2F)
                },
                5,
                new TradeOffers.Factory[] {
                  new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_PICKAXE, 64, 3, 30, 0.2F)
                }));
    PROFESSION_TO_LEVELED_TRADE.put(VillagerProfession.TOOLSMITH, toolsmith);
  }

  @Contract("_ -> new")
  @Unique
  private static @NotNull Int2ObjectOpenHashMap<TradeOffers.Factory[]> copyToFastUtilMap(
      final ImmutableMap<Integer, TradeOffers.Factory[]> map) {
    return new Int2ObjectOpenHashMap<>(map);
  }
}
