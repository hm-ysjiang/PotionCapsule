package hmysjiang.potioncapsule.utils;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;

public class ContainerProvider<T extends Container> implements INamedContainerProvider {
	private final Supplier<ITextComponent> name;
	private final Creator<T> creator;
	
	public ContainerProvider(Supplier<ITextComponent> name, Creator<T> creator) {
		this.name = name;
		this.creator = creator;
	}

	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
		return creator.create(p_createMenu_1_, p_createMenu_2_, p_createMenu_3_);
	}

	@Override
	public ITextComponent getDisplayName() {
		return name.get();
	}
	
	@FunctionalInterface
	public abstract static interface Creator<U extends Container> {
		
		U create(int winId, PlayerInventory inv, PlayerEntity player);
		
	}
	
}
