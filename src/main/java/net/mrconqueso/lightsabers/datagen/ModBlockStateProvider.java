package net.mrconqueso.lightsabers.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrconqueso.lightsabers.AdvancedLightsabers;
import net.mrconqueso.lightsabers.common.block.ModBlocks;
import net.mrconqueso.lightsabers.datagen.content.SimpleBlockItemModel;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, AdvancedLightsabers.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (Block block : SimpleBlockItemModel.blockItems) {
            ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
            if (key != null) {
                blockWithItem(RegistryObject.create(key, ForgeRegistries.BLOCKS));
            }
        }
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
