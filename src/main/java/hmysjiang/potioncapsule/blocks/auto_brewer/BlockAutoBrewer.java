package hmysjiang.potioncapsule.blocks.auto_brewer;

import hmysjiang.potioncapsule.blocks.HorizontalBaseMachineBlock;

public class BlockAutoBrewer extends HorizontalBaseMachineBlock {

	public BlockAutoBrewer() {
		super(TileEntityAutoBrewer::new);
	}
	
}
