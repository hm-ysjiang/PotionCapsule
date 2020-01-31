package hmysjiang.potioncapsule.utils;

import net.minecraft.nbt.CompoundNBT;

public interface ITileCustomSync {
	
	default CompoundNBT getCustomUpdate() {
		return new CompoundNBT();
	}
	
	void readCustomUpdate(CompoundNBT compound);
	
}
