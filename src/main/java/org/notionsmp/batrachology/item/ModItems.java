package org.notionsmp.batrachology.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.notionsmp.batrachology.Batrachology;
import org.notionsmp.batrachology.entity.FrogType;
import org.notionsmp.batrachology.item.custom.FrogBowlItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Batrachology.MODID);

    public static final RegistryObject<Item> AZURE_FROG_BOWL = registerFrogBowl("azure_frog_bowl", FrogType.AZURE);
    public static final RegistryObject<Item> BUMBLEBEE_FROG_BOWL = registerFrogBowl("bumblebee_frog_bowl", FrogType.BUMBLEBEE);
    public static final RegistryObject<Item> GREEN_BLACK_FROG_BOWL = registerFrogBowl("green_black_frog_bowl", FrogType.GREEN_BLACK);
    public static final RegistryObject<Item> GOLDEN_FROG_BOWL = registerFrogBowl("golden_frog_bowl", FrogType.GOLDEN);
    public static final RegistryObject<Item> STRAWBERRY_FROG_BOWL = registerFrogBowl("strawberry_frog_bowl", FrogType.STRAWBERRY);
    public static final RegistryObject<Item> FIERY_FROG_BOWL = registerFrogBowl("fiery_frog_bowl", FrogType.FIERY);
    public static final RegistryObject<Item> NOMINAL_FROG_BOWL = registerFrogBowl("nominal_frog_bowl", FrogType.NOMINAL);
    public static final RegistryObject<Item> AKIS_FROG_BOWL = registerFrogBowl("akis_frog_bowl", FrogType.AKIS);

    private static RegistryObject<Item> registerFrogBowl(String name, FrogType type) {
        return ITEMS.register(name, () -> new FrogBowlItem(
                new Item.Properties().food(ModFoods.FROG_BOWL),
                type
        ));
    }

    public static void init(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}