package hmysjiang.potioncapsule.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BaseMachineBlock extends Block {
	
	private final ITileCreator<?> tileCreator;
	
	public BaseMachineBlock() {
		this(null);
	}

	public BaseMachineBlock(@Nullable ITileCreator<?> tileCreatorIn) {
		super(Properties.create(Material.IRON).hardnessAndResistance(2.0F).sound(SoundType.METAL));
		
		tileCreator = tileCreatorIn;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return tileCreator != null;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return tileCreator.create();
	}
	
	@FunctionalInterface
	public static interface ITileCreator<T extends TileEntity> {
		T create();
	}

}
